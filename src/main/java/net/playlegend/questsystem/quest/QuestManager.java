package net.playlegend.questsystem.quest;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.database.QuestDatabase;
import net.playlegend.questsystem.database.QuestPlayerDatabase;
import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.quest.reward.IQuestReward;
import net.playlegend.questsystem.quest.reward.RewardType;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.quest.steps.QuestStepType;
import net.playlegend.questsystem.util.QuestObjectConverterUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

	private final List<Quest> quests;
	private final QuestDatabase questDatabase;
	private final QuestPlayerDatabase playerDatabase;

	/**
	 * Will load every quest and adds it to the List.
	 * First load all quest information, then, according to quest step and reward.
	 * Won't add the quest if there is no quest step.
	 *
	 * @see QuestDatabase
	 */
	public QuestManager() {
		this.quests = new ArrayList<>();
		this.questDatabase = QuestDatabase.getInstance();
		this.playerDatabase = QuestPlayerDatabase.getInstance();
		Logger log = QuestSystem.getInstance().getLogger();

		try (ResultSet questResult = questDatabase.getAllQuests()) {
			while (questResult.next()) {
				List<IQuestReward> rewards = new ArrayList<>();
				List<QuestStep> steps = new LinkedList<>();
				int id = questResult.getInt("id");
				String name = questResult.getString("name");
				String questIDForLog = id + ":'" + name + "'";
				QuestStepOrder stepOrder;

				try {
					stepOrder = QuestStepOrder.valueOf(questResult.getString("step_order").toUpperCase());
				} catch (IllegalArgumentException exception) {
					log.log(Level.SEVERE, "Invalid step order of quest {0}! Tried to insert '{0}' but: [{0}] is accepted only.",
							new Object[]{questIDForLog, questResult.getString("type"),
									Arrays.stream(QuestStepOrder.values()).map(Enum::name).collect(Collectors.joining(", "))});
					continue;
				}

				try (ResultSet stepResult = questDatabase.getAllQuestSteps(id)) {
					while (stepResult.next()) {
						QuestStepType questStepType;
						int stepId = stepResult.getInt("id");
						try {
							questStepType = QuestStepType.valueOf(stepResult.getString("step_type").toUpperCase());
						} catch (IllegalArgumentException exception) {
							log.log(Level.SEVERE, "Invalid step type for quest {0} at quest step '{0}'! " +
							                      "Tried to insert {0}, but [{0}] is accepted only.",
									new Object[]{questIDForLog, stepId, stepResult.getString("step_type"),
											Arrays.stream(QuestStepType.values()).map(Enum::name).collect(Collectors.joining(", "))});
							continue;
						}

						int amount = stepResult.getInt("amount");
						try {
							Object stepObject = QuestObjectConverterUtil.convertDatabaseStringToQuestStepObject(stepResult.getString("step_object"), questStepType);
							steps.add(questStepType.getQuestStepInstance(stepId, stepObject, amount));
						} catch (Exception e) {
							log.log(Level.SEVERE, "Could not create quest step for quest {0} at quest step '{0}'! " +
							                      "The step_object could not be parsed. Probably the wrong string was inserted in the step_object." +
							                      "Needed a {0} and got: {0}" +
							                      "Exception is: {0}",
									new Object[]{questIDForLog, stepId, questStepType.getConstructorStepClass(), stepResult.getString("step_object"), e.getMessage()});
						}
					}
				} catch (SQLException e) {
					log.log(Level.SEVERE, "There was an error reading quests steps for quest " + questIDForLog, e);
				}

				try (ResultSet rewardResult = questDatabase.getAllQuestRewards(id)) {
					while (rewardResult.next()) {
						RewardType rewardType;

						try {
							rewardType = RewardType.valueOf(rewardResult.getString("type").toUpperCase());
						} catch (IllegalArgumentException exception) {
							log.log(Level.SEVERE, "Invalid reward type for quest {0}! Tried to insert {0}, but [{0}] is accepted only.",
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
							                      "Needed a {0} and got: {0}" +
							                      "Exception is: {0}",
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
						steps, stepOrder, questResult.getLong("finish_time"), questResult.getBoolean("timer_runs_offline")));
			}

			if (quests.isEmpty())
				log.warning("No quests were added.");
			else
				log.log(Level.INFO, "Added Quests ({0}): {0}",
						new Object[]{quests.size(), quests.stream().map(Quest::name).collect(Collectors.joining(", "))});
		} catch (SQLException exception) {
			log.log(Level.SEVERE, "There was an error while trying to get all the quests", exception);
		}
	}

	public List<Quest> getPublicQuests() {
		return getQuests().stream().filter(Quest::isPublic).toList();
	}


	public Map<Quest, Timestamp> loadCompletedQuestIdsByPlayer(UUID uuid) {
		Map<Quest, Timestamp> quests = new HashMap<>();
		try (ResultSet set = playerDatabase.getPlayerCompletedQuests(uuid)) {
			while (set.next()) {
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
			while (set.next()) {
				putInMapOrLog(quests, set.getInt("quest_id"), set.getTimestamp("created_at"));
			}
		} catch (SQLException e) {
			QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not load error when loading completed player quests.", e);
		}
		return quests;
	}

	public Optional<ActivePlayerQuest> loadActiveQuestIdByPlayer(UUID uuid, Timestamp lastlogin) {
		Logger log = QuestSystem.getInstance().getLogger();
		try (ResultSet questOverviewResult = playerDatabase.getPlayerActiveQuest(uuid)) {
			if (!questOverviewResult.next()) {
				return Optional.empty();
			}
			int questId = questOverviewResult.getInt("quest_id");
			Optional<Quest> questOptional = getQuestById(questId);
			if (questOptional.isEmpty()) {
				log.log(Level.WARNING, "There was an active quest with id {0} for player '{0}' loaded, but it could not be found.",
						new Object[]{questId, uuid});
				return Optional.empty();
			}
			Quest quest = questOptional.get();


			Map<QuestStep, Integer> steps = new HashMap<>();
			try (ResultSet stepResults = playerDatabase.getPlayerActiveQuestSteps(uuid)) {
				while (stepResults.next()) {
					int stepId = stepResults.getInt("step_id");
					Optional<QuestStep> questStep = getQuestStep(quest, stepId);
					if (questStep.isEmpty()) {
						log.log(Level.SEVERE, "Could not find QuestStep object of step id {0} for quest: {0}",
								new Object[]{stepId, questId});
						continue;
					}
					steps.put(questStep.get(), stepResults.getInt("amount"));
				}
			} catch (SQLException exception) {
				log.log(Level.SEVERE,
						"Could not load quest {0} of player {0}: {0}", new Object[]{questId, uuid, exception.getMessage()});
			}

			if (steps.isEmpty()) {
				log.log(Level.WARNING, "Will not add the active player quest {0}, because there were no valid quest steps.", questId);
				return Optional.empty();
			}

			Instant timeLeft = questOverviewResult.getTimestamp("time_left").toInstant();
			if (quest.timerRunsOffline())
				timeLeft = timeLeft.minus(System.currentTimeMillis() - lastlogin.getTime(), ChronoUnit.MILLIS);

			return Optional.of(new ActivePlayerQuest(quest, steps, timeLeft));
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Could not load active player quests", e);
		}
		return Optional.empty();
	}

	public Optional<QuestStep> getQuestStep(Quest quest, int stepId) {
		return quest.completionSteps().stream().filter(s -> s.getId() == stepId).findFirst();
	}

	public Optional<Quest> getQuestById(int id) {
		return getQuests().stream().filter(q -> q.id() == id).findFirst();
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
}
