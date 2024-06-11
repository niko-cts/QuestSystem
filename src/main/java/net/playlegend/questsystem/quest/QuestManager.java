package net.playlegend.questsystem.quest;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.database.QuestDatabase;
import net.playlegend.questsystem.quest.reward.IQuestReward;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.quest.steps.QuestStepType;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This manager loads all quests, quest steps and quest rewards and stores it.
 *
 * @author Niko
 */
public class QuestManager {

	private List<Quest> quests;

	/**
	 * Will load every quest and adds it to the List.
	 * @see QuestDatabase
	 */
	public QuestManager() {
		this.quests = new ArrayList<>();
		Logger log = QuestSystem.getInstance().getLogger();

		QuestDatabase questDatabase = new QuestDatabase();
		try (ResultSet questResult = questDatabase.getAllQuests()) {
			while (questResult.next()) {
				List<IQuestReward> rewards = new ArrayList<>();
				List<QuestStep> steps = new ArrayList<>();
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

						Object stepObject = stepResult.getObject("step_object");
						int amount = stepResult.getInt("amount");

						try {
							steps.add(questStepType.getQuestStepInstance(stepId, stepObject, amount));
						} catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
						         IllegalAccessException e) {
							log.log(Level.SEVERE, "Could not create quest step for quest {0} at quest step '{0}'!" +
							                      "Probably the object of the step was wrong. Needed {0} but got {0}",
									new Object[]{questIDForLog, stepId, });
						}

					}
				} catch (SQLException e) {
					log.log(Level.WARNING, "There was an error reading quests steps for quest " + questIDForLog, e);
				}

				if (steps.isEmpty()) {
					log.log(Level.SEVERE, "There were no valid steps for {0}. Continuing without adding...", questIDForLog);
					continue;
				}


				quests.add(new Quest(questResult.getInt("id"), questResult.getString("name"), questResult.getString("description"), rewards,
						steps, stepOrder, questResult.getLong("finish_time")));
			}

			if (quests.isEmpty())
				log.info("No quests were added.");
			else
				log.info("Added Quests: " + quests.stream().map(Quest::name).collect(Collectors.joining(", ")));
		} catch (SQLException exception) {
			log.log(Level.SEVERE, "There was an error when trying to get all the quests", exception);
		}
	}
}
