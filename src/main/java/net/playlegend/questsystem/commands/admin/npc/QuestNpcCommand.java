package net.playlegend.questsystem.commands.admin.npc;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.npc.NPCManager;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;

public class QuestNpcCommand extends APISubCommand {
	public QuestNpcCommand() {
		super("npc", "command.quest.admin.npc", TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_USAGE);
		addSubCommand(new FindCommand());
		addSubCommand(new TaskCommand());
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		sendCommandUsage(questPlayer);

		NPCManager npcManager = QuestSystem.getInstance().getNpcManager();

		questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_TASK, "${npc}",
				npcManager.getTaskNPCList());
		questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_FIND, "${npc}",
				npcManager.getFindNPCList());
	}
}