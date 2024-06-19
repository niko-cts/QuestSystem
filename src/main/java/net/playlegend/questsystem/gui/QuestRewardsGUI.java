package net.playlegend.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.reward.IQuestReward;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;

import java.util.List;
import java.util.function.BiConsumer;

public class QuestRewardsGUI {

	private QuestRewardsGUI() {
		throw new UnsupportedOperationException();
	}

	public static void openRewardsFromNormal(QuestPlayer questPlayer, Quest quest, BiConsumer<QuestPlayer, Quest> goBack) {
		openRewardsWithBack(questPlayer, quest, quest, goBack);
	}

	public static <T> void openRewardsWithBack(QuestPlayer questPlayer, Quest quest, T backQuest, BiConsumer<QuestPlayer, T> goBack) {
		Language language = questPlayer.getLanguage();
		List<IQuestReward> rewards = quest.rewards();
		CustomInventory menu = new CustomInventory(
				language.translateMessage(TranslationKeys.QUESTS_GUI_REWARDS_TITLE, "${name}", quest.name()),
				rewards.size() + 2
		);

		for (IQuestReward reward : rewards) {
			menu.addItem(reward.getRewardDisplayItem(language));
		}

		GUIHelper.fillInventoryWithBackAndOpen(questPlayer, menu, backQuest, goBack);
	}

}
