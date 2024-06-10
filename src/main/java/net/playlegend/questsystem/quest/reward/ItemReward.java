package net.playlegend.questsystem.quest.reward;

import net.playlegend.questsystem.QuestPlayer;
import org.bukkit.inventory.ItemStack;

public class ItemReward extends IQuestReward {

    private final ItemStack item;

    public ItemReward(ItemStack item) {
        super(RewardType.ITEM, 1);
        this.item = item;
    }

    /**
     * Will be called, when a player finished the quest.
     * The player should receive the reward.
     *
     * @param player {@link QuestPlayer} - the quest player who finished the quest.
     */
    @Override
    void rewardPlayer(QuestPlayer player) {
        player.addItem(item.clone());
    }
}
