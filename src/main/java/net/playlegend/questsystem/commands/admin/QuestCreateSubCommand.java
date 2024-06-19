package net.playlegend.questsystem.commands.admin;

import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;

public class QuestCreateSubCommand extends APISubCommand {
	public QuestCreateSubCommand() {
		super("create", "command.quest.admin.create", TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_USAGE);
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_USAGE);

		// TODO impl menu
	}
}