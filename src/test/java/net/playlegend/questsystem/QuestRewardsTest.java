package net.playlegend.questsystem;

import net.playlegend.questsystem.quest.reward.*;
import net.playlegend.questsystem.quest.steps.QuestStepType;
import net.playlegend.questsystem.util.ItemToBase64ConverterUtil;
import net.playlegend.questsystem.util.QuestObjectConverterUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class QuestRewardsTest {


    @Test
    public void convertObject_coinReward_createInstance() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CoinsReward coinsReward = new CoinsReward(42);

        Object coins = QuestObjectConverterUtil.convertDatabaseStringToQuestRewardObject(String.valueOf(coinsReward.amount()), RewardType.COINS);
        assertNotNull(coins);
        assertEquals(coinsReward.amount(), coins);

        assertDoesNotThrow(() -> RewardType.COINS.getQuestRewardInstance(coins), "Probably wrong parameters for reward");

        IQuestReward questRewardInstance = RewardType.COINS.getQuestRewardInstance(coins);
        assertEquals(coinsReward, questRewardInstance);
    }

    @Test
    public void convertObject_expReward_createInstance() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ExpReward expReward = new ExpReward(42);

        Object lvl = QuestObjectConverterUtil.convertDatabaseStringToQuestRewardObject(String.valueOf(expReward.amount()), RewardType.LVL);
        assertNotNull(lvl);
        assertEquals(expReward.amount(), lvl);

        assertDoesNotThrow(() -> RewardType.LVL.getQuestRewardInstance(lvl), "Probably wrong parameters for reward");

        IQuestReward questRewardInstance = RewardType.LVL.getQuestRewardInstance(lvl);
        assertEquals(expReward, questRewardInstance);
    }


    @Test
    public void convertObject_itemReward_createInstance() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ItemReward itemReward = new ItemReward(new ItemStack(Material.IRON_SWORD));

        String base64 = ItemToBase64ConverterUtil.toBase64(itemReward.item());

        Object item = QuestObjectConverterUtil.convertDatabaseStringToQuestRewardObject(base64, RewardType.ITEM);
        assertNotNull(item);
        assertEquals(itemReward.item(), item);

        IQuestReward questRewardInstance = RewardType.ITEM.getQuestRewardInstance(item);
        assertEquals(itemReward, questRewardInstance);
    }


    @Test
    public void instantiateWrongItemReward_checkIfNull() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ItemReward itemReward = new ItemReward(new ItemStack(Material.IRON_SWORD));

        IQuestReward questRewardInstance = RewardType.LVL.getQuestRewardInstance(new ItemStack(Material.IRON_SWORD));
        assertNotNull(questRewardInstance);
        assertNotEquals(itemReward, questRewardInstance);
    }


    @Test
    public void wrongParameters_instantiateWrongItemReward_checkIThrew() {
        assertThrows(IllegalStateException.class, () ->
                RewardType.LVL.getQuestRewardInstance("das wird kein level sein"));
    }
    @Test
    public void allRewards_implementedInConverter() {
        for (RewardType type : RewardType.values()) {
            try {
                QuestObjectConverterUtil.convertDatabaseStringToQuestRewardObject("", type);
            } catch (IllegalStateException exception) {
                assertEquals(0, 1, "There is a quest step which is not implemtend: " + type);
            } catch (Throwable ignored) {
                // will throw something, because "" is given
            }
        }
        assertEquals(3, RewardType.values().length, "There may be some quest reward which have not been tested yet");
    }
}
