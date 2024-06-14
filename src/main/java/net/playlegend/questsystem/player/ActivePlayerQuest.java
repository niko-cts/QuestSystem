package net.playlegend.questsystem.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.steps.QuestStep;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores every information about an ongoing quest of the player.
 *
 * @author Niko
 */
@AllArgsConstructor
@Getter
public class ActivePlayerQuest {

    private final Quest activeQuest;
    private final Map<QuestStep, Integer> stepsWithAmounts;
    private final Instant timeLeft;

    protected ActivePlayerQuest(Quest quest, Instant timeLeft) {
        this(quest, new HashMap<>(), timeLeft);
        quest.completionSteps().forEach(step -> this.stepsWithAmounts.put(step, 0));
    }

    protected boolean isQuestFinished() {
        return getCompletedQuestSteps().size() >= activeQuest.completionSteps().size();
    }

    /**
     * Adds one to the given quest step amount.
     * @param step QuestStep - the quest step to increase
     * @return boolean - quest step is now completed
     */
    protected boolean playerDidQuestStep(QuestStep step) {
        int newAmount = stepsWithAmounts.compute(step, (ignored, current) ->
                (current != null ? current : 0) + 1);
        return step.isStepComplete(newAmount);
    }



    /**
     * Returns a list of all steps which have the same order number as the top one.
     *
     * @return List<QuestStep> - all quest steps which needs to be completed next.
     */
    public List<QuestStep> getNextUncompletedSteps() {
        List<QuestStep> stepsToComplete = getStepsToComplete();
        if (stepsToComplete.isEmpty())
            return stepsToComplete;

        int order = stepsToComplete.get(0).getOrder();
        return stepsToComplete.stream().filter(s -> s.getOrder() == order).toList();
    }

    /**
     * Returns a list of QuestSteps which the player still needs to complete.
     *
     * @return List<QuestStep> - the list of quest steps that are not completed.
     */
    private List<QuestStep> getStepsToComplete() {
        return stepsWithAmounts.entrySet().stream()
                .filter(entry -> !entry.getKey().isStepComplete(entry.getValue()))
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(QuestStep::getOrder))
                .toList();
    }

    private List<QuestStep> getCompletedQuestSteps() {
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
