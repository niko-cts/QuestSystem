package net.playlegend.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.QuestManager;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.QuestTimingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class QuestOverviewGUI {

	private QuestOverviewGUI() {
		throw new UnsupportedOperationException();
	}

	public static void openOverviewGUI(QuestPlayer questPlayer) {
		questPlayer.getActivePlayerQuest().ifPresentOrElse(
				activePlayerQuest -> openActiveGUI(questPlayer, activePlayerQuest),
				() -> openDefaultGUI(questPlayer)
		);
	}

	private static void openDefaultGUI(QuestPlayer questPlayer) {
		CustomInventory menu = new CustomInventory(27);
		Language language = questPlayer.getCurrentLanguage();
		menu.fill(UsefulItems.BACKGROUND_BLACK);

		QuestManager questManager = QuestSystem.getInstance().getQuestManager();

		menu.setItem(10, new ItemBuilder(Material.GUNPOWDER)
				.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_NAME))
				.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_LORE).split(";")).craft());

		menu.setItem(12, new ItemBuilder(Material.CLOCK)
						.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_PUBLIC_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_PUBLIC_LORE).split(";"))
						.setAmount(Math.min(64, Math.max(1, questManager.getPublicQuests().size())))
						.craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						FoundAndCompletedQuestsGUI.openFoundQuestsGUI(questPlayer);
					}
				});
		menu.setItem(13, new ItemBuilder(Material.CHEST)
						.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_FOUND_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_FOUND_LORE).split(";"))
						.setAmount(Math.min(64, Math.max(1, questPlayer.getFoundQuests().size())))
						.craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						FoundAndCompletedQuestsGUI.openFoundQuestsGUI(questPlayer);
					}
				});
		menu.setItem(16, new ItemBuilder(Material.GREEN_DYE)
						.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_COMPLETED_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_COMPLETED_LORE).split(";"))
						.setAmount(Math.min(64, Math.max(1, questPlayer.getFinishedQuests().size())))
						.craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						FoundAndCompletedQuestsGUI.openCompletedQuestGUI(questPlayer);
					}
				}
		);

		questPlayer.openCustomInv(menu);
	}

	private static void openActiveGUI(QuestPlayer questPlayer, ActivePlayerQuest activePlayerQuest) {
		CustomInventory menu = new CustomInventory(27);
		Language language = questPlayer.getCurrentLanguage();

		menu.fill(UsefulItems.BACKGROUND_GREEN);

		menu.setItem(10, new ItemBuilder(Material.BOOK)
						.setName(activePlayerQuest.getActiveQuest().name())
						.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_DESCRIPTION_LORE))
						.craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						apiPlayer.openBook(new ItemBuilder(Material.WRITTEN_BOOK).addPage(
								language.translateMessage(activePlayerQuest.getActiveQuest().description()).replace("/n", "\n").split(";")
						).craft());
					}
				});

		menu.setItem(12, getActivePlayerQuestItem(activePlayerQuest, language));

		activePlayerQuest.getNextUncompletedStep().ifPresent(nextStep ->
				menu.setItem(13, new ItemBuilder(Material.PAPER)
						.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_STEP_DETAILS_NAME, "${task}", nextStep.getTaskName(language)))
						.setLore(nextStep.getTaskDescription(language)).craft()));

		menu.setItem(14, activePlayerQuest.getActiveQuest().getRewardItem(language));

		menu.setItem(16, new ItemBuilder(Material.BARRIER)
						.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_CANCEL_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_CANCEL_LORE).split(";")).craft(),
				new ClickAction(true) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						Bukkit.dispatchCommand(apiPlayer.getPlayer(), "quest cancel");
					}
				});

		questPlayer.openCustomInv(menu);
	}


	private static ItemStack getActivePlayerQuestItem(ActivePlayerQuest quest, Language language) {
		List<String> lore = new ArrayList<>(Arrays.asList(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_LORE,
				"${duration}", QuestTimingsUtil.convertSecondsToDHMS(language, quest.getSecondsLeft())).split(";")));

		quest.getStepsWithAmounts().entrySet().stream().sorted(Comparator.comparingInt(e -> e.getKey().getOrder())).forEach(entry -> {
			if (entry.getKey().isStepComplete(entry.getValue())) {
				lore.addAll(Arrays.asList(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_LORE_STEP_COMPLETED,
						List.of("${order}", "${task}", "${amount}"),
						List.of(entry.getKey().getOrder(), entry.getKey().getTaskName(language), entry.getValue())).split(";")));
			} else {
				lore.addAll(Arrays.asList(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_LORE_STEP_TODO,
						List.of("${order}", "${task}", "${amount}"),
						List.of(entry.getKey().getOrder(), entry.getKey().getTaskName(language), entry.getValue())).split(";")));
			}
		});


		return new ItemBuilder(Material.ENCHANTED_BOOK).addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.setName(quest.getActiveQuest().name())
				.setLore(lore).craft();
	}
}
