package net.playlegend.questsystem.database;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.reward.QuestReward;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.util.QuestObjectConverterUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class QuestDatabase {

	protected static final String TABLE_QUESTS = "system_quests_overview";
	protected static final String TABLE_QUEST_STEPS_INFO = "system_quests_steps";
	private static final String TABLE_QUEST_REWARD_INFO = "system_quests_rewards";

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
				"finish_time BIGINT default 3600",
				"timer_runs_offline BOOLEAN NOT NULL DEFAULT 1")
		);

		dbHandler.createTableIfNotExists(TABLE_QUEST_STEPS_INFO, List.of(
				"id INT NOT NULL",
				"quest_id INT NOT NULL",
				"type VARCHAR(36) NOT NULL",
				"amount INT NOT NULL DEFAULT 1",
				"step_order INT NOT NULL DEFAULT 1",
				"step_object VARCHAR(1000) NOT NULL",
				"PRIMARY KEY(id, quest_id)",
				"FOREIGN KEY(quest_id) REFERENCES " + TABLE_QUESTS + "(id) ON DELETE CASCADE")
		);


		dbHandler.createTableIfNotExists(TABLE_QUEST_REWARD_INFO, List.of(
				"type VARCHAR(16) NOT NULL",
				"reward_object VARCHAR(750) NOT NULL",
				"quest_id INT NOT NULL",
				"PRIMARY KEY (type, reward_object, quest_id)",
				"FOREIGN KEY (quest_id) REFERENCES " + TABLE_QUESTS + "(id) ON DELETE CASCADE")
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
		return this.dbHandler.select(TABLE_QUEST_STEPS_INFO, List.of("*"), "WHERE quest_id=" + questId);
	}

	/**
	 * Returns the query of all all quest rewards + quest_id's
	 * <p>SELECT reward_id, quest_id, type, reward_object FROM  {@link QuestDatabase#TABLE_QUEST_REWARD_INFO} JOIN {@link QuestDatabase#TABLE_QUEST_REWARD_INFO} AND quest_id={param}</p>
	 *
	 * @param questId int - the quest id
	 * @return ResultSet - All quest rewards with the quest_id's
	 */
	public ResultSet getAllQuestRewards(int questId) {
		return this.dbHandler.select(TABLE_QUEST_REWARD_INFO, List.of("type", "reward_object"), "WHERE quest_id=" + questId);
	}

	/**
	 * Deletes the quest. All other quest information will be cascaded.
	 *
	 * @param id int - quest id
	 */
	public void deleteQuest(int id) {
		this.dbHandler.delete(List.of(TABLE_QUESTS), List.of("WHERE id=" + id + " LIMIT 1"));
	}

	/**
	 * Inserts new Quest, QuestStep, QuestRewards and maps the rewards with the quest
	 *
	 * @param name                String - quest name
	 * @param description         String - quest description
	 * @param rewards             List - all rewards the quest has
	 * @param steps               List - all steps the quest has
	 * @param finishTimeInSeconds long - the quest countdown in seconds
	 * @param isPublic            boolean - if a quest is public
	 * @param timerRunsOffline    boolean - if countdown runs offline as well
	 * @return Optional<Integer> - the id of the created quest
	 */
	public Optional<Integer> insertNewQuest(String name, String description, List<QuestReward<?>> rewards, List<QuestStep<?>> steps, long finishTimeInSeconds, boolean isPublic, boolean timerRunsOffline) {
		Logger log = QuestSystem.getInstance().getLogger();

		Integer questId = this.dbHandler.insertIntoTableAndGetGeneratedKey(TABLE_QUESTS,
				List.of("name", "description", "public", "finish_time", "timer_runs_offline"),
				List.of(name, description, isPublic ? 1 : 0, finishTimeInSeconds, timerRunsOffline ? 1 : 0));
		if (questId == null) {
			log.severe("Could not insert quest in database");
			return Optional.empty();
		}

		List<Object> allSteps = new ArrayList<>();
		Iterator<QuestStep<?>> newSteps = steps.iterator();
		while (newSteps.hasNext()) {
			QuestStep<?> step = newSteps.next();
			try {
				addStepFieldsToList(allSteps, step, questId);
				if (newSteps.hasNext())
					allSteps.add(null);
			} catch (IOException e) {
				log.log(Level.SEVERE, "Could not convert new StepObject to database string", e);
			}
		}


		if (allSteps.isEmpty()) {
			log.log(Level.SEVERE, "There is no quest step which can be added to the quest. Aborting...");
			deleteQuest(questId);
			return Optional.empty();
		}
		List<String> tableNames = new ArrayList<>();
		List<List<Object>> allValues = new ArrayList<>();
		tableNames.add(TABLE_QUEST_STEPS_INFO);
		allValues.add(allSteps);

		List<Object> allRewards = new ArrayList<>();
		Iterator<QuestReward<?>> rewardIterator = rewards.iterator();
		while (rewardIterator.hasNext()) {
			try {
				QuestReward<?> reward = rewardIterator.next();
				addRewardFieldsToList(allRewards, reward, questId);
				if (rewardIterator.hasNext()) {
					allRewards.add(null);
				}
			} catch (IOException ex) {
				log.log(Level.SEVERE, "Could not convert reward object to database string for new quest", ex);
			}
		}

		if (!allRewards.isEmpty()) {
			tableNames.add(TABLE_QUEST_REWARD_INFO);
			allValues.add(allRewards);
		}

		dbHandler.insertIntoTable(tableNames, allValues);
		return Optional.of(questId);
	}

	/**
	 * Updates all necessary quest informations.
	 * Deletes, inserts new elements.
	 *
	 * @param oldQuest Quest - the old quest
	 * @param newQuest Quest - the new updated quest with the same quest id
	 * @return boolean - update was successful
	 */
	public boolean updateQuest(Quest oldQuest, Quest newQuest) {
		List<String> updateTableNames = new ArrayList<>();
		List<List<String>> updateColumns = new ArrayList<>();
		List<List<Object>> updateValues = new ArrayList<>();
		List<String> updateWhereClauses = new ArrayList<>();

		List<String> insertTableNames = new ArrayList<>();
		List<List<Object>> insertValues = new ArrayList<>();

		List<String> deleteTables = new ArrayList<>();
		List<String> deleteWheres = new ArrayList<>();

		// QUEST DATA

		List<String> questColumns = new ArrayList<>();
		List<Object> questValues = new ArrayList<>();
		if (!oldQuest.name().equals(newQuest.name())) {
			questColumns.add("name");
			questValues.add(newQuest.name());
		}
		if (!oldQuest.description().equals(newQuest.description())) {
			questColumns.add("description");
			questValues.add(newQuest.description());
		}
		if (oldQuest.isPublic() != newQuest.isPublic()) {
			questColumns.add("public");
			questValues.add(newQuest.isPublic() ? 1 : 0);
		}
		if (oldQuest.timerRunsOffline() != newQuest.timerRunsOffline()) {
			questColumns.add("timer_runs_offline");
			questValues.add(newQuest.timerRunsOffline() ? 1 : 0);
		}
		if (oldQuest.finishTimeInSeconds() != newQuest.finishTimeInSeconds()) {
			questColumns.add("finish_time");
			questValues.add(newQuest.finishTimeInSeconds());
		}
		if (!questColumns.isEmpty()) {
			updateTableNames.add(TABLE_QUESTS);
			updateColumns.add(questColumns);
			updateValues.add(questValues);
			updateWhereClauses.add("WHERE id=" + newQuest.id() + " LIMIT 1");
		}


		// STEPS

		List<String> stepsUpdateColumn = new ArrayList<>();
		List<Object> stepsUpdateValues = new ArrayList<>();
		List<Integer> stepsIdWheres = new ArrayList<>();
		List<Object> newSteps = new ArrayList<>();

		List<QuestStep<?>> oldSteps = new ArrayList<>(oldQuest.completionSteps());
		for (QuestStep<?> newStep : newQuest.completionSteps()) {
			Optional<QuestStep<?>> oldStep = oldSteps.stream().filter(o -> o.getId() == newStep.getId()).findFirst();
			if (oldStep.isPresent()) {
				try {
					int oldColumnSize = stepsUpdateColumn.size();
					updateStepInList(oldStep.get(), newStep, stepsUpdateColumn, stepsUpdateValues);
					if (oldColumnSize != stepsUpdateColumn.size())
						stepsIdWheres.add(newStep.getId());
				} catch (IOException exception) {
					QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not convert quest step object for database update", exception);
				}
				oldSteps.remove(oldStep.get());
			} else {
				try {
					if (!newSteps.isEmpty())
						newSteps.add(null);
					addStepFieldsToList(newSteps, newStep, newQuest.id());
				} catch (IOException exception) {
					QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not convert quest step object for database insertion", exception);
				}
			}
		}
		if (!oldSteps.isEmpty()) {
			deleteTables.add(TABLE_QUEST_STEPS_INFO);
			deleteWheres.add("WHERE quest_id=" + oldQuest.id() + " AND id IN (" + oldSteps.stream().map(s -> s.getId() + "").collect(Collectors.joining(",")) + ")");
		}

		if (!stepsUpdateColumn.isEmpty()) {
			updateTableNames.add(TABLE_QUEST_STEPS_INFO);
			updateColumns.add(stepsUpdateColumn);
			updateValues.add(stepsUpdateValues);
			updateWhereClauses.add("WHERE quest_id=" + newQuest.id() +
			                       " AND id in(" + stepsIdWheres.stream().map(String::valueOf).collect(Collectors.joining(",")) + ") LIMIT " + stepsIdWheres.size());
		}
		if (!newSteps.isEmpty()) {
			insertTableNames.add(TABLE_QUEST_STEPS_INFO);
			insertValues.add(newSteps);
		}

		// REWARDS
		List<Object> newRewards = new ArrayList<>();

		List<QuestReward<?>> oldRewards = new ArrayList<>(oldQuest.rewards());
		for (QuestReward<?> reward : newQuest.rewards()) {
			try {
				if (oldRewards.contains(reward)) {
					oldRewards.remove(reward);
				} else {
					if (!newRewards.isEmpty())
						newRewards.add(null);
					addRewardFieldsToList(newRewards, reward, newQuest.id());
				}
			} catch (IOException exception) {
				QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not convert quest reward object for database insertion", exception);
			}
		}
		if (!newRewards.isEmpty()) {
			insertTableNames.add(TABLE_QUEST_REWARD_INFO);
			insertValues.add(newRewards);
		}
		if (!oldRewards.isEmpty()) { // delete not found rewards
			deleteTables.add(TABLE_QUEST_REWARD_INFO);

			List<String> types = new ArrayList<>();
			List<String> objects = new ArrayList<>();
			for (QuestReward<?> oldReward : oldRewards) {
				try {
					types.add(oldReward.getRewardType().name());
					objects.add(QuestObjectConverterUtil.convertObjectToDatabaseString(oldReward.getRewardObject()));
				} catch (IOException exception) {
					QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not convert quest reward object for database insertion", exception);
				}
			}

			deleteWheres.add("WHERE type in ('" + String.join("','", types) + "') AND reward_object in ('" +
			                 String.join("','", objects) + "') AND quest_id=" + newQuest.id() + " LIMIT 1");
		}

		// FINISH

		boolean successful = true;
		if (!updateTableNames.isEmpty())
			successful = dbHandler.update(updateTableNames, updateColumns, updateValues, updateWhereClauses);
		if (successful && !deleteTables.isEmpty())
			successful = dbHandler.delete(deleteTables, deleteWheres);
		if (successful && !insertTableNames.isEmpty())
			successful = dbHandler.insertIntoTable(insertTableNames, insertValues);
		return successful;
	}

	private void updateStepInList(QuestStep<?> old, QuestStep<?> newStep, List<String> columns, List<Object> values) throws IOException {
		if (old.getOrder() != newStep.getOrder()) {
			columns.add("step_order");
			values.add(newStep.getOrder());
		}
		if (old.getType() != newStep.getType()) {
			columns.add("type");
			values.add(newStep.getType().name());
		}
		if (!old.getStepObject().equals(newStep.getStepObject())) {
			columns.add("step_object");
			values.add(QuestObjectConverterUtil.convertObjectToDatabaseString(newStep.getStepObject()));
		}
		if (old.getMaxAmount() != newStep.getMaxAmount()) {
			columns.add("amount");
			values.add(newStep.getMaxAmount());
		}
	}

	private void addRewardFieldsToList(List<Object> allRewards, QuestReward<?> reward, Integer questId) throws IOException {
		allRewards.add(reward.getRewardType().name());
		allRewards.add(QuestObjectConverterUtil.convertObjectToDatabaseString(reward.getRewardObject()));
		allRewards.add(questId);
	}

	private void addStepFieldsToList(List<Object> allSteps, QuestStep<?> step, Integer questId) throws IOException {
		allSteps.add(step.getId());
		allSteps.add(questId);
		allSteps.add(step.getType().name());
		allSteps.add(step.getMaxAmount());
		allSteps.add(step.getOrder());
		allSteps.add(QuestObjectConverterUtil.convertObjectToDatabaseString(step.getStepObject()));
	}
}
