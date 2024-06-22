package net.playlegend.questsystem.commands.admin;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuestDeleteSubCommand extends APISubCommand {

	private final Map<UUID, BukkitTask> confirmation;

	/**
	 * Creates an instance of the quest delete sub command.
	 */
	public QuestDeleteSubCommand() {
		super("delete", "command.quest.admin.delete", TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_DELETE_USAGE, 1, "remove");
		this.confirmation = new HashMap<>();
	}

	/**
	 * Method that gets called if a subcommand is executed by a player.
	 *
	 * @param questPlayer {@link QuestPlayer} - the executor.
	 * @param arguments   String[] - the command arguments.
	 * @since 0.0.1
	 */
	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		String name = arguments[0];
		confirmation.compute(questPlayer.getUniqueId(), (uuid, runnable) -> {
			if (runnable == null) {

				if (getQuestByNameOrMessageError(questPlayer, name) == null)
					return null;

				questPlayer.sendClickableMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM, "questadmin quest delete " + name);

				return new BukkitRunnable() {
					@Override
					public void run() {
						confirmation.remove(uuid);
					}
				}.runTaskLater(QuestSystem.getInstance(), 20 * 30);
			} else {
				runnable.cancel();
				questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_DELETE_SUCCESSFUL);
				new BukkitRunnable() {

					@Override
					public void run() {
						QuestSystem.getInstance().getQuestManager().deleteQuest(name);
					}
				}.runTaskAsynchronously(QuestSystem.getInstance());
				return null;
			}
		});
	}
}
