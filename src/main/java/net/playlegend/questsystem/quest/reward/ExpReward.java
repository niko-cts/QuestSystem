package net.playlegend.questsystem.quest.reward;

import net.playlegend.questsystem.QuestPlayer;
import org.bukkit.Sound;

public record ExpReward(int amount) implements IQuestReward {

	/**
	 * Will be called, when a player finished the quest.
	 * The player should receive the reward.
	 *
	 * @param player {@link QuestPlayer} - the quest player who finished the quest.
	 */
	@Override
	public void rewardPlayer(QuestPlayer player) {
		player.getPlayer().setLevel(player.getPlayer().getLevel() + amount);
		player.playSound(Sound.ENTITY_PLAYER_LEVELUP);
	}
}
