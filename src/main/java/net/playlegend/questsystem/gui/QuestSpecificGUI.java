package net.playlegend.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import lombok.NonNull;
import net.playlegend.questsystem.gui.builder.QuestBuilder;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.util.QuestTimingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.util.function.Consumer;

import static net.playlegend.questsystem.translation.TranslationKeys.*;

public class QuestSpecificGUI {

	private QuestSpecificGUI() {
		throw new UnsupportedOperationException();
	}

	public static void openQuestGUI(@NonNull QuestPlayer questPlayer, @NonNull Quest quest, boolean adminMode, @NonNull Consumer<QuestPlayer> goBack) {
		Language language = questPlayer.getLanguage();
		CustomInventory menu = new CustomInventory(quest.name(), adminMode ? 9 * 4 : 9 * 3);
		ItemStack questItem = quest.getQuestItem(language);
		menu.setItem(10, questItem);

		menu.setItem(12,
				quest.rewards().size() == 1 ?
						quest.rewards().get(0).getRewardDisplayItem(language)
						: quest.getRewardItem(language),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						if (quest.rewards().size() > 1)
							QuestRewardsGUI.openRewardsFromNormal(questPlayer, quest,
									(backPlayer, q) -> openQuestGUI(backPlayer, q, adminMode, goBack));
					}
				});

		menu.setItem(14, GUIHelper.getStepItem(language, quest.completionSteps()),
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


			GUIHelper.fillInventoryWithBackAndOpen(questPlayer, menu, null, (backPlayer, o) -> goBack.accept(backPlayer));
		}
	}
}
