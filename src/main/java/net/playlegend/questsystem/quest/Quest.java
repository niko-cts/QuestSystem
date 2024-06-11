package net.playlegend.questsystem.quest;

import net.playlegend.questsystem.quest.reward.IQuestReward;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.translation.Language;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record Quest(int id,
                    String name,
                    String description,
                    List<IQuestReward> rewards,
                    List<QuestStep> completionSteps,
                    QuestStepOrder order,
                    long finishTimeInSeconds
                    ) {

    public ItemStack getQuestItem(Language language) {
        return null; // TODO IMPL
    }

}