package net.playlegend.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import lombok.NonNull;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.reward.QuestReward;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;

public class GUIHelper {

	private GUIHelper() {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method fills the given menu with a background, inserts a "back" button and opens the menu.
	 * The back button triggers a BiConsumer, with the given player and backQuest.
	 * The backQuest is nullable.
	 *
	 * @param questPlayer   QuestPlayer - player to open the menu
	 * @param menu          CustomInventory - menu to fill and display
	 * @param backParameter T - A parameter to call when player goes back
	 * @param goBack        BiConsumer<QuestPlayer, T> - player clicked the back button and this consumer is called
	 * @param <T>           - This element will be in the consumer
	 */
	public static <T> void fillInventoryWithBackAndOpen(@NonNull QuestPlayer questPlayer, @NonNull CustomInventory menu,
	                                                    T backParameter, BiConsumer<QuestPlayer, T> goBack) {
		menu.setItem(menu.getInventory().getSize() - 1, UsefulItems.ARROW_LEFT()
						.setName(questPlayer.getLanguage().translateMessage(TranslationKeys.QUESTS_GUI_BACK)).craft(),
				new ClickAction(Sound.BLOCK_CHEST_OPEN) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						goBack.accept(questPlayer, backParameter);
					}
				});

		menu.fill(UsefulItems.BACKGROUND_BLACK);

		questPlayer.openCustomInv(menu);
	}

	public static ItemStack getRewardItem(Language language, @NonNull List<QuestReward<?>> rewards) {
		return new ItemBuilder(Material.GOLD_INGOT)
				.setName(language.translateMessage(rewards.isEmpty() ? TranslationKeys.QUESTS_GUI_QUEST_REWARD_NAME_NONE : TranslationKeys.QUESTS_GUI_QUEST_REWARD_NAME_HAS))
				.setLore(rewards.stream().map(r -> ChatColor.GRAY + "- " + r.getRewardPreview(language)).toList())
				.craft();
	}

}
