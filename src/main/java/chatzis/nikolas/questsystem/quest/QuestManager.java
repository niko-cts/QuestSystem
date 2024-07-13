package chatzis.nikolas.questsystem.quest;

import chatzis.nikolas.questsystem.QuestSystem;
import chatzis.nikolas.questsystem.database.PlayerQuestDatabase;
import chatzis.nikolas.questsystem.database.QuestDatabase;
import chatzis.nikolas.questsystem.player.ActivePlayerQuest;
import chatzis.nikolas.questsystem.quest.exception.QuestNotFoundException;
import chatzis.nikolas.questsystem.quest.exception.QuestStepNotFoundException;
import chatzis.nikolas.questsystem.quest.reward.QuestReward;
import chatzis.nikolas.questsystem.quest.reward.RewardType;
import chatzis.nikolas.questsystem.quest.steps.QuestStep;
import chatzis.nikolas.questsystem.quest.steps.QuestStepType;
import chatzis.nikolas.questsystem.util.QuestObjectConverterUtil;
import chatzis.nikolas.questsystem.util.QuestTimingsUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This manager loads all quests, quest steps and quest rewards and stores it.
 *
 * @author Niko
 */
public class QuestManager {

	private final Vector<Quest> quests;
	private final PlayerQuestDatabase playerDatabase;

	/**
	 * Will load every quest and adds it to the List.
	 * First load all quest information, then, according to quest step and reward.
	 * Won't add the quest if there is no quest step.
	 *
	 * @see QuestDatabase
	 */
	public QuestManager() {
		this.quests = new Vector<>();
		QuestDatabase questDatabase = QuestDatabase.getInstance();
		this.playerDatabase = PlayerQuestDatabase.getInstance(); // needs to come after questDatabase
		Logger log = QuestSystem.getInstance().getLogger();

		try (ResultSet questResult = questDatabase.getAllQuests()) {
			while (questResult != null && questResult.next()) {
				List<QuestReward<?>> rewards = new ArrayList<>();
				List<QuestStep<?>> steps = new LinkedList<>();
				int id = questResult.getInt("id");
				String name = questResult.getString("name");
				String questIDForLog = id + ":'" + name + "'";

				// STEPS
				try (ResultSet stepResult = questDatabase.getAllQuestSteps(id)) {
					while (stepResult != null && stepResult.next()) {
						QuestStepType questStepType;
						int stepId = stepResult.getInt("id");
						try {
							questStepType = QuestStepType.valueOf(stepResult.getString("type").toUpperCase());
						} catch (IllegalArgumentException exception) {
							log.log(Level.SEVERE, "Invalid step type for quest {0} at quest step '{1}'! " +
							                      "Tried to insert {2}, but [{3}] is accepted only.",
									new Object[]{questIDForLog, stepId, stepResult.getString("step_type"),
											Arrays.stream(QuestStepType.values()).map(Enum::name).collect(Collectors.joining(", "))});
							continue;
						}

						int amount = stepResult.getInt("amount");
						int order = stepResult.getInt("step_order");
						try {
							steps.add(
									QuestObjectConverterUtil.instantiateQuestStepFromTypeAndParameter(questStepType, stepId, order, amount,
											stepResult.getString("step_object"))
							);
						} catch (Exception e) {
							log.log(Level.SEVERE, "Could not create quest step for quest {0} at quest step '{1}'! " +
							                      "Probably the wrong string was inserted in the step_object" +
							                      "Constructor of type was {2} and parameter was {3}" +
							                      "Exception is: {4}",
									new Object[]{questIDForLog, stepId, Arrays.toString(questStepType.getQuestStepClass().getConstructors()), stepResult.getString("step_object"), e.getMessage()});
						}
					}
				} catch (SQLException e) {
					log.log(Level.SEVERE, "There was an error reading quests steps for quest " + questIDForLog, e);
				}

				// REWARDS
				try (ResultSet rewardResult = questDatabase.getAllQuestRewards(id)) {
					while (rewardResult != null && rewardResult.next()) {
						RewardType rewardType;

						try {
							rewardType = RewardType.valueOf(rewardResult.getString("type").toUpperCase());
						} catch (IllegalArgumentException exception) {
							log.log(Level.SEVERE, "Invalid reward type for quest {0}! Tried to insert {1}, but [{2}] is accepted only.",
									new Object[]{questIDForLog, rewardResult.getString("type"),
											Arrays.stream(RewardType.values()).map(Enum::name).collect(Collectors.joining(", "))});
							continue;
						}

						try {
							Object rewardObject = QuestObjectConverterUtil.convertDatabaseStringToQuestRewardObject(rewardResult.getString("reward_object"), rewardType);
							rewards.add(rewardType.getQuestRewardInstance(rewardObject));
						} catch (Exception e) {
							log.log(Level.SEVERE, "Could not create reward for quest {0}! " +
							                      "The reward_object could not be parsed. Probably the wrong string was inserted in the reward_object column." +
							                      "Needed a {1} and got: {2}" +
							                      "Exception is: {3}",
									new Object[]{questIDForLog, rewardType.getConstructorParameter(), rewardResult.getString("reward_object"), e.getMessage()});
						}
					}
				} catch (SQLException e) {
					log.log(Level.SEVERE, "There was an error reading quests rewards for quest " + questIDForLog, e);
				}

				if (steps.isEmpty()) {
					log.log(Level.WARNING, "There were no valid steps for {0}. Continuing without adding it...", questIDForLog);
					continue;
				}

				quests.add(new Quest(questResult.getInt("id"), questResult.getString("name"), questResult.getString("description"), questResult.getBoolean("public"), rewards,
						steps, questResult.getLong("finish_time"), questResult.getBoolean("timer_runs_offline")));
			}

			if (quests.isEmpty())
				log.warning("No quests were added.");
			else
				log.log(Level.INFO, "Added Quests ({0}): {1}",
						new Object[]{quests.size(), quests.stream().map(Quest::name).collect(Collectors.joining(", "))});
		} catch (SQLException exception) {
			log.log(Level.SEVERE, "There was an error while trying to get all the quests", exception);
		}
	}


