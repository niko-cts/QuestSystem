package chatzis.nikolas.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import chatzis.nikolas.questsystem.player.ActivePlayerQuest;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.quest.reward.QuestReward;
import chatzis.nikolas.questsystem.quest.steps.QuestStep;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import chatzis.nikolas.questsystem.util.QuestTimingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static chatzis.nikolas.questsystem.translation.TranslationKeys.QUESTS_GUI_REWARDS_PREVIEW_LORE;
import static chatzis.nikolas.questsystem.translation.TranslationKeys.QUESTS_GUI_STEPS_PREVIEW_LORE;

/**
 * This class holds methods to open the active quest menu.
 *
 * @author Niko
 */
public class ActiveQuestGUI {

	private ActiveQuestGUI() {
		throw new UnsupportedOperationException("Should not be instantiated.");
	}

	public static void openActiveGUI(QuestPlayer questPlayer) {
		questPlayer.getActivePlayerQuest().ifPresentOrElse(
				q -> openActiveGUI(questPlayer, q),
				() -> QuestOverviewGUI.openOverviewGUI(questPlayer));
	}

	private static void openActiveGUI(QuestPlayer questPlayer, ActivePlayerQuest activeQuest) {
		CustomInventory menu = new CustomInventory(27);
		Language language = questPlayer.getLanguage();

		menu.setItem(10, new ItemBuilder(Material.BOOK)
				.setName(activeQuest.getQuest().name())
				.setLore(activeQuest.getQuest().description().split(";"))
				.craft());

		menu.setItem(11, getActivePlayerQuestItem(activeQuest, language));

		List<QuestReward<?>> rewards = activeQuest.getQuest().rewards();
		ItemStack rewardItem = GUIHelper.getRewardItem(language, rewards);
		if (rewards.size() > 1)
			rewardItem = new ItemBuilder(rewardItem).addLore(language.translateMessage(QUESTS_GUI_REWARDS_PREVIEW_LORE).split(";")).craft();

		menu.setItem(13, rewardItem,
				new ClickAction(Sound.BLOCK_CHEST_OPEN) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						if (rewards.size() > 1)
							QuestRewardsGUI.openRewardsWithBack(questPlayer, activeQuest.getQuest(), activeQuest, (player, b) -> openActiveGUI(player));
					}
				});

		Map<QuestStep<?>, Integer> stepsWithAmounts = activeQuest.getStepsWithAmounts();
		ItemStack stepItem = GUIHelper.getActiveStepItem(language, stepsWithAmounts);
		if (stepsWithAmounts.size() > 1)
			stepItem = new ItemBuilder(stepItem).addLore(language.translateMessage(QUESTS_GUI_STEPS_PREVIEW_LORE).split(";")).craft();

		menu.setItem(14, stepItem,
				new ClickAction(Sound.BLOCK_CHEST_OPEN) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						if (stepsWithAmounts.size() > 1)
							QuestStepsGUI.openActiveQuestSteps(questPlayer, activeQuest, ActiveQuestGUI::openActiveGUI);
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
						List.of("${task}"),
						List.of(ChatColor.stripColor(entry.getKey().getActiveTaskLine(language, entry.getValue())))).split(";")));
			} else {
				lore.addAll(Arrays.asList(entry.getKey().getActiveTaskLine(language, entry.getValue()).split(";")));
			}
		});

		return new ItemBuilder(Material.IRON_SWORD).addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_ACTIVE_OVERVIEW_NAME))
				.setDamageByMultiply((float) quest.getCompletedSteps().size() / quest.getStepsWithAmounts().size())
				.setLore(lore).craft();
	}
}
