package net.playlegend.questsystem.database;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class QuestDatabase {

	private static final String TABLE_QUESTS = "system_quests_overview";
	private static final String TABLE_QUEST_STEPS_INFO = "system_quests_steps";
	private static final String TABLE_QUEST_REWARD_INFO = "system_quests_rewards";
	private static final String TABLE_QUEST_REWARD_MAP = "system_quests_has_rewards";

	private final DatabaseHandler dbHandler;

	/**
	 * Creates all database tables regarding basic quest informations
	 */
	public QuestDatabase() {
		this.dbHandler = DatabaseHandler.getInstance();

		dbHandler.createTable(TABLE_QUESTS,
				Arrays.asList(
						"id int NOT NULL PRIMARY KEY AUTO_INCREMENT",
						"name VARCHAR(36) NOT NULL",
						"description VARCHAR(1000) NOT NULL",
						"step_order VARCHAR(36) NOT NULL DEFAULT 'CHRONOLOGICAL'",
						"finish_time BIGINT default 3600",
						"comment 'Information about every quest'"));

		dbHandler.createTable(TABLE_QUEST_STEPS_INFO,
				Arrays.asList("id INT NOT NULL",
						"quest_id INT NOT NULL",
						"type VARCHAR(36) NOT NULL",
						"amount INT NOT NULL DEFAULT 1",
						"step_object VARCHAR(1000) NOT NULL",
						"PRIMARY KEY(id, quest_id)",
						"CONSTRAINT fk_stepForQuestId FOREIGN KEY(quest_id) REFERENCES " + TABLE_QUESTS + "(id)",
						"comment 'Every quest (quest_id) may has multiple steps which needs to be done. The object represents the parameter of the step type which may be an integer, a bukkit.Material or else'"));


		dbHandler.createTable(TABLE_QUEST_REWARD_INFO,
				Arrays.asList("id INT NOT NULL PRIMARY KEY",
						"type VARCHAR(36) NOT NULL",
						"reward_object VARCHAR(1000) NOT NULL",
						"comment 'All rewards that can be given, when a quest is completed. The reward_object represents the parameter for the reward type which may be an integer, an itemstack or else'"));

		dbHandler.createTable(TABLE_QUEST_REWARD_MAP,
				Arrays.asList("reward_id INT NOT NULL",
						"quest_id INT NOT NULL",
						"CONSTRAINT pk_rewardmaps PRIMARY KEY (reward_id, quest_id)",
						"CONSTRAINT fk_quest_id FOREIGN KEY (quest_id) REFERENCES " + TABLE_QUESTS + "(id)",
						"CONSTRAINT fk_reward_id FOREIGN KEY (reward_id) REFERENCES " + TABLE_QUEST_REWARD_INFO + "(id)",
						"comment 'A relationship between a quest and a reward. A quest can have multiple rewards. As rewards may be the same over multiple quests, this relation was separated.'"));
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
		return this.dbHandler.select(TABLE_QUEST_STEPS_INFO, List.of("*"), "quest_id=" + questId);
	}

	/**
	 * Returns the query of all all quest rewards + quest_id's
	 * <p>SELECT reward_id, quest_id, type, reward_object FROM  {@link QuestDatabase#TABLE_QUEST_REWARD_INFO} JOIN {@link QuestDatabase#TABLE_QUEST_REWARD_MAP} AND quest_id={param}</p>
	 *
	 * @param questId int - the quest id
	 * @return ResultSet - All quest rewards with the quest_id's
	 */
	public ResultSet getAllQuestRewards(int questId) {
		return this.dbHandler.executeSQL(new StringBuilder()
				.append("SELECT reward_id, quest_id, type, reward_object FROM ")
				.append(TABLE_QUEST_REWARD_INFO).append(" JOIN ")
				.append(TABLE_QUEST_REWARD_MAP)
				.append(" ON id=reward_id AND quest_id=").append(questId).toString());
	}


}
