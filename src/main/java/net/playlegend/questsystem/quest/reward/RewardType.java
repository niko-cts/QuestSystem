package net.playlegend.questsystem.quest.reward;

import lombok.Getter;
import net.playlegend.questsystem.QuestSystem;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Lists every type of reward with the representative classes.
 *
 * @author Niko
 * @see IQuestReward
 */
public enum RewardType {

    COINS(CoinsReward.class, Integer.class),
    LVL(ExpReward.class, Integer.class),
    ITEM(ItemReward.class, ItemStack.class);

    private final Class<? extends IQuestReward> rewardClass;
    @Getter
    private final Class<?> constructorParameter;

    RewardType(Class<? extends IQuestReward> rewardClass, Class<?> constructorParameters) {
        this.rewardClass = rewardClass;
        this.constructorParameter = constructorParameters;
    }

    /**
     * Instantiates a quest reward based on given parameter.
     *
     * @param parameter Object - the parameter the reward class needs. This should match the {@link RewardType#constructorParameter}
     * @return {@link IQuestReward} - A instance of {@link RewardType#rewardClass}
     * @throws IllegalStateException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException - Could be thrown if the parameters do not match the constructor of the reward class
     */
    public IQuestReward getQuestRewardInstance(Object parameter) throws IllegalStateException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!constructorParameter.isInstance(parameter)) {
            throw new IllegalStateException("Given parameter does not match the reward constructor");
        }
        return rewardClass.getConstructor(constructorParameter).newInstance(parameter);
    }

    /**
     * Gets the reward type based on the given class.
     * Checks the {@link RewardType#rewardClass} if is instance of.
     *
     * @param rewardClass Class<? extends IQuestReward>
     * @return RewardType - the instance of the reward
     */
    public static RewardType getFromClass(Class<? extends IQuestReward> rewardClass) {
        return Arrays.stream(values()).filter(rewardClass::isInstance).findFirst().orElseGet(() -> {
            QuestSystem.getInstance().getLogger().warning("Reward type of class is not implemented: " + rewardClass);
            return null;
        });
    }

}
