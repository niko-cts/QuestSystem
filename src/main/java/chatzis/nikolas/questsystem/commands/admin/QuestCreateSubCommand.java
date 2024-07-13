package chatzis.nikolas.questsystem.commands.admin;

import chatzis.nikolas.questsystem.commands.handler.APISubCommand;
import chatzis.nikolas.questsystem.gui.builder.QuestBuilder;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class QuestCreateSubCommand extends APISubCommand {
	public QuestCreateSubCommand() {
		super("create", "command.quest.admin.create", TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_CREATE_USAGE, 0, "add");
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		QuestBuilder builder = QuestBuilder.getBuilder(questPlayer.getUniqueId());
		if (builder != null && arguments.length > 1) {
			if (arguments[0].equalsIgnoreCase("description")) {
				builder.setDescription(ChatColor.translateAlternateColorCodes('&',
						String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length))));
				builder.openMenu();
			} else {
				sendCommandUsage(questPlayer);
			}
			return;
		}
		QuestBuilder.addPlayer(questPlayer);
	}
}
