package net.playlegend.questsystem.commands;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.commands.handler.APICommand;
import net.playlegend.questsystem.gui.ActiveQuestGUI;
import net.playlegend.questsystem.gui.FoundAndCompletedQuestsGUI;
import net.playlegend.questsystem.gui.QuestOverviewGUI;
import net.playlegend.questsystem.gui.QuestSpecificGUI;
import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;

import java.util.Optional;

public class QuestCommand extends APICommand {

	public QuestCommand() {
		super("quest", "", TranslationKeys.QUESTS_COMMAND_QUEST_USAGE, "aufgaben", "aufgabe", "quests", "task", "tasks");
		addSubCommand(new QuestCancelCommand());
		addSubCommand(new QuestInfoCommand());
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("found")) {
				if (args.length > 1) {
					Quest quest = getQuestByNameOrMessageError(questPlayer, args[1]);
					if (quest != null) {
						QuestSpecificGUI.openQuestGUI(questPlayer, quest, false, FoundAndCompletedQuestsGUI::openFoundQuestsGUI);
						return;
					}
				}
				FoundAndCompletedQuestsGUI.openFoundQuestsGUI(questPlayer);
				return;
			}
			if (args[0].equalsIgnoreCase("completed")) {
				if (args.length > 1) {
					Quest quest = getQuestByNameOrMessageError(questPlayer, args[1]);
					if (quest != null) {
						QuestSpecificGUI.openQuestGUI(questPlayer, quest, false, FoundAndCompletedQuestsGUI::openCompletedQuestGUI);
						return;
					}
				}
				FoundAndCompletedQuestsGUI.openCompletedQuestGUI(questPlayer);
				return;
			}
			if (args[0].equalsIgnoreCase("public")) {
				if (args.length > 1) {
					Quest quest = getQuestByNameOrMessageError(questPlayer, args[1]);
					if (quest != null) {
						QuestSpecificGUI.openQuestGUI(questPlayer, quest, false, FoundAndCompletedQuestsGUI::openPublicQuestsGUI);
						return;
					}
				}
				FoundAndCompletedQuestsGUI.openPublicQuestsGUI(questPlayer);
				return;
			}
		}

		Optional<ActivePlayerQuest> activePlayerQuest = questPlayer.getActivePlayerQuest();
		if (activePlayerQuest.isPresent()) {
			ActiveQuestGUI.openActiveGUI(questPlayer);
			return;
		}

		QuestOverviewGUI.openOverviewGUI(questPlayer);
	}

	private Quest getQuestByNameOrMessageError(QuestPlayer questPlayer, String name) {
		Optional<Quest> quest = QuestSystem.getInstance().getQuestManager().getQuestByName(ChatColor.translateAlternateColorCodes('&', name));
		if (quest.isEmpty()) {
			questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_NOT_FOUND, "${name}", name);
			return null;
		}
		return quest.get();
	}
}