	public Map<Quest, Timestamp> loadCompletedQuestIdsByPlayer(UUID uuid) {
		Map<Quest, Timestamp> quests = new HashMap<>();
		try (ResultSet set = playerDatabase.getPlayerCompletedQuests(uuid)) {
			while (set != null && set.next()) {
				putInMapOrLog(quests, set.getInt("quest_id"), set.getTimestamp("created_at"));
			}
		} catch (SQLException e) {
			QuestSystem.getInstance().getLogger().log(Level.SEVERE, "There was an error when loading completed player quests.", e);
		}
		return quests;
	}

	public Map<Quest, Timestamp> loadFoundQuestIdsByPlayer(UUID uuid) {
		Map<Quest, Timestamp> quests = new HashMap<>();
		try (ResultSet set = playerDatabase.getPlayerFoundQuests(uuid)) {
			while (set != null && set.next()) {
				putInMapOrLog(quests, set.getInt("quest_id"), set.getTimestamp("created_at"));
			}
		} catch (SQLException e) {
			QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not load error when loading completed player quests.", e);
		}
		return quests;
	}

	/**
	 * Loads an active player quest from the database with all steps.
	 *
	 * @param uuid      UUID - the uuid of the player
	 * @param lastlogin Timestamp - When necessary, subtracts the duration from it
	 * @return Optional<ActivePlayerQuest> - If player has an active quest
	 * @throws QuestNotFoundException - If a quest id was loaded but not found in the cache
	 * @see ActivePlayerQuest
	 */
	public Optional<ActivePlayerQuest> loadActiveQuestIdByPlayer(UUID uuid, Timestamp lastlogin)
			throws QuestNotFoundException {
		Logger log = QuestSystem.getInstance().getLogger();
		try (ResultSet questOverviewResult = playerDatabase.getPlayerActiveQuest(uuid)) {
			if (questOverviewResult == null || !questOverviewResult.next()) {
				return Optional.empty();
			}
			int questId = questOverviewResult.getInt("quest_id");
			Optional<Quest> questOptional = getQuestById(questId);
			if (questOptional.isEmpty()) {
				throw new QuestNotFoundException(questId, "was loaded from database but could not be found");
			}
			Quest quest = questOptional.get();


			Map<QuestStep<?>, Integer> steps = new HashMap<>();
			try (ResultSet stepResults = playerDatabase.getPlayerActiveQuestSteps(uuid)) {
				while (stepResults != null && stepResults.next()) {
					int stepId = stepResults.getInt("step_id");
					Optional<QuestStep<?>> questStep = getQuestStep(quest, stepId);
					if (questStep.isEmpty()) {
						log.log(Level.WARNING, "Could not find QuestStep object of step id {0} for quest: {1}",
								new Object[]{stepId, questId});
						continue;
					}
					steps.put(questStep.get(), stepResults.getInt("amount"));
				}
			} catch (SQLException exception) {
				log.log(Level.SEVERE,
						"Could not load quest {0} of player {1}: {2}", new Object[]{questId, uuid, exception.getMessage()});
			}

			if (steps.isEmpty()) {
				throw new QuestStepNotFoundException(questId, "quest was loaded but could not find any quest step");
			}

			long secondsLeft = questOverviewResult.getLong("time_left");

			return Optional.of(new ActivePlayerQuest(quest, steps,
					QuestTimingsUtil.calculatedInstantLeftAfterLogin(secondsLeft, quest.timerRunsOffline(), lastlogin)));
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Could not load active player quests", e);
		}
		return Optional.empty();
	}


