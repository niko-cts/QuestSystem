package chatzis.nikolas.questsystem.quest.reward;

import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.Language;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

/**
 * This class represents the abstract form of a quest reward.
 * Each quest can have multiple rewards which will be given as soon as the player finishes a quest.
 * <br>
 * This class is instantiated in {@link RewardType}, with the respective parameters each class has.
 *
 * @author Niko
 */
@Data
public abstract class QuestReward<T> {

	private final RewardType rewardType;
	private final T rewardObject;


	/**
	 * Will be called, when a player finished the quest.
	 * The player should receive the reward.
	 *
	 * @param player {@link QuestPlayer} - the quest player who finished the quest.
	 */
	public abstract void rewardPlayer(QuestPlayer player);

	/**
	 * Returns a one-liner that previews the reward. E.g. "10 level"
	 *
	 * @param language Language - the language to translate in
	 * @return String - the reward explanation
	 */
	public abstract String getRewardPreview(Language language);

	/**
	 * Returns an ItemStack that explains the reward.
	 * E.g., new ItemStack(EXPERIENCE).setLore("10 exp")
	 *
	 * @param language Language - the language to translate in
	 * @return ItemStack - the item explaining the reward
	 */
	public abstract ItemStack getRewardDisplayItem(Language language);

}
