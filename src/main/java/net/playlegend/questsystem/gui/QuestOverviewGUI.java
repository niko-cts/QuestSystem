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
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class QuestOverviewGUI {

	private QuestOverviewGUI() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Opens an quest overview for the player.<br>
	 * All listed elements are: <br>
	 * - active / non active quest<br>
	 * - uncompleted public quests<br>
	 * - found quests<br>
	 * - completed quest<br>
	 * <p>
	 * Each element can be clicked and will redirect to another menu.
	 * @param questPlayer QuestPlayer - the player to open the menu
	 */
	public static void openOverviewGUI(QuestPlayer questPlayer) {
		CustomInventory menu = new CustomInventory(27);
		Language language = questPlayer.getLanguage();
		menu.fill(UsefulItems.BACKGROUND_BLACK);

		Optional<ActivePlayerQuest> activeQuestOptional = questPlayer.getActivePlayerQuest();
		if (activeQuestOptional.isEmpty()) {
			menu.setItem(10, new ItemBuilder(Material.GUNPOWDER)
					.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_NAME))
					.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_LORE).split(";")).craft());
		} else {
			ActivePlayerQuest activeQuest = activeQuestOptional.get();
			menu.setItem(10, new ItemBuilder(Material.REDSTONE)
							.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_NAME, "${name}", activeQuest.getQuest().name()))
							.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_ACTIVE_LORE, "${name}", activeQuest.getQuest().name()).split(";")).craft(),
					new ClickAction(Sound.BLOCK_CHEST_OPEN) {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							ActiveQuestGUI.openActiveGUI(questPlayer);
						}
					});
		}

		menu.setItem(12, new ItemBuilder(Material.WRITTEN_BOOK)
						.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_PUBLIC_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_PUBLIC_LORE).split(";"))
						.setAmount(Math.min(64, Math.max(1, questPlayer.getUnfinishedPublicQuests().size())))
						.craft(),
				new ClickAction(Sound.BLOCK_CHEST_OPEN) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						FoundAndCompletedQuestsGUI.openPublicQuestsGUI(questPlayer);
					}
				});
		menu.setItem(14, new ItemBuilder(Material.WRITABLE_BOOK)
						.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_FOUND_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_FOUND_LORE).split(";"))
						.setAmount(Math.min(64, Math.max(1, questPlayer.getFoundQuests().size())))
						.craft(),
				new ClickAction(Sound.BLOCK_CHEST_OPEN) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						FoundAndCompletedQuestsGUI.openFoundQuestsGUI(questPlayer);
					}
				});
		menu.setItem(16, new ItemBuilder(Material.BOOKSHELF)
						.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_COMPLETED_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_OVERVIEW_COMPLETED_LORE).split(";"))
						.setAmount(Math.min(64, Math.max(1, questPlayer.getFinishedQuests().size())))
						.craft(),
				new ClickAction(Sound.BLOCK_CHEST_OPEN) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						FoundAndCompletedQuestsGUI.openCompletedQuestGUI(questPlayer);
					}
				}
		);

		questPlayer.openCustomInv(menu);
	}

}
