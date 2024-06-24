package net.playlegend.questsystem.commands.admin.npc;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskCommand extends APISubCommand {
	public TaskCommand() {
		super("task", "command.quest.admin.npc.task", TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_TASK_USAGE, 4, "aufgabe", "step");
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		String npcName = ChatColor.translateAlternateColorCodes('&', arguments[0]);
		try {
			UUID uuid = UUID.fromString(arguments[1]);

			Optional<Language> language = QuestSystem.getInstance().getLanguageHandler().getLanguageByName(arguments[2]);
			if (language.isEmpty()) {
				questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_LANGUAGE_ILLEGAL, "${languages}",
						language.stream().map(Language::getName).collect(Collectors.joining(", ")));
				return;
			}

			String text = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.copyOfRange(arguments, 3, arguments.length))).replace("/n", "\n");

			new BukkitRunnable() {
				@Override
				public void run() {
					QuestSystem.getInstance().getNpcManager()
							.insertTaskNPC(uuid, npcName, questPlayer.getPlayer().getLocation(), language.get().getLanguageKey(), text);
				}
			}.runTaskAsynchronously(QuestSystem.getInstance());
			questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_SETUP_SUCCESSFUL);
		} catch (IllegalArgumentException exception) {
			sendCommandUsage(questPlayer);
		}
	}
}
