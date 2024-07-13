package net.playlegend.questsystem.converter;

import be.seeseemelk.mockbukkit.MockBukkit;
import chatzis.nikolas.questsystem.quest.reward.*;
import chatzis.nikolas.questsystem.util.ItemToBase64ConverterUtil;
import chatzis.nikolas.questsystem.util.QuestObjectConverterUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class QuestRewardsTest {

    @BeforeEach
    public void setUp() {
        MockBukkit.mock();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }
    @Test
    public void convertObject_coinReward_createInstance() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CoinsReward coinsReward = new CoinsReward(42);

        Object coins = QuestObjectConverterUtil.convertDatabaseStringToQuestRewardObject(String.valueOf(coinsReward.getRewardObject()), RewardType.COINS);
        assertNotNull(coins);
        assertEquals(coinsReward.getRewardObject(), coins);

        assertDoesNotThrow(() -> RewardType.COINS.getQuestRewardInstance(coins), "Probably wrong parameters for reward");

        QuestReward<?> questRewardInstance = RewardType.COINS.getQuestRewardInstance(coins);
        assertEquals(coinsReward, questRewardInstance);
    }

    @Test
    public void convertObject_expReward_createInstance() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        LevelReward levelReward = new LevelReward(42);

        Object lvl = QuestObjectConverterUtil.convertDatabaseStringToQuestRewardObject(String.valueOf(levelReward.getRewardObject()), RewardType.LVL);
        assertNotNull(lvl);
        assertEquals(levelReward.getRewardObject(), lvl);

        assertDoesNotThrow(() -> RewardType.LVL.getQuestRewardInstance(lvl), "Probably wrong parameters for reward");

        QuestReward<?> questRewardInstance = RewardType.LVL.getQuestRewardInstance(lvl);
        assertEquals(levelReward, questRewardInstance);
    }


    @Test
    public void convertObject_itemReward_createInstance() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ItemReward itemReward = new ItemReward(new ItemStack(Material.IRON_SWORD));

        String base64 = ItemToBase64ConverterUtil.toBase64(itemReward.getRewardObject());

        Object item = QuestObjectConverterUtil.convertDatabaseStringToQuestRewardObject(base64, RewardType.ITEM);
        assertNotNull(item);
        assertEquals(itemReward.getRewardObject(), item);

        QuestReward<?> questRewardInstance = RewardType.ITEM.getQuestRewardInstance(item);
        assertEquals(itemReward, questRewardInstance);
    }


    @Test
    public void wrongParameters_instantiateWrongItemReward_checkIThrew() {
        assertThrows(IllegalStateException.class, () ->
                RewardType.ITEM.getQuestRewardInstance("das wird kein level sein"));
    }
    @Test
    public void allRewards_implementedInConverter() {
        for (RewardType type : RewardType.values()) {
            try {
                QuestObjectConverterUtil.convertDatabaseStringToQuestRewardObject("", type);
            } catch (IllegalStateException exception) {
                assertEquals(0, 1, "There is a quest step which is not implement: " + type);
            } catch (Throwable ignored) {
                // will throw something, because "" is given
            }
        }
        assertEquals(3, RewardType.values().length, "There may be some quest reward which have not been tested yet");
    }
}
