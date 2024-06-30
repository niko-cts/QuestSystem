package net.playlegend.questsystem.commands;

import net.playlegend.questsystem.commands.handler.APICommand;
import net.playlegend.questsystem.gui.ActiveQuestGUI;
import net.playlegend.questsystem.gui.FoundAndCompletedQuestsGUI;
import net.playlegend.questsystem.gui.QuestOverviewGUI;
import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;

import java.util.Optional;

public class QuestCommand extends APICommand {

	public QuestCommand() {
		super("quest", "", TranslationKeys.QUESTS_COMMAND_QUEST_USAGE, "aufgaben", "aufgabe", "quests", "task", "tasks");
		addSubCommand(new QuestCancelCommand());
		addSubCommand(new QuestInfoCommand());
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] args) {
		Optional<ActivePlayerQuest> activePlayerQuest = questPlayer.getActivePlayerQuest();
		if (activePlayerQuest.isPresent()) {
			ActiveQuestGUI.openActiveGUI(questPlayer, activePlayerQuest.get());
			return;
		}

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("found")) {
				FoundAndCompletedQuestsGUI.openFoundQuestsGUI(questPlayer);
				return;
			}
			if (args[0].equalsIgnoreCase("completed")) {
				FoundAndCompletedQuestsGUI.openCompletedQuestGUI(questPlayer);
				return;
			}
			if (args[0].equalsIgnoreCase("public")) {
				FoundAndCompletedQuestsGUI.openPublicQuestsGUI(questPlayer);
				return;
			}
		}
		QuestOverviewGUI.openOverviewGUI(questPlayer);
	}
}
