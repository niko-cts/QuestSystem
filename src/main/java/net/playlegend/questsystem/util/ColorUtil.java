package net.playlegend.questsystem.util;

import lombok.NonNull;
import org.bukkit.ChatColor;

public class ColorUtil {

	private ColorUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Converts bright colors to black usually to display in books.
	 *
	 * @param text String - text to transform
	 * @return String - text with darker chat colors
	 */
	public static String convertToBlackColors(@NonNull String text) {
		return text.replace(ChatColor.YELLOW.toString(), ChatColor.GOLD.toString())
				.replace(ChatColor.GRAY.toString(), ChatColor.DARK_GRAY.toString())
				.replace(ChatColor.LIGHT_PURPLE.toString(), ChatColor.DARK_PURPLE.toString())
				.replace(ChatColor.BLUE.toString(), ChatColor.DARK_BLUE.toString())
				.replace(ChatColor.GREEN.toString(), ChatColor.DARK_GREEN.toString())
				.replace(ChatColor.AQUA.toString(), ChatColor.DARK_AQUA.toString())
				.replace(ChatColor.RED.toString(), ChatColor.DARK_RED.toString());
	}
}