	public List<Quest> getPublicQuests() {
		return getQuests().stream().filter(Quest::isPublic).toList();
	}

	public Optional<QuestStep<?>> getQuestStep(Quest quest, int stepId) {
		return quest.completionSteps().stream().filter(s -> s.getId() == stepId).findFirst();
	}

	public Optional<Quest> getQuestById(int id) {
		return getQuests().stream().filter(q -> q.id() == id).findFirst();
	}

	public Optional<Quest> getQuestByName(String name) {
		return getQuests().stream().filter(q -> q.name().equals(name)).findFirst();
	}

	public List<Quest> getQuests() {
		return new ArrayList<>(quests);
	}

	/**
	 * Tries to get the quest by the questId. If found, put it in the given map. Else logs an error.
	 *
	 * @param quests    Map<Quest, Timestamp> - the quest and the timestamp
	 * @param questId   int - the id of the quest
	 * @param createdAt Timestamp - the created at timestamp.
	 */
	private void putInMapOrLog(Map<Quest, Timestamp> quests, int questId, Timestamp createdAt) {
		Optional<Quest> quest = getQuestById(questId);
		if (quest.isPresent()) {
			quests.put(quest.get(), createdAt);
		} else {
			QuestSystem.getInstance().getLogger().log(Level.SEVERE,
					"Could not load quest {0} but is in players list.", questId);
		}
	}

	public void deleteQuest(String name) {
		getQuestByName(name).ifPresent(quest -> {
			quests.remove(quest);
			QuestDatabase.getInstance().deleteQuest(quest.id());
		});
	}

	/**
	 * Adds a new Quest to the System.
	 *
	 * @param name                String - name of quest
	 * @param description         String - description of quest (; represents line breaks, §n represents color)
	 * @param rewards             List<QuestReward<?>>- a list of all rewards a player receives on completion
	 * @param steps               List<QuestStep<?>> - a list of all steps a player needs to complete
	 * @param finishTimeInSeconds long - the amount of seconds the player has to complete the quest
	 * @param isPublic            boolean - quest is publicly visible in the menu
	 * @param timerRunsOffline    boolean - timer runs offline
	 * @return boolean - creation was successful
	 */
	public boolean addQuest(String name, String description, List<QuestReward<?>> rewards, List<QuestStep<?>> steps, long finishTimeInSeconds, boolean isPublic, boolean timerRunsOffline) {
		Optional<Integer> idOptional = QuestDatabase.getInstance().insertNewQuest(name, description, rewards, steps, finishTimeInSeconds, isPublic, timerRunsOffline);
		idOptional.ifPresent(id -> quests.add(new Quest(id, name, description, isPublic, rewards, steps, finishTimeInSeconds, timerRunsOffline)));
		return idOptional.isPresent();
	}

	/**
	 * Updates an old Quest.
	 *
	 * @param questId             int - the quest id
	 * @param name                String - name of a quest
	 * @param description         String - description of quest (; represents line breaks, §n represents color)
	 * @param rewards             List<QuestReward<?>>- a list of all rewards a player receives on completion
	 * @param steps               List<QuestStep<?>> - a list of all steps a player needs to complete
	 * @param finishTimeInSeconds long - the amount of seconds the player has to complete the quest
	 * @param isPublic            boolean - quest is publicly visible in the menu
	 * @param timerRunsOffline    boolean - timer runs offline
	 * @return boolean - creation was successful
	 */
	public boolean updateQuest(int questId, String name, String description, List<QuestReward<?>> rewards, List<QuestStep<?>> steps, long finishTimeInSeconds, boolean isPublic, boolean timerRunsOffline) {
		Optional<Quest> questOptional = getQuestById(questId);
		if (questOptional.isEmpty())
			throw new IllegalStateException("Could not find quest which should be modified id=" + questId);
		Quest oldQuest = questOptional.get();
		Quest quest = new Quest(questId, name, description, isPublic, rewards, steps, finishTimeInSeconds, timerRunsOffline);
		this.quests.remove(oldQuest);
		this.quests.add(quest);
		return QuestDatabase.getInstance().updateQuest(oldQuest, quest);

	}
}
