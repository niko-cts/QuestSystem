package net.playlegend.questsystem.player;

import lombok.Getter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

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

    public PlayerDatabaseInformationHolder(boolean needsInsertActiveDirty) {
        this.needsInsertActiveDirty = needsInsertActiveDirty;
        this.newFoundQuests = new HashMap<>();
        this.newCompletedQuests = new HashMap<>();
        this.markActiveQuestDirty = false;
        this.markLanguageDirty = false;
    }


    protected void markActiveQuestDirty() {
        this.markActiveQuestDirty = true;
    }

    protected void markLanguageDirty() {
        this.markLanguageDirty = true;
    }
}
