package net.playlegend.questsystem.commands;

import net.playlegend.questsystem.commands.handler.APICommand;
import net.playlegend.questsystem.gui.QuestOverviewGUI;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;

public class QuestCommand extends APICommand {

	public QuestCommand() {
		super("quest", "", TranslationKeys.QUESTS_COMMAND_QUEST_USAGE, "aufgaben", "aufgabe", "quests", "task", "tasks");
		addSubCommand(new QuestCancelCommand());
		addSubCommand(new QuestInfoCommand());
		addSubCommand(new QuestFindCommand());
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] args) {
		QuestOverviewGUI.openOverviewGUI(questPlayer);
	}
}
