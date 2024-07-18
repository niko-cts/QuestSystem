package chatzis.nikolas.questsystem.commands.handler;

import chatzis.nikolas.questsystem.QuestSystem;
import lombok.Getter;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler for the APICommands
 *
 * @author Niko
 * @since 0.0.1
 */
public class CommandHandler {

	@Getter
	private final Map<String, APICommand> commandMap;
	@Getter
	private final Map<String, APICommand> eventCommands;
	private final SimpleCommandMap simpleCommandMap;

	/**
	 * Creates an instance of the command handler.
	 *
	 * @since 0.0.1
	 */
	public CommandHandler() {
		this.commandMap = new HashMap<>();
		this.eventCommands = new HashMap<>();
		this.simpleCommandMap = ((CraftServer) QuestSystem.getInstance().getServer()).getCommandMap();
	}

	/**
	 * Adds a list of APICommands to the CommandHandler
	 *
	 * @param apiCommand {@link APICommand}... - List of Commands
	 * @since 0.0.1
	 */
	public void addCommands(APICommand... apiCommand) {
		addCommands(Arrays.asList(apiCommand));
	}

	/**
	 * Adds a list of APICommands to the CommandHandler.
	 *
	 * @param commandList List of {@link APICommand} - the list of commands.
	 * @since 0.0.1
	 */
	public void addCommands(List<APICommand> commandList) {
		for (APICommand command : commandList) {
			registerCommand(command);
		}
	}

	/**
	 * Registers a command when added to CommandHandler
	 */
	private void registerCommand(APICommand command) {
		commandMap.put(command.getCommand().toLowerCase(), command);
		for (String commandAlias : command.getCommandAliases()) {
			commandMap.put(commandAlias.toLowerCase(), command);
			if (command.isRunAsEvent())
				eventCommands.put(commandAlias.toLowerCase(), command);
		}

		if (command.isRunAsEvent()) {
			eventCommands.put(command.getCommand().toLowerCase(), command);
			QuestSystem.getInstance().getLogger().info("Registered command: " + command.getCommand() + " as event.");
			return;
		}


		try {
			simpleCommandMap.register(command.getCommand(), command);
			QuestSystem.getInstance().getLogger().info("Registered command: " + command.getCommand());
		} catch (Exception exception) {
			QuestSystem.getInstance().getLogger().warning("Failed to register command " + command.getCommand() + ": " + exception.getMessage());
		}
	}

}
