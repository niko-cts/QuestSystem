package net.playlegend.questsystem.database;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

public class QuestPlayerDatabase {

    private static final String TABLE_PLAYER_FOUND_QUESTS = "system_quests_player_found";
    private static final String TABLE_PLAYER_COMPLETED_QUESTS = "system_quests_player_completed";
    private static final String TABLE_PLAYER_ACTIVE_QUEST = "system_quests_player_active";

    private final DatabaseHandler dbHandler;

    /**
     * Creates all database tables regarding quest and player informations
     */
    public QuestPlayerDatabase() {
        this.dbHandler = DatabaseHandler.getInstance();

        dbHandler.createTableIfNotExists(TABLE_PLAYER_FOUND_QUESTS, List.of(
                "uuid VARCHAR(36) NOT NULL",
                "quest_id INT NOT NULL",
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
                "PRIMARY KEY(uuid, quest_id)",
                "CONSTRAINT fk_foundquests_qId FOREIGN KEY(quest_id) REFERENCES " + QuestDatabase.TABLE_QUESTS + "(quest_id)",
                "comment 'Each player can find multiple quests. The player can only find quests which are not public'"
        ));

        dbHandler.createTableIfNotExists(TABLE_PLAYER_COMPLETED_QUESTS, List.of(
                "uuid VARCHAR(36) NOT NULL",
                "quest_id INT NOT NULL",
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
                "PRIMARY KEY(uuid, quest_id)",
                "CONSTRAINT fk_completedquests_qId FOREIGN KEY(quest_id) REFERENCES " + QuestDatabase.TABLE_QUESTS + "(quest_id)",
                "comment 'Each player can find multiple quests.'"
        ));

        dbHandler.createTableIfNotExists(TABLE_PLAYER_ACTIVE_QUEST, List.of(
                "uuid VARCHAR(36) NOT NULL PRIMARY KEY",
                "quest_id INT NOT NULL",
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
                "CONSTRAINT fk_activequests_qId FOREIGN KEY(quest_id) REFERENCES " + QuestDatabase.TABLE_QUESTS + "(quest_id)",
                "comment 'Each player can find multiple quests.'"
        ));
    }

    public ResultSet getPlayerFoundQuests(UUID uuid) {
        return getPlayerQuests(TABLE_PLAYER_FOUND_QUESTS, uuid);
    }


    public ResultSet getPlayerCompletedQuests(UUID uuid) {
        return getPlayerQuests(TABLE_PLAYER_COMPLETED_QUESTS, uuid);
    }

    public ResultSet getPlayerActiveQuests(UUID uuid) {
        return getPlayerQuests(TABLE_PLAYER_ACTIVE_QUEST, uuid);
    }

    private ResultSet getPlayerQuests(String table, UUID uuid) {
        return dbHandler.select(table, List.of("*"), "WHERE uuid='" + uuid + "'");
    }
}
