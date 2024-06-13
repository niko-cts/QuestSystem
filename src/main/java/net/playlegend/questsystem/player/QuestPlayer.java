package net.playlegend.questsystem.player;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.quest.Quest;
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
    @Setter
    private int coins;

    private final Map<Quest, Timestamp> finishedQuests;
    private final Map<Quest, Timestamp> foundQuests;
    private final PlayerDatabaseInformationHolder playerDatabaseInformationHolder;

    private ActivePlayerQuest activePlayerQuest;


    public QuestPlayer(Player player, Language language, Timestamp lastLogout) {
        this.player = player;
        this.currentLanguage = language;
        this.lastLogout = lastLogout;
        this.finishedQuests = QuestSystem.getInstance().getQuestManager().loadCompletedQuestIdsByPlayer(player.getUniqueId());
        this.foundQuests = QuestSystem.getInstance().getQuestManager().loadFoundQuestIdsByPlayer(player.getUniqueId());
        this.activePlayerQuest = QuestSystem.getInstance().getQuestManager().loadActiveQuestIdByPlayer(player.getUniqueId(), lastLogout).orElse(null);
        this.playerDatabaseInformationHolder = new PlayerDatabaseInformationHolder(activePlayerQuest == null);
        this.questTimer = new QuestTimerPlayer(this);
    }

    /**
     * Will check and finish the active quest if done.
     * Rewards the player and updates scoreboard.
     */
    public void checkAndFinishActiveQuest() {
        if (activePlayerQuest != null && activePlayerQuest.isQuestFinished()) {
            activePlayerQuest.getActiveQuest().rewards().forEach(r -> r.rewardPlayer(this));
            finishedQuests.put(activePlayerQuest.getActiveQuest(), Timestamp.from(Instant.now()));
            playerDatabaseInformationHolder.getNewCompletedQuests().put(activePlayerQuest.getActiveQuest().id(), Timestamp.from(Instant.now()));
            sendMessage(TranslationKeys.QUESTS_EVENT_FINISHED, "${name}", activePlayerQuest.getActiveQuest().name());
            setActivePlayerQuest(null);

            ScoreboardUtil.updateScoreboard(this);
        }
    }

    /**
     * Check if expired and send every needed update
     */
    public void checkIfExpired() {
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
        playerDatabaseInformationHolder.markActiveQuestDirty();


        this.questTimer.startTimerIfActiveQuestPresent();
    }

    /**
     * Changes the language and updates the scoreboard.
     *
     * @param currentLanguage Language - the new language of the player
     */
    public void setCurrentLanguage(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
        this.playerDatabaseInformationHolder.markLanguageDirty();
        ScoreboardUtil.updateScoreboard(this);
    }
}
