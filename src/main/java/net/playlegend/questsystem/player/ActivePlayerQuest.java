package net.playlegend.questsystem.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.steps.QuestStep;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * This class stores every information about an ongoing quest of the player.
 *
 * @author Niko
 */
@AllArgsConstructor
@Getter
public class ActivePlayerQuest {

	private final Quest quest;
	private final Map<QuestStep<?>, Integer> stepsWithAmounts;
	private final Instant timeLeft;

	protected ActivePlayerQuest(Quest quest, Instant timeLeft) {
		this(quest, new HashMap<>(), timeLeft);
		quest.completionSteps().forEach(step -> this.stepsWithAmounts.put(step, 0));
	}

	public boolean isQuestFinished() {
		return getCompletedSteps().size() >= quest.completionSteps().size();
	}

	/**
	 * Adds one to the given quest step amount.
	 *
	 * @param step        QuestStep - the quest step to increase
	 * @param amountToAdd int - the amount the player did at once
	 * @return boolean - quest step is now completed
	 */
	protected boolean playerDidQuestStep(QuestStep<?> step, int amountToAdd) {
		int newAmount = stepsWithAmounts.compute(step, (ignored, current) ->
				(current != null ? current : 0) + amountToAdd);
		return step.isStepComplete(newAmount);
	}

	/**
	 * Returns next quest step to take
	 *
	 * @return Optional<QuestStep> - quest step which needs to be completed next.
	 */
	public Optional<Map.Entry<QuestStep<?>, Integer>> getNextUncompletedStep() {
		return stepsWithAmounts.entrySet().stream()
				.filter(entry -> !entry.getKey().isStepComplete(entry.getValue()))
				.min(Comparator.comparingInt(entry -> entry.getKey().getOrder()));
	}

	/**
	 * Returns a list of all steps which have the same order number as the top one.
	 *
	 * @return List<QuestStep> - all quest steps which needs to be completed next.
	 */
	public List<? extends QuestStep<?>> getNextUncompletedSteps() {
		List<? extends QuestStep<?>> stepsToComplete = getUncompletedSteps();
		if (stepsToComplete.isEmpty())
			return stepsToComplete;

		int order = stepsToComplete.get(0).getOrder();
		return stepsToComplete.stream().filter(s -> s.getOrder() == order).toList();
	}

	public List<? extends QuestStep<?>> getPreviousSteps() {
		List<? extends QuestStep<?>> stepsToComplete = getCompletedSteps();
		Optional<Integer> maxOrder = stepsToComplete.stream().map(QuestStep::getOrder).max(Comparator.comparingInt(i -> i));
		if (maxOrder.isEmpty()) {
			return List.of();
		}

		Integer order = maxOrder.get();
		return stepsToComplete.stream().filter(s -> s.getOrder() == order).toList();
	}

	/**
	 * Returns a list of QuestSteps which the player still needs to complete.
	 *
	 * @return List<QuestStep> - the list of quest steps that are not completed.
	 */
	public List<? extends QuestStep<?>> getUncompletedSteps() {
		return stepsWithAmounts.entrySet().stream()
				.filter(entry -> !entry.getKey().isStepComplete(entry.getValue()))
				.map(Map.Entry::getKey)
				.sorted(Comparator.comparingInt(QuestStep::getOrder))
				.toList();
	}

	public List<? extends QuestStep<?>> getCompletedSteps() {
		return stepsWithAmounts.entrySet().stream()
				.filter(entry -> entry.getKey().isStepComplete(entry.getValue()))
				.map(Map.Entry::getKey)
				.sorted(Comparator.comparingInt(QuestStep::getOrder))
				.toList();
	}

	public Map<QuestStep<?>, Integer> getStepsWithAmounts() {
		return new HashMap<>(stepsWithAmounts);
	}

	/**
	 * Returns the seconds the player has left to complete the quest.
	 *
	 * @return long - seconds to finish quest
	 */
	public long getSecondsLeft() {
		return Duration.between(Instant.now(), timeLeft).getSeconds();
	}

	public int getStepAmount(QuestStep<?> uncompletedStep) {
		return stepsWithAmounts.getOrDefault(uncompletedStep, -1);
	}

}
