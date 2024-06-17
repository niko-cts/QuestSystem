package net.playlegend.questsystem.player;

import lombok.Getter;
import lombok.NonNull;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.QuestManager;
import net.playlegend.questsystem.quest.exception.QuestNotFoundException;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.ScoreboardUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Class which stores all important information about the player and the quest.
 *
 * @author Niko
 */
@Getter
public class QuestPlayer {

    private final Player player;
    private final Timestamp lastLogout;
    private final QuestTimerPlayer questTimer;
    private Language currentLanguage;
    private int coins;

    private final Map<Quest, Timestamp> finishedQuests;
    private final Map<Quest, Timestamp> foundQuests;
    private final PlayerDatabaseInformationHolder playerDbInformationHolder;

    private ActivePlayerQuest activePlayerQuest;

    public QuestPlayer(Player player, Language language, Timestamp lastLogout, int coins) {
        this.player = player;
        this.currentLanguage = language;
        this.lastLogout = lastLogout;
        this.coins = coins;
        QuestManager questManager = QuestSystem.getInstance().getQuestManager();
        this.finishedQuests = questManager.loadCompletedQuestIdsByPlayer(player.getUniqueId());
        this.foundQuests = questManager.loadFoundQuestIdsByPlayer(player.getUniqueId());
        this.playerDbInformationHolder = new PlayerDatabaseInformationHolder(activePlayerQuest == null);
        try {
            this.activePlayerQuest = questManager.loadActiveQuestIdByPlayer(player.getUniqueId(), lastLogout).orElse(null);
        } catch (QuestNotFoundException exception) {
            QuestSystem.getInstance().getLogger().info("Player had active quest which could not be found: " + exception.getMessage());
            this.playerDbInformationHolder.markActiveQuestDirty();
            this.activePlayerQuest = null;
        }

        checkAndFinishActiveQuest();
        this.questTimer = new QuestTimerPlayer(this);
    }

    /**
     * Will check and finish the active quest if done.
     * Rewards the player and updates scoreboard.
     */
    public void checkAndFinishActiveQuest() {
        if (activePlayerQuest != null && activePlayerQuest.isQuestFinished()) {
            activePlayerQuest.getActiveQuest().rewards().forEach(r -> r.rewardPlayer(this));
            Timestamp completedAt = Timestamp.from(Instant.now());
            finishedQuests.put(activePlayerQuest.getActiveQuest(), completedAt);
            playerDbInformationHolder.addCompletedQuest(activePlayerQuest.getActiveQuest().id(), completedAt);
            sendMessage(TranslationKeys.QUESTS_EVENT_FINISHED, "${name}", activePlayerQuest.getActiveQuest().name());
            setActivePlayerQuest(null);

            ScoreboardUtil.updateScoreboard(this);
        }
    }

    public void foundQuest(Quest quest) {
        Timestamp foundAt = Timestamp.from(Instant.now());
        foundQuests.put(quest, foundAt);
        playerDbInformationHolder.addFoundQuest(quest.id(), foundAt);
        sendMessage(TranslationKeys.QUESTS_EVENT_FOUND_NEW, "${name}", quest.name());
    }

    public List<Quest> getEveryFoundOrPublicQuest() {
        return QuestSystem.getInstance().getQuestManager().getQuests().stream()
                .filter(q -> activePlayerQuest == null || activePlayerQuest.getActiveQuest().id() != q.id())
                .filter(q -> !finishedQuests.containsKey(q))
                .filter(q -> q.isPublic() || foundQuests.containsKey(q)).toList();
    }

    /**
     * Check if expired and send every needed update
     */
    public void checkIfExpired() {
        checkAndFinishActiveQuest();
        if (activePlayerQuest != null && activePlayerQuest.getSecondsLeft() <= 0) {
            sendMessage(TranslationKeys.QUESTS_EVENT_TIMER_EXPIRED, "${name}", activePlayerQuest.getActiveQuest().name());
            setActivePlayerQuest(null);
            ScoreboardUtil.updateScoreboard(this);
        }
    }

    /**
     * Changes the active quest
     *
     * @param quest Quest - the quest to switch to
     */
    public void switchActiveQuest(@NonNull Quest quest) {
        createAndSetPlayerQuest(quest);
        sendMessage(TranslationKeys.QUESTS_EVENT_SWITCHED, "${name}", quest.name());
        ScoreboardUtil.updateScoreboard(this);
    }

    public void startActiveQuest(@NonNull Quest quest) {
        createAndSetPlayerQuest(quest);
        sendMessage(TranslationKeys.QUESTS_EVENT_STARTED, "${name}", quest.name());
        ScoreboardUtil.updateScoreboard(this);
    }

    public void cancelActiveQuest() {
        setActivePlayerQuest(null);
        sendMessage(TranslationKeys.QUESTS_EVENT_CANCELED);
        ScoreboardUtil.updateScoreboard(this);
    }

    /**
     * Adds one to the current step. Marks the active quest as dirty.
     *
     * @param quest ActivePlayerQuest - the quest the player did the step in.
     * @param step  QuestStep - the step which was done
     * @return boolean - The questStep is finished
     */
    public boolean playerDidQuestStep(ActivePlayerQuest quest, QuestStep step) {
        playerDbInformationHolder.markActiveQuestDirty();
        boolean isDone = quest.playerDidQuestStep(step);
        ScoreboardUtil.updateScoreboard(this);
        return isDone;
    }

    /**
     * Will create a new ActivePlayerQuest.
     *
     * @param quest Quest - the quest to create from
     */
    private void createAndSetPlayerQuest(@NonNull Quest quest) {
        setActivePlayerQuest(new ActivePlayerQuest(quest, Instant.now().plusSeconds(quest.finishTimeInSeconds())));
    }

    /**
     * Sets a new active player quest, restarts the countdown.
     *
     * @param activePlayerQuest ActivePlayerQuest - the active player quest (nullable)
     */
    private void setActivePlayerQuest(ActivePlayerQuest activePlayerQuest) {
        if (this.activePlayerQuest == activePlayerQuest)
            return;
        this.questTimer.cancelTask();
        this.activePlayerQuest = activePlayerQuest;
        this.playerDbInformationHolder.markActiveQuestDirty();
        this.questTimer.startTimerIfActiveQuestPresent();
    }

    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    /**
     * Changes the language and updates the scoreboard.
     *
     * @param currentLanguage Language - the new language of the player
     */
    public void setCurrentLanguage(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
        this.playerDbInformationHolder.markLanguageDirty();
        ScoreboardUtil.updateScoreboard(this);
    }

    /**
     * Changes the number of coins and updates the scoreboard.
     *
     * @param coins int - the new coins of the player
     */
    public void setCoins(int coins) {
        this.coins = coins;
        this.playerDbInformationHolder.markCoinsDirty();
        ScoreboardUtil.updateScoreboard(this);
    }


    public Optional<ActivePlayerQuest> getActivePlayerQuest() {
        return Optional.ofNullable(activePlayerQuest);
    }

    public void sendMessage(String translationKey) {
        player.sendMessage(currentLanguage.translateMessage(translationKey));
    }

    public void sendMessage(String translationKey, String placeholder, Object replacement) {
        player.sendMessage(currentLanguage.translateMessage(translationKey, placeholder, replacement));
    }

    public void sendMessage(String translationKey, List<String> placeholder, List<Object> replacement) {
        player.sendMessage(currentLanguage.translateMessage(translationKey, placeholder, replacement));
    }

    public void playSound(Sound sound) {
        player.playSound(player, sound, 1, 1);
    }

    public void addItem(ItemStack... item) {
        player.getInventory().addItem(item);
    }
}
