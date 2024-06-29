package net.playlegend.questsystem.commands.admin.npc;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.npc.NPCManager;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;

public class NPCListCommand extends APISubCommand {

	public NPCListCommand() {
		super("list", "command.quest.admin.npc", TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_LIST_USAGE, 0);
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		NPCManager npcManager = QuestSystem.getInstance().getNpcManager();

		questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_TASK, "${npc}",
				"\n" + npcManager.getTaskNPCList());
		questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_FIND, "${npc}",
				"\n" + npcManager.getFindNPCList());
	}
}
