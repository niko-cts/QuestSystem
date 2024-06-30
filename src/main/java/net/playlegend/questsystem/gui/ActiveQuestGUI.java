package net.playlegend.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.QuestTimingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This class holds methods to open the active quest menu.
 *
 * @author Niko
 */
public class ActiveQuestGUI {

	private ActiveQuestGUI() {
		throw new UnsupportedOperationException("Should not be instantiated.");
	}


	public static void openActiveGUI(QuestPlayer questPlayer, ActivePlayerQuest activeQuest) {
		CustomInventory menu = new CustomInventory(27);
		Language language = questPlayer.getLanguage();

		menu.setItem(10, new ItemBuilder(Material.BOOK)
				.setName(activeQuest.getQuest().name())
				.setLore(activeQuest.getQuest().description().split(";"))
				.craft());

		menu.setItem(11, getActivePlayerQuestItem(activeQuest, language));

		menu.setItem(13, new ItemBuilder(GUIHelper.getActiveStepItem(language, activeQuest.getStepsWithAmounts()))
						.addLore(language.translateMessage(TranslationKeys.QUESTS_GUI_ACTIVE_STEPS_PREVIEW_LORE).split(";"))
						.craft(),
				new ClickAction(Sound.BLOCK_CHEST_OPEN) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						QuestStepsGUI.openActiveQuestSteps(questPlayer, activeQuest, ActiveQuestGUI::openActiveGUI);
					}
				});

		menu.setItem(14, new ItemBuilder(GUIHelper.getRewardItem(language, activeQuest.getQuest().rewards()))
						.addLore(language.translateMessage(TranslationKeys.QUESTS_GUI_REWARDS_PREVIEW_LORE).split(";")).craft(),
				new ClickAction(Sound.BLOCK_CHEST_OPEN) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						QuestRewardsGUI.openRewardsWithBack(questPlayer, activeQuest.getQuest(), activeQuest, ActiveQuestGUI::openActiveGUI);
					}
				});

		menu.setItem(16, new ItemBuilder(Material.BARRIER)
						.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_ACTIVE_CANCEL_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_ACTIVE_CANCEL_LORE).split(";")).craft(),
				new ClickAction(true) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						Bukkit.dispatchCommand(apiPlayer.getPlayer(), "quest cancel");
					}
				});


		long secondsLeft = activeQuest.getSecondsLeft();
		String timeLeftName = language.translateMessage(TranslationKeys.QUESTS_GUI_ACTIVE_TIME_LEFT_NAME, "${duration}",
				QuestTimingsUtil.convertSecondsToDHMS(language, secondsLeft));
		long completeSeconds = activeQuest.getQuest().finishTimeInSeconds();
		double timeFraction = (double) secondsLeft / completeSeconds;

		for (double i = 0, j = 0; i < 1 && j < 9; i += 0.1, j++) {
			boolean isInTime = timeFraction >= i;
			menu.setItem((int) j, new ItemBuilder(
					isInTime ? UsefulItems.BACKGROUND_GREEN : UsefulItems.BACKGROUND_YELLOW
			).setName(timeLeftName).craft());
		}

		GUIHelper.fillInventoryWithBackAndOpen(questPlayer, menu, QuestOverviewGUI::openOverviewGUI);
	}

	private static ItemStack getActivePlayerQuestItem(ActivePlayerQuest quest, Language language) {
		List<String> lore = new ArrayList<>(List.of(
				language.translateMessage(
						TranslationKeys.QUESTS_GUI_ACTIVE_OVERVIEW,
						List.of("${duration}", "${done}", "${todo}"),
						List.of(QuestTimingsUtil.convertSecondsToDHMS(language, quest.getSecondsLeft()),
								quest.getCompletedSteps().size(), quest.getUncompletedSteps().size())
				).split(";")));

		quest.getStepsWithAmounts().entrySet().stream().sorted(Comparator.comparingInt(e -> e.getKey().getOrder())).forEach(entry -> {
			if (entry.getKey().isStepComplete(entry.getValue())) {
				lore.addAll(Arrays.asList(language.translateMessage(TranslationKeys.QUESTS_GUI_ACTIVE_LORE_STEP_COMPLETED,
						List.of("${order}", "${task}"),
						List.of(entry.getKey().getOrder(), entry.getKey().getActiveTaskLine(language, entry.getValue()))).split(";")));
			} else {
				lore.addAll(Arrays.asList(language.translateMessage(TranslationKeys.QUESTS_GUI_ACTIVE_LORE_STEP_TODO,
						List.of("${order}", "${task}"),
						List.of(entry.getKey().getOrder(), entry.getKey().getActiveTaskLine(language, entry.getValue()))).split(";")));
			}
		});

		return new ItemBuilder(Material.IRON_SWORD).addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.setName(quest.getQuest().name())
				.setDamageByMultiply((float) quest.getCompletedSteps().size() / quest.getStepsWithAmounts().size())
				.setLore(lore).craft();
	}
}
