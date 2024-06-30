package net.playlegend.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import lombok.NonNull;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.reward.QuestReward;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

	/**
	 * This method fills the given menu with a background, inserts a "back" button and opens the menu.
	 * The back button triggers a Consumer with the given player.
	 *
	 * @param questPlayer QuestPlayer - player to open the menu
	 * @param menu        CustomInventory - menu to fill and display
	 * @param goBack      Consumer<QuestPlayer> - player clicked the back button and this consumer is called
	 */
	public static void fillInventoryWithBackAndOpen(@NonNull QuestPlayer questPlayer, @NonNull CustomInventory menu,
	                                                Consumer<QuestPlayer> goBack) {
		menu.setItem(menu.getInventory().getSize() - 1, UsefulItems.ARROW_LEFT()
						.setName(questPlayer.getLanguage().translateMessage(TranslationKeys.QUESTS_GUI_BACK)).craft(),
				new ClickAction(Sound.BLOCK_CHEST_OPEN) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						goBack.accept(questPlayer);
					}
				});

		menu.fill(UsefulItems.BACKGROUND_BLACK);

		questPlayer.openCustomInv(menu);
	}

	/**
	 * Returns a preview of the reward item.
	 *
	 * @param language Language - language to display
	 * @param rewards  List<QuestReward<?>> - all rewards to show in the lore
	 * @return ItemStack - the previewed reward item.
	 */
	public static ItemStack getRewardItem(@NonNull Language language, @NonNull List<QuestReward<?>> rewards) {
		return rewards.size() == 1 ?
				rewards.get(0).getRewardDisplayItem(language) :
				new ItemBuilder(Material.GOLD_INGOT)
						.setName(language.translateMessage(rewards.isEmpty() ? TranslationKeys.QUESTS_GUI_QUEST_REWARD_NAME_NONE : TranslationKeys.QUESTS_GUI_QUEST_REWARD_NAME_HAS))
						.setLore(rewards.stream().map(r -> ChatColor.GRAY + "- " + r.getRewardPreview(language)).toList())
						.craft();
	}

	public static ItemStack getStepItem(@NonNull Language language, @NonNull List<QuestStep<?>> steps) {
		return steps.size() == 1 ?
				steps.get(0).getTaskItem(language) :
				new ItemBuilder(Material.IRON_DOOR)
						.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_QUEST_STEPS_NAME))
						.setLore(steps.stream().sorted(Comparator.comparingInt(QuestStep::getOrder)).map(s -> s.getTaskLine(language)).toList())
						.craft();
	}

	public static ItemStack getActiveStepItem(@NonNull Language language, @NonNull Map<QuestStep<?>, Integer> steps) {
		if (steps.size() == 1) {
			QuestStep<?> questStep = new ArrayList<>(steps.keySet()).get(0);
			Integer amount = steps.get(questStep);
			return questStep.getActiveTask(language, amount);
		}
		return new ItemBuilder(Material.IRON_DOOR)
				.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_QUEST_STEPS_NAME))
				.setLore(steps.entrySet().stream().sorted(Comparator.comparingInt(e -> e.getKey().getOrder())).map(e -> e.getKey().getActiveTaskLine(language, e.getValue()))
						.toList())
				.setAmount(Math.min(64, Math.max(1, steps.size())))
				.craft();
	}
}
