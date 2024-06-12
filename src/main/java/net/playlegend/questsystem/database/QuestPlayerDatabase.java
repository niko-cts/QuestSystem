package net.playlegend.questsystem.database;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

public class QuestPlayerDatabase {

	private static final String TABLE_PLAYER_FOUND_QUESTS = "system_quests_player_found";
	private static final String TABLE_PLAYER_COMPLETED_QUESTS = "system_quests_player_completed";
	private static final String TABLE_PLAYER_ACTIVE_QUEST = "system_quests_player_active_quest";
	private static final String TABLE_PLAYER_ACTIVE_QUEST_STEPS = "system_quests_player_active_steps";
	private static QuestPlayerDatabase instance;

	public static QuestPlayerDatabase getInstance() {
		if (instance == null)
			instance = new QuestPlayerDatabase();
		return instance;
	}

	private final DatabaseHandler dbHandler;

	/**
	 * Creates all database tables regarding quest and player information
	 */
	private QuestPlayerDatabase() {
		this.dbHandler = DatabaseHandler.getInstance();

		dbHandler.createTableIfNotExists(TABLE_PLAYER_FOUND_QUESTS, List.of(
				"uuid VARCHAR(36) NOT NULL",
				"quest_id INT NOT NULL",
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
				"PRIMARY KEY(uuid, quest_id)",
				"CONSTRAINT fk_foundquests_qId FOREIGN KEY(quest_id) REFERENCES " + QuestDatabase.TABLE_QUESTS + "(id)",
				"comment 'Each player can find multiple quests. The player can only find quests which are not public'"
		));

		dbHandler.createTableIfNotExists(TABLE_PLAYER_COMPLETED_QUESTS, List.of(
				"uuid VARCHAR(36) NOT NULL",
				"quest_id INT NOT NULL",
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
				"PRIMARY KEY(uuid, quest_id)",
				"CONSTRAINT fk_completedquests_qId FOREIGN KEY(quest_id) REFERENCES " + QuestDatabase.TABLE_QUESTS + "(id)",
				"comment 'Each player can complete multiple quests.'"
		));

		dbHandler.createTableIfNotExists(TABLE_PLAYER_ACTIVE_QUEST_STEPS, List.of(
				"uuid VARCHAR(36) NOT NULL ",
				"step_id INT NOT NULL",
				"amount INT NOT NULL DEFAULT 0",
				"PRIMARY KEY(uuid, step_id)",
				"CONSTRAINT fk_activequests_sId FOREIGN KEY(step_id) REFERENCES " + QuestDatabase.TABLE_QUEST_STEPS_INFO + "(id)",
				"comment 'Each entry represents an active quest-step. Which may be completed or in progress.'"
		));

		dbHandler.createTableIfNotExists(TABLE_PLAYER_ACTIVE_QUEST, List.of(
				"uuid VARCHAR(36) NOT NULL PRIMARY KEY",
				"quest_id INT NOT NULL",
				"time_left BIGINT NOT NULL",
				"CONSTRAINT fk_activequests_qId FOREIGN KEY(quest_id) REFERENCES " + QuestDatabase.TABLE_QUESTS + "(id)",
				"comment 'Each player may activate one quest'"
		));
	}

	public ResultSet getPlayerFoundQuests(UUID uuid) {
		return getPlayerQuests(TABLE_PLAYER_FOUND_QUESTS, uuid);
	}


	public ResultSet getPlayerCompletedQuests(UUID uuid) {
		return getPlayerQuests(TABLE_PLAYER_COMPLETED_QUESTS, uuid);
	}

	public ResultSet getPlayerActiveQuest(UUID uuid) {
		return dbHandler.select(TABLE_PLAYER_ACTIVE_QUEST, List.of("quest_id", "time_left"),
				"WHERE uuid='" + uuid + "' LIMIT 1");
	}

	public ResultSet getPlayerActiveQuestSteps(UUID uuid) {
		return dbHandler.select(TABLE_PLAYER_ACTIVE_QUEST_STEPS, List.of("step_id", "amount"),
				"WHERE uuid='" + uuid + "' ORDER BY step_id");
	}

	private ResultSet getPlayerQuests(String table, UUID uuid) {
		return dbHandler.select(table, List.of("*"), "WHERE uuid='" + uuid + "'");
	}
}
