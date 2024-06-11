package net.playlegend.questsystem.quest.reward;

import net.playlegend.questsystem.QuestPlayer;
import org.bukkit.inventory.ItemStack;

public record ItemReward(ItemStack item) implements IQuestReward {

    /**
     * Will be called, when a player finished the quest.
     * The player should receive the reward.
     *
     * @param player {@link QuestPlayer} - the quest player who finished the quest.
     */
    @Override
    public void rewardPlayer(QuestPlayer player) {
        player.addItem(item.clone());
    }
}
