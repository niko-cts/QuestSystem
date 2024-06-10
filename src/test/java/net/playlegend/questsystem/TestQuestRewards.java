package net.playlegend.questsystem;

import net.playlegend.questsystem.quest.reward.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestQuestRewards {

    @Test
    public void instantiateCoinReward_checkIfSame() {
        CoinsReward coinsReward = new CoinsReward(42);

        IQuestReward questRewardInstance = RewardType.COINS.getQuestRewardInstance(42);
        assertNotNull(questRewardInstance);
        assertEquals(coinsReward, questRewardInstance);
    }

    @Test
    public void instantiateExpReward_checkIfSame() {
        ExpReward expReward = new ExpReward(1000);

        IQuestReward questRewardInstance = RewardType.LVL.getQuestRewardInstance(1000);
        assertNotNull(questRewardInstance);
        assertEquals(expReward, questRewardInstance);
    }


    @Test
    public void instantiateItemReward_checkIfSame() {
        ItemReward itemReward = new ItemReward(new ItemStack(Material.IRON_SWORD));

        IQuestReward questRewardInstance = RewardType.ITEM.getQuestRewardInstance(new ItemStack(Material.IRON_SWORD));
        assertNotNull(questRewardInstance);
        assertEquals(itemReward, questRewardInstance);
    }


    @Test
    public void instantiateWrongItemReward_checkIfNull() {
        ItemReward itemReward = new ItemReward(new ItemStack(Material.IRON_SWORD));

        IQuestReward questRewardInstance = RewardType.LVL.getQuestRewardInstance(new ItemStack(Material.IRON_SWORD));
        assertNotNull(questRewardInstance);
        assertNotEquals(itemReward, questRewardInstance);
    }


    @Test
    public void wrongParameters_instantiateWrongItemReward_checkIThrew() {
        assertThrows(IllegalStateException.class, () ->
                RewardType.LVL.getQuestRewardInstance(Material.IRON_SWORD, 1));
    }
}
