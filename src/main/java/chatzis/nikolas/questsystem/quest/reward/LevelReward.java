package chatzis.nikolas.questsystem.quest.reward;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class LevelReward extends QuestReward<Integer> {

	public LevelReward(Integer level) {
		super(RewardType.LVL, level);
	}

	/**
	 * Will be called, when a player finished the quest.
	 * The player should receive the reward.
	 *
	 * @param player {@link QuestPlayer} - the quest player who finished the quest.
	 */
	@Override
	public void rewardPlayer(QuestPlayer player) {
		player.getPlayer().setLevel(player.getPlayer().getLevel() + getRewardObject());
		player.playSound(Sound.ENTITY_PLAYER_LEVELUP);
		player.sendMessage(TranslationKeys.QUESTS_REWARD_LVL_MESSAGE, "${amount}", getRewardObject());
	}

	/**
	 * Returns a one-liner that previews the reward. E.g. "10 level"
	 *
	 * @param language Language - the language to translate in
	 * @return String - the reward explanation
	 */
	@Override
	public String getRewardPreview(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_REWARD_LVL_PREVIEW, "${amount}", getRewardObject());
	}

	/**
	 * Returns an ItemStack that explains the reward.
	 * E.g., new ItemStack(EXPERIENCE).setLore("10 exp")
	 *
	 * @param language Language - the language to translate in
	 * @return ItemStack - the item explaining the reward
	 */
	@Override
	public ItemStack getRewardDisplayItem(Language language) {
		return new ItemBuilder(Material.EXPERIENCE_BOTTLE)
				.setName(language.translateMessage(TranslationKeys.QUESTS_REWARD_LVL_NAME, "${amount}", getRewardObject()))
				.setLore(language.translateMessage(TranslationKeys.QUESTS_REWARD_LVL_LORE, "${amount}", getRewardObject()).split(";")).craft();
	}
}
