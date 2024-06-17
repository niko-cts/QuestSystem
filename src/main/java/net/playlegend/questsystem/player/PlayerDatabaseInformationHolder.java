package net.playlegend.questsystem.player;

import lombok.Getter;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class only holds information about the current session of the player.
 * This is necessary to make sure the data gets inserted correct as soon as the QuestPlayer disconnects.
 * @author Niko
 */
@Getter
public class PlayerDatabaseInformationHolder {
    private final Map<Integer, Timestamp> newFoundQuests;
    private final Map<Integer, Timestamp> newCompletedQuests;
    private final boolean needsInsertActiveDirty;
    private boolean markLanguageDirty;
    private boolean markActiveQuestDirty;
    private boolean markCoinsDirty;

    public PlayerDatabaseInformationHolder(boolean needsInsertActiveDirty) {
        this.needsInsertActiveDirty = needsInsertActiveDirty;
        this.newFoundQuests = new ConcurrentHashMap<>();
        this.newCompletedQuests = new ConcurrentHashMap<>();
        this.markActiveQuestDirty = false;
        this.markLanguageDirty = false;
    }

    protected void addFoundQuest(Integer questId, Timestamp foundAt) {
        this.newFoundQuests.put(questId, foundAt);
    }

    protected void addCompletedQuest(Integer questId, Timestamp foundAt) {
        this.newCompletedQuests.put(questId, foundAt);
    }

    protected void markActiveQuestDirty() {
        this.markActiveQuestDirty = true;
    }

    protected void markLanguageDirty() {
        this.markLanguageDirty = true;
    }

    protected void markCoinsDirty() {
        this.markCoinsDirty = true;
    }

}
