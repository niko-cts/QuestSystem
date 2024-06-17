package net.playlegend.questsystem.commands.admin;

import net.playlegend.questsystem.commands.handler.APICommand;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;

public class QuestAdminCommand extends APICommand {

	/**
	 * Creates the quest admin command object with all needed values to function.
	 * @since 0.0.1
	 */
	public QuestAdminCommand() {
		super("questadmin", "command.quest.admin", TranslationKeys.QUESTS_COMMAND_ADMIN_USAGE, "adminquest");
		addSubCommand(new QuestCreateSubCommand());
		addSubCommand(new QuestDeleteSubCommand());
		addSubCommand(new QuestListSubCommand());
	}

	/**
	 * Gets fired when a player executed a command and has permissions to do so.
	 *
	 * @param questPlayer {@link QuestPlayer} - the player who executed the command.
	 * @param args        String[] - the arguments which were used.
	 * @since 0.0.1
	 */
	@Override
	public void onCommand(QuestPlayer questPlayer, String[] args) {
		sendCommandUsage(questPlayer);
	}
}
