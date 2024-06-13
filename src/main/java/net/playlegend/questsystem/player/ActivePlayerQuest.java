package net.playlegend.questsystem.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.steps.QuestStep;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
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


    /**
     * Returns a list of QuestSteps which the player still needs to complete.
     *
     * @return List<QuestStep> - the list of quest steps that are not completed.
     */
    public List<QuestStep> getStepsToComplete() {
        return stepsWithAmounts.entrySet().stream()
                .filter(entry -> !entry.getKey().isStepComplete(entry.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }


    public List<QuestStep> getCompletedQuestSteps() {
        return stepsWithAmounts.entrySet().stream()
                .filter(entry -> entry.getKey().isStepComplete(entry.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Returns the seconds the player has left to complete the quest.
     *
     * @return long - seconds to finish quest
     */
    public long getSecondsLeft() {
        return Duration.between(Instant.now(), timeLeft).getSeconds();
    }
}
