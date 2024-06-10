package net.playlegend.questsystem.quest.reward;

import net.playlegend.questsystem.QuestPlayer;

/**
 * This class represents the abstract form of a quest reward.
 * Each quest can have multiple rewards which will be given as soon as the player finishes a quest.
 * <br>
 * This class is instantiated in {@link RewardType}, with the respective parameters each class has.
 * @author Niko
 */
public interface IQuestReward {

    /**
     * Will be called, when a player finished the quest.
     * The player should receive the reward.
     * @param player {@link QuestPlayer} - the quest player who finished the quest.
     */
    void rewardPlayer(QuestPlayer player);
}
