package chatzis.nikolas.questsystem.commands.admin.npc;

import chatzis.nikolas.questsystem.QuestSystem;
import chatzis.nikolas.questsystem.commands.handler.APISubCommand;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskCommand extends APISubCommand {
	public TaskCommand() {
		super("task", "command.quest.admin.npc.task", TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_TASK_USAGE, 2, "aufgabe", "step");
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		UUID uuid;
		try {
			uuid = UUID.fromString(arguments[1]);
		} catch (IllegalArgumentException exception) {
			sendCommandUsage(questPlayer);
			return;
		}
		switch (arguments[0].toLowerCase()) {
			case "add", "create" -> {
				if (arguments.length < 4) {
					sendCommandUsage(questPlayer);
					return;
				}

				String npcName = ChatColor.translateAlternateColorCodes('&', arguments[2]);

				Optional<Language> language = QuestSystem.getInstance().getLanguageHandler().getLanguageByName(arguments[3]);
				if (language.isEmpty()) {
					questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_LANGUAGE_ILLEGAL, "${languages}",
							QuestSystem.getInstance().getLanguageHandler().getSupportedLanguages()
									.stream().map(Language::getName).collect(Collectors.joining(", ")));
					return;
				}

				String text = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.copyOfRange(arguments, 4, arguments.length))).replace("/n", "\n");

				new BukkitRunnable() {
					@Override
					public void run() {
						QuestSystem.getInstance().getNpcManager()
								.insertTaskNPC(uuid, npcName, questPlayer.getPlayer().getLocation(), language.get().getLanguageKey(), text);
					}
				}.runTaskAsynchronously(QuestSystem.getInstance());
				questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_TASK_SUCCESSFUL);
			}
			case "remove", "delete" -> {
				new BukkitRunnable() {
					@Override
					public void run() {
						QuestSystem.getInstance().getNpcManager().deleteTaskNPC(uuid);
					}
				}.runTaskAsynchronously(QuestSystem.getInstance());
				questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_TASK_DELETED);
			}
		}

	}
}
