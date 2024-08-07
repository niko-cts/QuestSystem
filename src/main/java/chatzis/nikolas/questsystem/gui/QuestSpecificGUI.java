package chatzis.nikolas.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import chatzis.nikolas.questsystem.gui.builder.QuestBuilder;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.quest.Quest;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import chatzis.nikolas.questsystem.util.QuestTimingsUtil;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.util.function.Consumer;

import static chatzis.nikolas.questsystem.translation.TranslationKeys.*;

/**
 * Class to open a GUI for a specific Quest.
 * This is not an active quest just the normal view from the Found, Complete, Public List.
 *
 * @author Niko
 */
public class QuestSpecificGUI {

	private QuestSpecificGUI() {
		throw new UnsupportedOperationException();
	}

	public static void openQuestGUI(@NonNull QuestPlayer questPlayer, @NonNull Quest quest, boolean adminMode, @NonNull Consumer<QuestPlayer> goBack) {
		if (questPlayer.getActivePlayerQuest().isPresent() && questPlayer.getActivePlayerQuest().get().getQuest().equals(quest)) {
			ActiveQuestGUI.openActiveGUI(questPlayer);
			return;
		}

		Language language = questPlayer.getLanguage();
		CustomInventory menu = new CustomInventory(language.translateMessage(TranslationKeys.QUESTS_GUI_QUEST_TITLE,
				"${quest}", quest.name()),
				adminMode ? 9 * 4 : 9 * 3);
		ItemStack questItem = quest.getQuestItem(language);
		menu.setItem(10, questItem);

		ItemStack rewardItem = GUIHelper.getRewardItem(language, quest.rewards());
		if (quest.rewards().size() > 1)
			rewardItem = new ItemBuilder(rewardItem).addLore(language.translateMessage(QUESTS_GUI_REWARDS_PREVIEW_LORE).split(";")).craft();

		menu.setItem(12, rewardItem,
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						if (quest.rewards().size() > 1)
							QuestRewardsGUI.openRewardsFromNormal(questPlayer, quest,
									(backPlayer, q) -> openQuestGUI(backPlayer, q, adminMode, goBack));
					}
				});

		ItemStack stepItem = GUIHelper.getStepItem(language, quest.completionSteps());
		if (quest.completionSteps().size() > 1)
			stepItem = new ItemBuilder(stepItem).addLore(language.translateMessage(QUESTS_GUI_STEPS_PREVIEW_LORE).split(";")).craft();

		menu.setItem(14, stepItem,
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						if (quest.completionSteps().size() > 1)
							QuestStepsGUI.openQuestSteps(questPlayer, quest,
									(backPlayer, q) -> openQuestGUI(backPlayer, q, adminMode, goBack));
					}
				});

		// cant start or go back when in admin mode
		if (adminMode) {
			menu.setItem(20, new ItemBuilder(Material.IRON_HOE)
							.setName(language.translateMessage(QUESTS_GUI_QUEST_ADMIN_MODIFY_NAME))
							.setLore(language.translateMessage(QUESTS_GUI_QUEST_ADMIN_MODIFY_LORE).split(";"))
							.addItemFlags(ItemFlag.HIDE_ATTRIBUTES).craft(),
					new ClickAction() {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							QuestBuilder.addPlayer(questPlayer, quest);
						}
					});
			menu.setItem(24, new ItemBuilder(Material.BARRIER)
							.setName(language.translateMessage(QUESTS_GUI_QUEST_ADMIN_DELETE_NAME))
							.setLore(language.translateMessage(QUESTS_GUI_QUEST_ADMIN_DELETE_LORE).split(";")).craft(),
					new ClickAction(true) {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							Bukkit.dispatchCommand(apiPlayer.getPlayer(), "quest remove " + quest.name());
						}
					});

			menu.fill(UsefulItems.BACKGROUND_BLACK);
			questPlayer.openCustomInv(menu);
		} else {
			Timestamp finishedAt = questPlayer.getFinishedQuests().get(quest);
			menu.setItem(16, finishedAt != null ?
							new ItemBuilder(Material.CLOCK)
									.setName(language.translateMessage(QUESTS_GUI_QUEST_TIME_NAME))
									.setLore(language.translateMessage(QUESTS_GUI_QUEST_TIME_LORE, "${time}", QuestTimingsUtil.formatDateTime(finishedAt.toInstant())).split(";")).craft()
							: UsefulItems.HEAD_A()
							.setName(language.translateMessage(QUESTS_GUI_ACCEPT_START_NAME))
							.setLore(language.translateMessage(QUESTS_GUI_ACCEPT_START_LORE).split(";")).craft(),
					new ClickAction(Sound.ENTITY_PLAYER_LEVELUP, true) {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							if (finishedAt == null) {
								if (questPlayer.getActivePlayerQuest().isPresent())
									questPlayer.switchActiveQuest(quest);
								else
									questPlayer.startActiveQuest(quest);
							}
						}
					});


			GUIHelper.fillInventoryWithBackAndOpen(questPlayer, menu, goBack);
		}
	}
}
