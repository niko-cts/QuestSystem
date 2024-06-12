package net.playlegend.questsystem.quest.reward;

import net.playlegend.questsystem.player.QuestPlayer;
import org.bukkit.Sound;


/**
 * CoinsReward will reward the player with coins.
 *
 * @author Niko
 */
public record CoinsReward(Integer amount) implements IQuestReward {

    /**
     * Will be called, when a player finished the quest.
     * The player should receive the reward.
     *
     * @param player {@link QuestPlayer} - the quest player who finished the quest.
     */
    @Override
    public void rewardPlayer(QuestPlayer player) {
        player.setCoins(player.getCoins() + amount());
        player.playSound(Sound.ENTITY_PLAYER_LEVELUP);
    }
}
