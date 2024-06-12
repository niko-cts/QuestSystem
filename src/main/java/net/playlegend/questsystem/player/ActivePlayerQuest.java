package net.playlegend.questsystem.player;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.steps.QuestStep;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ActivePlayerQuest {

    private final Quest activeQuest;
    private final List<Integer> completedSteps;
    private final Map<Integer, Integer> activeQuestSteps; // queststepId, amount
    private int currentAmountAtStep;


    public void setStepCompleted(int stepId) {
        activeQuestSteps.remove(stepId);
        completedSteps.add(stepId);
    }

    public boolean isQuestCompleted() {
        return completedSteps.size() == activeQuest.completionSteps().size();
    }

}
