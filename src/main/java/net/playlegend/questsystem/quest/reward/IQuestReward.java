package net.playlegend.questsystem.quest.reward;

import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import org.bukkit.inventory.ItemStack;

/**
 * This class represents the abstract form of a quest reward.
 * Each quest can have multiple rewards which will be given as soon as the player finishes a quest.
 * <br>
 * This class is instantiated in {@link RewardType}, with the respective parameters each class has.
 *
 * @author Niko
 */
public interface IQuestReward {

	/**
	 * Will be called, when a player finished the quest.
	 * The player should receive the reward.
	 *
	 * @param player {@link QuestPlayer} - the quest player who finished the quest.
	 */
	void rewardPlayer(QuestPlayer player);

	/**
	 * Returns a one-liner that previews the reward. E.g. "10 level"
	 *
	 * @param language Language - the language to translate in
	 * @return String - the reward explanation
	 */
	String getRewardPreview(Language language);

	/**
	 * Returns an ItemStack that explains the reward.
	 * E.g., new ItemStack(EXPERIENCE).setLore("10 exp")
	 *
	 * @param language Language - the language to translate in
	 * @return ItemStack - the item explaining the reward
	 */
	ItemStack getRewardDisplayItem(Language language);

}
