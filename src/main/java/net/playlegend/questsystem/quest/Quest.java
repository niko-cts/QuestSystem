package net.playlegend.questsystem.quest;

import net.playlegend.questsystem.quest.reward.IQuestReward;
import net.playlegend.questsystem.translation.Language;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record Quest(int id,
                    String name,
                    String description,
                    List<IQuestReward> rewards,
                    List<QuestSteps> completionSteps,
                    QuestStepOrder order
                    ) {

    public ItemStack getQuestItem(Language language) {
        throw new NotImplementedException();
    }

}
