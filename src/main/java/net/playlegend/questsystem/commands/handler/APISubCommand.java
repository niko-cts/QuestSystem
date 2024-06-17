package net.playlegend.questsystem.commands.handler;

import lombok.Getter;
import lombok.Setter;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Class to create a sub command for an APICommand
 *
 * @author Niko
 * @since 0.0.1
 */
public abstract class APISubCommand {

	@Getter
	private final String name;
	@Getter
	private final String permission;
	@Getter
	private final String usage;
	@Getter
	private final int minimumArguments;
	@Getter
	private final String[] aliases;
	@Setter
	protected boolean tabRecommendPlayers;

	private final Map<String, APISubCommand> subCommandMap;

	/**
	 * Creates an instance of the api sub command.
	 *
	 * @param name             String - the sub command name.
	 * @param permission       String - the sub command permission.
	 * @param usage            String - the usage of the sub command.
	 * @param minimumArguments int - the minimum arguments needed to perform the sub command.
	 * @since 0.0.1
	 */
	public APISubCommand(String name, String permission, String usage, int minimumArguments, String... aliases) {
		this.name = name;
		this.permission = permission;
		this.usage = usage;
		this.minimumArguments = minimumArguments;
		this.aliases = aliases;
		this.tabRecommendPlayers = false;

		this.subCommandMap = new HashMap<>();
	}

	/**
	 * Creates an instance of the api sub command.
	 *
	 * @param name        String - the sub command name.
	 * @param permission  String - the sub command permission.
	 * @param usage       String - the usage of the sub command.
	 * @since 0.0.1
	 */
	public APISubCommand(String name, String permission, String usage) {
		this(name, permission, usage, 0);
	}

	/**
	 * Creates an instance of the api sub command.
	 *
	 * @param name             String - the sub command name.
	 * @param usage            String - the usage of the sub command.
	 * @param minimumArguments int - the minimum arguments needed to perform the sub command.
	 * @since 0.0.1
	 */
	public APISubCommand(String name, String usage, int minimumArguments) {
		this(name, null, usage, minimumArguments);
	}

	/**
	 * Creates an instance of the api sub command.
	 *
	 * @param name        String - the sub command name.
	 * @param usage       String - the usage of the sub command.
	 * @since 0.0.1
	 */
	public APISubCommand(String name, String usage) {
		this(name, null, usage, 0);
	}

	/**
	 * Executes a sub command called by a command sender.
	 *
	 * @param commandSender CommandSender - the sender of the command.
	 * @param arguments     String[] - the given arguments.
	 * @since 0.0.1
	 */
	public void execute(CommandSender commandSender, String[] arguments) {
		if (!subCommandMap.isEmpty() && arguments.length > 0) {
			String firstArg = arguments[0];
			APISubCommand subCommand = subCommandMap.get(firstArg);
			if (subCommand != null) {
				subCommand.execute(commandSender, Arrays.copyOfRange(arguments, 1, arguments.length));
				return;
			}
		}

		if (commandSender instanceof Player p) {
			QuestPlayer player = QuestSystem.getInstance().getPlayerHandler().getPlayer(p);
			executePlayer(player, Arrays.copyOfRange(arguments, 1, arguments.length));
			return;
		}

		if (arguments.length < minimumArguments) {
			return;
		}

		onConsole(commandSender, arguments);
	}

	/**
	 * Executes the sub command as a player.
	 *
	 * @param questPlayer {@link QuestPlayer} - the executor.
	 * @param arguments String[] the given arguments.
	 * @since 0.0.1
	 */
	private void executePlayer(QuestPlayer questPlayer, String[] arguments) {
		if (hasPermission() && !questPlayer.getPlayer().hasPermission(permission)) {
			questPlayer.sendMessage(TranslationKeys.SYSTEM_NO_PERMISSION);
			return;
		}

		if (arguments.length <= minimumArguments) {
			sendCommandUsage(questPlayer);
			return;
		}

		onCommand(questPlayer, arguments);
	}

	/**
	 * Method that gets called if a subcommand is executed by a player.
	 *
	 * @param questPlayer {@link QuestPlayer} - the executor.
	 * @param arguments String[] - the command arguments.
	 * @since 0.0.1
	 */
	public abstract void onCommand(QuestPlayer questPlayer, String[] arguments);

	/**
	 * Method that gets called if a subcommand is executed by the console.
	 *
	 * @param commandSender CommandSender - the executor.
	 * @param arguments     String[] - the command arguments.
	 * @since 0.0.1
	 */
	public void onConsole(CommandSender commandSender, String[] arguments) {
		commandSender.sendMessage("This command is not implemented for the console.");
	}

	/**
	 * Checks whatever the sub command has a permission set or not.
	 *
	 * @return boolean - whatever a permission is set.
	 */
	public boolean hasPermission() {
		return permission != null;
	}

	/**
	 * Adds a sub command to the sub command to chain commands.
	 *
	 * @param subCommand {@link APISubCommand} - the sub command.
	 * @since 0.0.1
	 */
	public void addSubCommand(APISubCommand subCommand) {
		subCommandMap.put(subCommand.getName(), subCommand);
		for (String alias : subCommand.getAliases()) {
			subCommandMap.put(alias, subCommand);
		}
	}

	/**
	 * Returns a list of all registered subcommands.
	 *
	 * @return Collection ({@link APISubCommand}).
	 * @since 0.0.1
	 */
	public Collection<APISubCommand> getSubCommandList() {
		return subCommandMap.values();
	}

	/**
	 * Get the tab recommendation list for the next argument.
	 *
	 * @return List<String> - List of recommendations for the next argument.
	 * @since 0.0.1
	 */
	public List<String> getTabRecommendations() {
		return new ArrayList<>();
	}

	/**
	 * Sends the correct usage of the command to a specified player.
	 *
	 * @param player {@link QuestPlayer} - the player to send the usage to.
	 * @since 0.0.1
	 */
	public void sendCommandUsage(QuestPlayer player) {
		String commandUsage = player.getCurrentLanguage().translateMessage(getUsage(), "${command}", getName());
		player.sendMessage(TranslationKeys.SYSTEM_COMMAND_USAGE, "${command}", commandUsage);
	}

	public Quest getQuestByNameOrMessageError(QuestPlayer questPlayer, String name) {
		Optional<Quest> quest = QuestSystem.getInstance().getQuestManager().getQuestByName(name);
		if (quest.isEmpty()) {
			questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_NOT_FOUND, "${name}", name);
			return null;
		}
		return quest.get();
	}
}
