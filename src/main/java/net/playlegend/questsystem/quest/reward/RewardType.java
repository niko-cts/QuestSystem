package net.playlegend.questsystem.quest.reward;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

/**
 * Lists every type of reward with the representative classes.
 *
 * @author Niko
 * @see QuestReward
 */
@Getter
public enum RewardType {

    COINS(CoinsReward.class, Integer.class),
    LVL(LevelReward.class, Integer.class),
    ITEM(ItemReward.class, ItemStack.class);

    private final Class<? extends QuestReward<?>> rewardClass;
    private final Class<?> constructorParameter;

    RewardType(Class<? extends QuestReward<?>> rewardClass, Class<?> constructorParameters) {
        this.rewardClass = rewardClass;
        this.constructorParameter = constructorParameters;
    }

    /**
     * Instantiates a quest reward based on given parameter.
     *
     * @param parameter Object - the parameter the reward class needs. This should match the {@link RewardType#constructorParameter}
     * @return {@link QuestReward} - A instance of {@link RewardType#rewardClass}
     * @throws IllegalStateException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException - Could be thrown if the parameters do not match the constructor of the reward class
     */
    public QuestReward<?> getQuestRewardInstance(Object parameter) throws IllegalStateException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!constructorParameter.isInstance(parameter)) {
            throw new IllegalStateException("Given parameter does not match the reward constructor");
        }
        return rewardClass.getConstructor(constructorParameter).newInstance(parameter);
    }

}
