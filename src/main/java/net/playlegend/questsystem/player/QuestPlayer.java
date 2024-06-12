package net.playlegend.questsystem.player;

import lombok.Getter;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class which stores all important information about the player and the quest.
 * @author Niko
 */
@Getter
@Setter
public class QuestPlayer {

    private final Player player;
    private final Timestamp lastLogout;
    private Language currentLanguage;
    private int coins;

    private final Map<Quest, Timestamp> finishedQuests;
    private boolean markFinishedDirty;
    private final Map<Quest, Timestamp> foundQuests;
    private boolean markFoundDirty;

    private ActivePlayerQuest activePlayerQuest;
    private boolean markActiveDirty;

    public QuestPlayer(Player player, Language language, Timestamp lastLogout) {
        this.player = player;
        this.currentLanguage = language;
        this.lastLogout = lastLogout;
        this.finishedQuests = QuestSystem.getInstance().getQuestManager().loadCompletedQuestIdsByPlayer(player.getUniqueId());
        this.foundQuests = QuestSystem.getInstance().getQuestManager().loadFoundQuestIdsByPlayer(player.getUniqueId());
        this.activePlayerQuest = QuestSystem.getInstance().getQuestManager().loadActiveQuestIdByPlayer(player.getUniqueId(), lastLogout).orElse(null);
        this.markFoundDirty = false;
        this.markFinishedDirty = false;
        this.markActiveDirty = false;

        checkIfExpired();
    }

    public void checkAndFinishActiveQuest() {
        if (activePlayerQuest != null && activePlayerQuest.isQuestFinished()) {
            activePlayerQuest.getActiveQuest().rewards().forEach(r -> r.rewardPlayer(this));
            finishedQuests.put(activePlayerQuest.getActiveQuest(), Timestamp.from(Instant.now()));
            sendMessage(TranslationKeys.QUESTS_EVENT_FINISHED, "${name}", activePlayerQuest.getActiveQuest().name());

            this.activePlayerQuest = null;
            this.markActiveDirty = true;
            this.markFinishedDirty = true;

            ScoreboardUtil.updateScoreboard(this);
        }
    }

    public void checkIfExpired() {
        if (activePlayerQuest != null && activePlayerQuest.getSecondsLeft() <= 0) {
            sendMessage(TranslationKeys.QUESTS_EVENT_TIMER_EXPIRED, "${name}", activePlayerQuest.getActiveQuest().name());
            this.activePlayerQuest = null;
            this.markActiveDirty = true;

            ScoreboardUtil.updateScoreboard(this);
        }
    }

    public void switchActiveQuest(Quest quest) {
        this.activePlayerQuest = new ActivePlayerQuest(quest, Instant.now().plus(quest.finishTimeInSeconds(), ChronoUnit.SECONDS));
        this.markActiveDirty = true;
        sendMessage(TranslationKeys.QUESTS_EVENT_SWITCHED, "${name}", quest.name());
        ScoreboardUtil.updateScoreboard(this);
    }

    public Optional<ActivePlayerQuest> getActivePlayerQuest() {
        return Optional.ofNullable(activePlayerQuest);
    }

    public void sendMessage(String translationKey) {
        player.sendMessage(currentLanguage.translateMessage(translationKey));
    }

    public void sendMessage(String translationKey, String placeholder, String replacement) {
        player.sendMessage(currentLanguage.translateMessage(translationKey, placeholder, replacement));
    }

    public void sendMessage(String translationKey, List<String> placeholder, List<String> replacement) {
        player.sendMessage(currentLanguage.translateMessage(translationKey, placeholder, replacement));
    }

    public void playSound(Sound sound) {
        player.playSound(player, sound, 1, 1);
    }

    public void addItem(ItemStack... item) {
        player.getInventory().addItem(item);
    }
}
