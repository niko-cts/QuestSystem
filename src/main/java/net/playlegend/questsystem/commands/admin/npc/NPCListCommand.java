package net.playlegend.questsystem.commands.admin.npc;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.npc.NPCManager;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;

public class NPCListCommand extends APISubCommand {

	public NPCListCommand() {
		super("list", "command.quest.admin.npc.list", TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_LIST_USAGE);
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		NPCManager npcManager = QuestSystem.getInstance().getNpcManager();

		questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_LIST_TASK);
		for (String s : npcManager.getTaskNPCList(questPlayer.getLanguage())) {
			questPlayer.getPlayer().sendMessage(s);
		}

		questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_LIST_FIND);
		for (String s : npcManager.getFindNPCList(questPlayer.getLanguage())) {
			questPlayer.getPlayer().sendMessage(s);
		}
	}
}
