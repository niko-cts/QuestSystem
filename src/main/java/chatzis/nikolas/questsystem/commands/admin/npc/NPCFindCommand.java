package chatzis.nikolas.questsystem.commands.admin.npc;

import chatzis.nikolas.questsystem.QuestSystem;
import chatzis.nikolas.questsystem.commands.handler.APISubCommand;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.quest.Quest;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCFindCommand extends APISubCommand {
	public NPCFindCommand() {
		super("find", "command.quest.admin.npc.find", TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_FIND_USAGE, 2);
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		Quest quest = getQuestByNameOrMessageError(questPlayer, arguments[1]);
		if (quest == null) return;
		switch (arguments[0].toLowerCase()) {
			case "add", "create" -> {
				if (arguments.length < 3) {
					sendCommandUsage(questPlayer);
					return;
				}
				String npcName = ChatColor.translateAlternateColorCodes('&', arguments[2]);
				new BukkitRunnable() {
					@Override
					public void run() {
						QuestSystem.getInstance().getNpcManager()
								.insertFindNPC(quest, npcName, questPlayer.getPlayer().getLocation());
					}
				}.runTaskAsynchronously(QuestSystem.getInstance());
				questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_FIND_SUCCESSFUL);
			}
			case "delete", "remove" -> {
				new BukkitRunnable() {
					@Override
					public void run() {
						QuestSystem.getInstance().getNpcManager().deleteFindNPC(quest);
					}
				}.runTaskAsynchronously(QuestSystem.getInstance());
				questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_FIND_DELETED);
			}
		}
	}
}
