package chatzis.nikolas.questsystem.commands.handler;

import chatzis.nikolas.questsystem.QuestSystem;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * A command class for custom commands
 * @author Niko
 * @since 0.0.1
 */
public abstract class APICommand extends Command {

    @Getter
    private final String command;
    private final String permission;
    @Getter
    private final String desc;
    private final String usage;

    @Setter
    @Getter
	private boolean enabled;
    @Setter
    @Getter
	private boolean runAsEvent;
    @Setter
    @Getter
	private boolean tabRecommendPlayers;
    @Setter
    @Getter
	private long spamProtectionTicks;

    @Getter
    private final List<String> commandAliases;
    private final Map<String, APISubCommand> subCommandMap;

    /**
     * Creates the api command object with all needed values to function.
     * @param command String - the command name.
     * @param permission String - the permission to run the command.
     * @param usage String - the correct command usage.
     * @param aliases List<String> - the aliases for the command.
     * @since 0.0.1
     */
    public APICommand(String command, String permission, String usage, String... aliases) {
        super(command, "", usage, Arrays.asList(aliases));
        this.command = command;
        this.permission = permission;
        this.usage = usage;
        this.desc = description;
        this.commandAliases = Arrays.asList(aliases);
        this.enabled = true;
        this.runAsEvent = false;
        this.tabRecommendPlayers = false;
        this.spamProtectionTicks = 20L;

        this.subCommandMap = new HashMap<>();
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        if (commandSender instanceof Player p) {
            if (!isEnabled() && !commandSender.hasPermission("command.disabled.bypass")) {
                QuestPlayer questPlayer = QuestSystem.getInstance().getPlayerHandler().getPlayer(p);
                if (questPlayer != null) 
                    questPlayer.sendMessage(TranslationKeys.SYSTEM_DISABLED_COMMAND);
                return true;
            }
        }

        if (checkSubCommand(commandSender, args))
            return true;

        if (commandSender instanceof Player) {
            executePlayerCommand((Player) commandSender, args);
            return true;
        }

        onConsole(commandSender, args);
        return true;
    }

    @Override
    public boolean testPermission(CommandSender target) {
        if (this.testPermissionSilent(target))
            return true;
        if (target instanceof Player player) {
            QuestPlayer questPlayer = QuestSystem.getInstance().getPlayerHandler().getPlayer(((Player) target).getUniqueId());
            if (questPlayer != null)
                player.sendMessage(TranslationKeys.SYSTEM_NO_PERMISSION);
        }
        return false;
    }

    /**
     * Checks sub commands and executes them if one is found.
     * @param commandSender CommandSender - the sender of the command.
     * @param args String[] - the arguments.
     * @return boolean - whatever a subcommand has been executed.
     * @since 0.0.1
     */
    private boolean checkSubCommand(CommandSender commandSender, String[] args) {
        if (args.length == 0 || subCommandMap.isEmpty())
            return false;

        String subCommandName = args[0];
        APISubCommand subCommand = subCommandMap.get(subCommandName);

        if (subCommand == null)
            return false;

        subCommand.execute(commandSender, getNewArguments(args));

        return true;
    }

    /**
     * Executes a command for a player and checks the permissions.
     * @param player Player - the player to perform
     * @param args String[] - the arguments which were used.
     * @since 0.0.1
     */
    private void executePlayerCommand(Player player, String[] args) {
        QuestPlayer questPlayer = QuestSystem.getInstance().getPlayerHandler().getPlayer(player);

        if (!permission.isEmpty() && !player.hasPermission(permission)) {
            sendNoPermissionMessage(questPlayer);
            return;
        }

        onCommand(questPlayer, args);
    }

    /**
     * Returns a list of all registered subcommands.
     * @return Collection ({@link APISubCommand}).
     * @since 0.0.1
     */
    public Collection<APISubCommand> getSubCommandList() {
        return subCommandMap.values();
    }

    /**
     * Gets fired when a player executed a command and has permissions to do so.
     * @param questPlayer {@link QuestPlayer} - the player who executed the command.
     * @param args String[] - the arguments which were used.
     * @since 0.0.1
     */
    public abstract void onCommand(QuestPlayer questPlayer, String[] args);

    /**
     * Gets fired when the console executes a command.
     * @param commandSender CommandSender - the command sender.
     * @param args String[] - the arguments used.
     * @since 0.0.1
     */
    public void onConsole(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("This command is not implemented for the console.");
    }

    /**
     * Returns a new array of Strings with command arguments, cuts the start of oldArguments.
     * @param oldArguments String[] - the old arguments to cut.
     * @return String[] - the new arguments.
     * @since 0.0.1
     */
    private String[] getNewArguments(String[] oldArguments) {

        if (oldArguments.length <= 1) {
            return new String[] {};
        }

        return Arrays.copyOfRange(oldArguments, 1, oldArguments.length);
    }

    /**
     * Adds an APISubCommand to the command.
     * @param subCommand {@link APISubCommand} - the subcommand to add.
     * @since 0.0.1
     */
    public void addSubCommand(APISubCommand subCommand) {
        subCommandMap.put(subCommand.getName(), subCommand);
        for (String alias : subCommand.getAliases()) {
            subCommandMap.put(alias, subCommand);
        }
    }

