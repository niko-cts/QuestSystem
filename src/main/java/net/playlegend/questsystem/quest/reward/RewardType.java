package net.playlegend.questsystem.quest.reward;

import net.playlegend.questsystem.QuestSystem;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

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
    private final Class<?>[] constructorParameters;

    RewardType(Class<? extends IQuestReward> rewardClass, Class<?>... constructorParameters) {
        this.rewardClass = rewardClass;
        this.constructorParameters = constructorParameters;
    }

    /**
     * Instantiates a quest reward based on given parameters.
     *
     * @param parameters Object[] - the parameters the reward class needs. This should match the {@link RewardType#constructorParameters}
     * @return {@link IQuestReward} - A instance of {@link RewardType#rewardClass}
     */
    public IQuestReward getQuestRewardInstance(Object... parameters) {
        if (parameters.length != constructorParameters.length) {
            throw new IllegalStateException("Cannot create a QuestReward instance: The number of parameters does not match the number of the constructor");
        }

        try {
            return rewardClass.getConstructor(constructorParameters).newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            QuestSystem.getInstance().getLogger().warning("Could not instantiate the QuestRewardClass. Wrong parameters? :" + e.getMessage());
            return null;
        }
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
