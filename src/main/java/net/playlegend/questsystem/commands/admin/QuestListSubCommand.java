package net.playlegend.questsystem.commands.admin;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.gui.QuestSpecificGUI;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.translation.TranslationKeys;

import java.util.Iterator;

public class QuestListSubCommand extends APISubCommand {

	public QuestListSubCommand() {
		super("list", "command.quest.admin.list", TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_USAGE);
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		if (arguments.length == 0) {
			StringBuilder quests = new StringBuilder();
			Iterator<Quest> questIterator = QuestSystem.getInstance().getQuestManager().getQuests().iterator();
			if (!questIterator.hasNext()) {
				questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_LIST_NO_QUESTS);
			} else {
				while (questIterator.hasNext()) {
					quests.append(questIterator.next().name());
					if (questIterator.hasNext())
						quests.append(", ");
				}
				questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_LIST, "${quests}", quests.toString());
			}
		} else {
			String name = arguments[0];
			Quest quest = getQuestByNameOrMessageError(questPlayer, name);
			if (quest != null) {
				QuestSpecificGUI.openQuestGUI(questPlayer, quest);
			}
		}
	}
}
