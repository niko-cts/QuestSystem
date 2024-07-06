package net.playlegend.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.util.Utils;
import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Class to open active and default quest tasks.
 *
 * @author Niko
 */
public class QuestStepsGUI {

	private QuestStepsGUI() {
		throw new UnsupportedOperationException();
	}

	public static void openActiveQuestSteps(QuestPlayer questPlayer, ActivePlayerQuest activePlayerQuest, Consumer<QuestPlayer> goBack) {
		Language language = questPlayer.getLanguage();

		List<? extends QuestStep<?>> uncompletedSteps = activePlayerQuest.getUncompletedSteps();
		List<? extends QuestStep<?>> completedSteps = activePlayerQuest.getCompletedSteps();

		CustomInventory menu = new CustomInventory(
				language.translateMessage(TranslationKeys.QUESTS_GUI_ACTIVE_STEPS_TITLE),
				Utils.getPerfectInventorySize(uncompletedSteps.size() + 18 - uncompletedSteps.size() % 9 +
				                              completedSteps.size() + 4));


		menu.addItem(UsefulItems.ARROW_RIGHT()
				.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_ACTIVE_STEPS_COMPLETED))
				.setAmount(Math.min(64, Math.max(1, completedSteps.size())))
				.craft());

		int i = 1;
		for (QuestStep<?> step : completedSteps) {
			menu.setItem(i, step.getActiveTask(language, activePlayerQuest.getStepAmount(step)));
			i++;
		}

		i += 18 - i % 9;

		menu.setItem(i, UsefulItems.ARROW_RIGHT()
				.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_ACTIVE_STEPS_UNCOMPLETED))
				.setAmount(Math.min(64, Math.max(1, uncompletedSteps.size())))
				.craft());
		i++;

		for (QuestStep<?> step : uncompletedSteps) {
			menu.setItem(i, step.getActiveTask(language, activePlayerQuest.getStepAmount(step)));
			i++;
		}

		GUIHelper.fillInventoryWithBackAndOpen(questPlayer, menu, goBack);
	}

	public static void openQuestSteps(QuestPlayer questPlayer, Quest quest, BiConsumer<QuestPlayer, Quest> goBack) {
		Language language = questPlayer.getLanguage();

		List<QuestStep<?>> completionSteps = quest.completionSteps();

		CustomInventory menu = new CustomInventory(
				language.translateMessage(TranslationKeys.QUESTS_GUI_NORMAL_STEPS_TITLE),
				Utils.getPerfectInventorySize(completionSteps.size() + 1));

		menu.addItem(UsefulItems.ARROW_RIGHT()
				.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_NORMAL_STEPS_INFO))
				.setAmount(Math.min(64, Math.max(1, completionSteps.size())))
				.craft());

		for (QuestStep<?> step : completionSteps) {
			menu.addItem(step.getTaskItem(language));
		}

		GUIHelper.fillInventoryWithBackAndOpen(questPlayer, menu, quest, goBack);
	}

}
