package chatzis.nikolas.questsystem.commands.admin.npc;

import chatzis.nikolas.questsystem.commands.handler.APISubCommand;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.TranslationKeys;

public class QuestNpcCommand extends APISubCommand {
	public QuestNpcCommand() {
		super("npc", "command.quest.admin.npc", TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_USAGE);
		addSubCommand(new NPCFindCommand());
		addSubCommand(new TaskCommand());
		addSubCommand(new NPCListCommand());
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		for (APISubCommand apiSubCommand : getSubCommandList()) {
			apiSubCommand.sendCommandUsage(questPlayer);
		}
	}
}
