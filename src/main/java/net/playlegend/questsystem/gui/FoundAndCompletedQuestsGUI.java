package net.playlegend.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import chatzis.nikolas.mc.nikoapi.util.Utils;
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

/**
 * This class contains methods to open Quest GUIs to the player.
 * Specially Found, Completed and Public quests menus.
 *
 * @author Niko
 */
public class FoundAndCompletedQuestsGUI {

	private FoundAndCompletedQuestsGUI() {
		throw new UnsupportedOperationException();
	}

	public static void openFoundQuestsGUI(QuestPlayer questPlayer) {
		Map<Quest, Timestamp> foundQuests = questPlayer.getFoundQuests();
		questPlayer.getFinishedQuests().keySet().forEach(foundQuests::remove);

		fillItemsAndOpen(questPlayer, foundQuests, TranslationKeys.QUESTS_GUI_FOUND_INFO, TranslationKeys.QUESTS_GUI_QUEST_DETAILS_COMPLETED_LORE,
				FoundAndCompletedQuestsGUI::openFoundQuestsGUI);
	}

	public static void openCompletedQuestGUI(QuestPlayer questPlayer) {
		Map<Quest, Timestamp> foundQuests = questPlayer.getFinishedQuests();
		fillItemsAndOpen(questPlayer, foundQuests, TranslationKeys.QUESTS_GUI_COMPLETED_INFO, TranslationKeys.QUESTS_GUI_QUEST_DETAILS_FOUND_LORE,
				FoundAndCompletedQuestsGUI::openCompletedQuestGUI);
	}

	private static void fillItemsAndOpen(QuestPlayer questPlayer, Map<Quest, Timestamp> questsWithTimestamp, String titleKey, String loreDetailsKey,
	                                     Consumer<QuestPlayer> fromSite) {
		Language language = questPlayer.getLanguage();
		CustomInventory menu = new CustomInventory(language.translateMessage(titleKey), Utils.getPerfectInventorySize(questsWithTimestamp.size() + 2));

		questsWithTimestamp.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> {
			menu.addItem(new ItemBuilder(entry.getKey().getQuestItem(language))
							.addLore(language.translateMessage(loreDetailsKey,
									"${time}",
									QuestTimingsUtil.formatDateTime(entry.getValue().toInstant())).split(";")).craft(),
					new ClickAction() {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							QuestSpecificGUI.openQuestGUI(questPlayer, entry.getKey(), false, fromSite);
						}
					});
		});

		GUIHelper.fillInventoryWithBackAndOpen(questPlayer, menu, null,
				(goBackPlayer, q) -> QuestOverviewGUI.openOverviewGUI(goBackPlayer));
	}

	public static void openPublicQuestsGUI(QuestPlayer questPlayer) {
		List<Quest> publicQuests = questPlayer.getUnfinishedPublicQuests();

		Language language = questPlayer.getLanguage();
		CustomInventory menu = new CustomInventory(Utils.getPerfectInventorySize(publicQuests.size() + 2));
		menu.addItem(new ItemBuilder(Material.PAPER)
				.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_PUBLIC_INFO_NAME))
				.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_PUBLIC_INFO_LORE)).craft());
		for (Quest publicQuest : publicQuests) {

			menu.addItem(new ItemBuilder(publicQuest.getQuestItem(language))
							.addLore(language.translateMessage(TranslationKeys.QUESTS_GUI_PUBLIC_LORE).split(";")).craft(),
					new ClickAction(Sound.BLOCK_CHEST_OPEN) {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							QuestSpecificGUI.openQuestGUI(questPlayer, publicQuest, false, FoundAndCompletedQuestsGUI::openPublicQuestsGUI);
						}
					});
		}

		GUIHelper.fillInventoryWithBackAndOpen(questPlayer, menu, QuestOverviewGUI::openOverviewGUI);
	}
}
