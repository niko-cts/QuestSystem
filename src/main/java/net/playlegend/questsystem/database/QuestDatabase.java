package net.playlegend.questsystem.database;

import net.playlegend.questsystem.quest.reward.IQuestReward;
import net.playlegend.questsystem.quest.steps.QuestStep;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class QuestDatabase {

	protected static final String TABLE_QUESTS = "system_quests_overview";
	protected static final String TABLE_QUEST_STEPS_INFO = "system_quests_steps";
	private static final String TABLE_QUEST_REWARD_INFO = "system_quests_rewards";
	private static final String TABLE_QUEST_REWARD_MAP = "system_quests_has_rewards";

	private static QuestDatabase instance;

	public static QuestDatabase getInstance() {
		if (instance == null)
			instance = new QuestDatabase();
		return instance;
	}

	private final DatabaseHandler dbHandler;

	/**
	 * Creates all database tables regarding basic quest informations
	 */
	private QuestDatabase() {
		this.dbHandler = DatabaseHandler.getInstance();

		dbHandler.createTableIfNotExists(TABLE_QUESTS, List.of(
				"id int NOT NULL PRIMARY KEY AUTO_INCREMENT",
				"name VARCHAR(36) NOT NULL",
				"description VARCHAR(1000) NOT NULL",
				"public BOOLEAN NOT NULL DEFAULT 1",
				"step_order VARCHAR(36) NOT NULL DEFAULT 'CHRONOLOGICAL'",
				"finish_time BIGINT default 3600",
				"timer_runs_offline BOOLEAN NOT NULL DEFAULT 1")
		);

		dbHandler.createTableIfNotExists(TABLE_QUEST_STEPS_INFO, List.of(
				"id INT NOT NULL",
				"quest_id INT NOT NULL",
				"type VARCHAR(36) NOT NULL",
				"amount INT NOT NULL DEFAULT 1",
				"step_object VARCHAR(1000) NOT NULL",
				"PRIMARY KEY(id, quest_id)",
				"CONSTRAINT fk_stepForQuestId FOREIGN KEY(quest_id) REFERENCES " + TABLE_QUESTS + "(id)")
		);


		dbHandler.createTableIfNotExists(TABLE_QUEST_REWARD_INFO, List.of(
				"id INT NOT NULL PRIMARY KEY",
				"type VARCHAR(36) NOT NULL",
				"reward_object VARCHAR(1000) NOT NULL")
		);

		dbHandler.createTableIfNotExists(TABLE_QUEST_REWARD_MAP, List.of(
				"reward_id INT NOT NULL",
				"quest_id INT NOT NULL",
				"CONSTRAINT pk_rewardmaps PRIMARY KEY (reward_id, quest_id)",
				"CONSTRAINT fk_quest_id FOREIGN KEY (quest_id) REFERENCES " + TABLE_QUESTS + "(id)",
				"CONSTRAINT fk_reward_id FOREIGN KEY (reward_id) REFERENCES " + TABLE_QUEST_REWARD_INFO + "(id)")
		);
	}

	/**
	 * Returns the query of all quests (overview of quests)
	 * <p>SELECT * FROM {@link QuestDatabase#TABLE_QUESTS}</p>
	 *
	 * @return ResultSet - All quest infos
	 */
	public ResultSet getAllQuests() {
		return this.dbHandler.select(TABLE_QUESTS, List.of("*"), "");
	}

	/**
	 * Returns the query of every quest step for specified questId
	 * <p>SELECT * FROM {@link QuestDatabase#TABLE_QUEST_STEPS_INFO} WHERE quest_id={param}</p>
	 *
	 * @param questId int - the quest id
	 * @return ResultSet - All quest steps for the quest
	 */
	public ResultSet getAllQuestSteps(int questId) {
		return this.dbHandler.select(TABLE_QUEST_STEPS_INFO, List.of("*"), "quest_id=" + questId + " ORDER BY id");
	}

	/**
	 * Returns the query of all all quest rewards + quest_id's
	 * <p>SELECT reward_id, quest_id, type, reward_object FROM  {@link QuestDatabase#TABLE_QUEST_REWARD_INFO} JOIN {@link QuestDatabase#TABLE_QUEST_REWARD_MAP} AND quest_id={param}</p>
	 *
	 * @param questId int - the quest id
	 * @return ResultSet - All quest rewards with the quest_id's
	 */
	public ResultSet getAllQuestRewards(int questId) {
		return this.dbHandler.executeSQL(String.format(
				"SELECT type, reward_object FROM %s JOIN %s ON id=reward_id AND quest_id=%s",
				TABLE_QUEST_REWARD_INFO, TABLE_QUEST_REWARD_MAP, questId));
	}


	public void deleteQuest(int id) {
		this.dbHandler.delete(List.of(TABLE_QUEST_REWARD_MAP, TABLE_QUEST_STEPS_INFO, TABLE_QUESTS),
				List.of("WHERE quest_id=" + id, "WHERE quest_id=" + id, "WHERE id=" + id + "LIMIT 1"));
	}

	public Optional<Integer> insertNewQuest(String name, String description, List<IQuestReward> rewards, List<QuestStep> steps, long finishTimeInSeconds, boolean isPublic, boolean timerRunsOffline) {
		return Optional.empty();
	}
}
