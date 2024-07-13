package chatzis.nikolas.questsystem.util;

import chatzis.nikolas.questsystem.QuestSystem;
import org.bukkit.Bukkit;

/**
 * Will start an asynchronous task which saves every player data in a period of time.
 * @author Niko
 */
public class PlayerQuestDataSaveTimer {

	private PlayerQuestDataSaveTimer() {
		throw new UnsupportedOperationException();
	}

	private static final long SAVE_DELAY_TICKS = 20 * 60 * 30; // 30 mins


	public static void startsSaveTimer(QuestSystem plugin) {
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, PlayerQuestDataSaveTimer::saveAllPlayer, SAVE_DELAY_TICKS, SAVE_DELAY_TICKS);
	}

	private static void saveAllPlayer() {
		QuestSystem plugin = QuestSystem.getInstance();

		if (plugin.isEnabled() && !plugin.getServer().getOnlinePlayers().isEmpty()) {
			plugin.getLogger().info("Start to save data for all online players...");
			plugin.getPlayerHandler().saveAllPlayers();
			plugin.getLogger().info("Data saved.");
		}
	}
}
