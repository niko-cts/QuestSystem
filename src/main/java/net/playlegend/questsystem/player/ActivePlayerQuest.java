package net.playlegend.questsystem.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.steps.QuestStep;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class ActivePlayerQuest {

	private final Quest activeQuest;
	private final Map<QuestStep, Integer> stepsWithAmounts; // queststepId, amount
	private final Instant timeLeft;

	public ActivePlayerQuest(Quest quest, Instant timeLeft) {
		this(quest, new HashMap<>(), timeLeft);
		quest.completionSteps().forEach(step -> this.stepsWithAmounts.put(step, 0));
	}

	public boolean isQuestFinished() {
		return getCompletedQuestSteps().size() >= activeQuest.completionSteps().size();
	}

	public List<QuestStep> getCompletedQuestSteps() {
		return stepsWithAmounts.entrySet().stream()
				.filter(entry -> entry.getKey().isStepComplete(entry.getValue()))
				.map(Map.Entry::getKey)
				.toList();
	}

	public long getSecondsLeft() {
		return (Timestamp.from(timeLeft).getTime() - System.currentTimeMillis()) / 1000;
	}
}
