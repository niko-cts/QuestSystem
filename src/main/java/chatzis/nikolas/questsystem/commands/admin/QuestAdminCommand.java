package chatzis.nikolas.questsystem.commands.admin;

import chatzis.nikolas.questsystem.QuestSystem;
import chatzis.nikolas.questsystem.commands.admin.npc.QuestNpcCommand;
import chatzis.nikolas.questsystem.commands.handler.APICommand;
import chatzis.nikolas.questsystem.commands.handler.APISubCommand;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.TranslationKeys;

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
		if (QuestSystem.getInstance().getNpcManager() != null)
			addSubCommand(new QuestNpcCommand());
		addSubCommand(QuestSystem.getInstance().getQuestSignManager());
		addSubCommand(new QuestFindCommand());
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
		for (APISubCommand apiSubCommand : getSubCommandList()) {
			apiSubCommand.sendCommandUsage(questPlayer);
		}
	}
}
