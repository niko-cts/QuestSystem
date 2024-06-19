package net.playlegend.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.QuestTimingsUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FoundAndCompletedQuestsGUI {

	private FoundAndCompletedQuestsGUI() {
		throw new UnsupportedOperationException();
	}

	public static void openFoundQuestsGUI(QuestPlayer questPlayer) {
		Map<Quest, Timestamp> foundQuests = questPlayer.getFoundQuests();
		fillItemsAndOpen(questPlayer, foundQuests, TranslationKeys.QUESTS_GUI_FOUND_INFO,
				FoundAndCompletedQuestsGUI::openFoundQuestsGUI);
	}

	public static void openCompletedQuestGUI(QuestPlayer questPlayer) {
		Map<Quest, Timestamp> foundQuests = questPlayer.getFinishedQuests();
		fillItemsAndOpen(questPlayer, foundQuests, TranslationKeys.QUESTS_GUI_COMPLETED_INFO,
				FoundAndCompletedQuestsGUI::openCompletedQuestGUI);
	}

	private static void fillItemsAndOpen(QuestPlayer questPlayer, Map<Quest, Timestamp> questsWithTimestamp, String titleKey,
	                                     Consumer<QuestPlayer> fromSite) {
		Language language = questPlayer.getLanguage();
		CustomInventory menu = new CustomInventory(questsWithTimestamp.size() + 2);
		menu.addItem(new ItemBuilder(Material.PAPER).setName(language.translateMessage(titleKey)).craft());

		questsWithTimestamp.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> {
			menu.addItem(new ItemBuilder(entry.getKey().getQuestItem(language))
							.addLore(language.translateMessage(TranslationKeys.QUESTS_GUI_QUEST_DETAILS_LORE,
									"${time}",
									QuestTimingsUtil.formatDateTime(entry.getValue().toInstant())).split(";")).craft(),
					new ClickAction() {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							QuestSpecificGUI.openQuestGUI(questPlayer, entry.getKey(), fromSite);
						}
					});
		});

		GUIHelper.fillInventoryWithBackAndOpen(questPlayer, menu, null,
				(goBackPlayer, q) -> QuestOverviewGUI.openOverviewGUI(goBackPlayer));
	}

	public static void openPublicQuestsGUI(QuestPlayer questPlayer) {
		List<Quest> publicQuests = QuestSystem.getInstance().getQuestManager().getPublicQuests();

		Language language = questPlayer.getLanguage();
		CustomInventory menu = new CustomInventory(publicQuests.size() + 2);
		menu.addItem(new ItemBuilder(Material.PAPER).setName(language.translateMessage(TranslationKeys.QUESTS_GUI_PUBLIC_INFO)).craft());
		for (Quest publicQuest : publicQuests) {
			if (questPlayer.getFinishedQuests().containsKey(publicQuest)) continue;

			menu.addItem(new ItemBuilder(publicQuest.getQuestItem(language))
							.addLore(language.translateMessage(TranslationKeys.QUESTS_GUI_PUBLIC_LORE).split(";")).craft(),
					new ClickAction(Sound.BLOCK_CHEST_OPEN) {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							QuestSpecificGUI.openQuestGUI(questPlayer, publicQuest, FoundAndCompletedQuestsGUI::openPublicQuestsGUI);
						}
					});
		}

		GUIHelper.fillInventoryWithBackAndOpen(questPlayer, menu, null, (goBackPlayer, o) -> QuestOverviewGUI.openOverviewGUI(questPlayer));
	}
}
