package net.playlegend.questsystem.util;

import net.playlegend.questsystem.QuestSystem;
import org.bukkit.Bukkit;

public class SaveTimerUtil {

	private SaveTimerUtil() {
		throw new UnsupportedOperationException();
	}

	private static final long SAVE_DELAY_TICKS = 20 * 60 * 30; // 30 mins


	public static void startsSaveTimer(QuestSystem plugin) {
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, SaveTimerUtil::saveAllPlayer, SAVE_DELAY_TICKS, SAVE_DELAY_TICKS);
	}

	private static void saveAllPlayer() {
		QuestSystem plugin = QuestSystem.getInstance();

		if (plugin.isEnabled() && !Bukkit.getOnlinePlayers().isEmpty()) {
			plugin.getLogger().info("Start to save data for all online players...");
			plugin.getPlayerHandler().saveAllPlayers();
			plugin.getLogger().info("Data saved.");
		}
	}
}