	@Override
    public String getUsage() {
        return usage;
    }

    @Override
    public String getPermission() {
        return permission;
    }

	/**
     * Sends the correct usage of the command to a specified player.
     * @param player {@link QuestPlayer} - the player to send the usage to.
     * @since 0.0.1
     */
    public void sendCommandUsage(QuestPlayer player) {
        String commandUsage = player.getLanguage().translateMessage(getUsage(), "${command}", getCommand());
        player.sendMessage(TranslationKeys.SYSTEM_COMMAND_USAGE, "${command}", commandUsage);
    }

    /**
     * Sends the correct usage of the command to a command sender.
     * @param commandSender CommandSender - the sender to send the usage to.
     * @since 0.0.1
     */
    public void sendCommandUsage(CommandSender commandSender) {
        Language apiLanguage = QuestSystem.getInstance().getLanguageHandler().getFallbackLanguage();
        String commandUsage = apiLanguage.translateMessage(getUsage(), "${command}", getCommand());
        String message = apiLanguage.translateMessage(TranslationKeys.SYSTEM_COMMAND_USAGE,"${command}", commandUsage);
        commandSender.sendMessage(message);
    }

    /**
     * Sends a no permission message to the player.
     * Could be overwritten to send a custom message like different content
     * for players without permission.
     * @param QuestPlayer {@link QuestPlayer} - the player to sent to.
     * @since 0.0.1
     */
    public void sendNoPermissionMessage(QuestPlayer QuestPlayer) {
        QuestPlayer.sendMessage(TranslationKeys.SYSTEM_NO_PERMISSION);
    }

    /**
     * Will be called, when a player or console did a tab complete.
     * @param sender CommandSender - The sender who called the tab complete.
     * @param alias String - the command.
     * @param args String[] - arguments afterwards
     * @return List<String> - The recommendation list.
     * @throws IllegalArgumentException - could throw an exception.
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (!sender.hasPermission(permission))
            return new ArrayList<>();
        if (args.length == 0 || getSubCommandList().isEmpty()) {
            List<String> tabRecommendations = new ArrayList<>(getTabRecommendations());
            for (APISubCommand apiSubCommand : getSubCommandList())
                tabRecommendations.add(apiSubCommand.getName());

            if (this.tabRecommendPlayers) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    tabRecommendations.add(onlinePlayer.getName());
                }
            }
            return tabRecommendations;
        }

        return getCurrentSubCommand(getSubCommandList(), args, 0);
    }

    /**
     * Get the tab recommendation list for the first argument.
     * @return List<String> - List of recommendations for the first argument.
     * @since 0.0.1
     */
    public List<String> getTabRecommendations() {
        return new ArrayList<>();
    }

    /**
     * Recursive method, which returns the current subcommands the user can write.
     * @param subCommandList Collection<APISubCommand> - A collection of sub commands to check.
     * @param args String[] - the arguments of the command line.
     * @param i int - the current argument checking.
     * @return Collection<APISubCommand> - all sub commands the player may execute.
     * @since 0.0.1
     */
    private List<String> getCurrentSubCommand(Collection<APISubCommand> subCommandList, String[] args, int i) {
        if (args.length != i + 1) {
            APISubCommand apiSubCommand = subCommandList.stream()
                    .filter(s -> args[i].replace(" ", "").isEmpty() || s.getName().equalsIgnoreCase(args[i]) ||
                    Arrays.stream(s.getAliases()).anyMatch(a -> a.equalsIgnoreCase(args[i].toLowerCase()))).findFirst().orElse(null);

            if (apiSubCommand == null || apiSubCommand.getSubCommandList().isEmpty()) {
                List<String> result = apiSubCommand == null ? new ArrayList<>() : apiSubCommand.getTabRecommendations();
                if (apiSubCommand == null && this.tabRecommendPlayers || apiSubCommand != null && apiSubCommand.tabRecommendPlayers)
                    Bukkit.getOnlinePlayers().stream().filter(player -> args[i].replace(" ", "").isEmpty() || player.getName().toLowerCase().startsWith(args[i].toLowerCase())).forEach(player -> result.add(player.getName()));

                return result;
            }
            return getCurrentSubCommand(apiSubCommand.getSubCommandList(), args, i + 1);
        }

	    return getCommandWithAliases(subCommandList, args, i);
    }

    private static List<String> getCommandWithAliases(Collection<APISubCommand> subCommandList, String[] args, int i) {
        List<String> results = new ArrayList<>();

        for (APISubCommand apiSubCommand : subCommandList) {
            if (args[i].replace(" ", "").isEmpty()) {
               results.add(apiSubCommand.getName());
               continue;
            }
            if (apiSubCommand.getName().toLowerCase().startsWith(args[i].toLowerCase())) {
                results.add(apiSubCommand.getName());
                continue;
            }
            for (String alias : apiSubCommand.getAliases()) {
                if (alias.toLowerCase().startsWith(args[i].toLowerCase())) {
                    results.add(alias);
                    break;
                }
            }
        }
        return results;
    }
}
