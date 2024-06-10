package net.playlegend.questsystem.quest.reward;

import net.playlegend.questsystem.QuestPlayer;
import org.bukkit.Sound;

/**
 * {@inheritDoc}
 */
public class CoinsReward extends IQuestReward {


    public CoinsReward(int amount) {
        super(RewardType.COINS, amount);
    }


    /**
     * Will be called, when a player finished the quest.
     * The player should receive the reward.
     *
     * @param player {@link QuestPlayer} - the quest player who finished the quest.
     */
    @Override
    void rewardPlayer(QuestPlayer player) {
        player.setCoins(player.getCoins() + getAmount());
        player.playSound(Sound.ENTITY_PLAYER_LEVELUP);
    }
}
