package net.playlegend.questsystem.util;

import org.bukkit.ChatColor;

public class ColorConverterUtil {

	private ColorConverterUtil() {
		throw new UnsupportedOperationException();
	}

	public static String convertToBlackColors(String text) {
		return text.replace(ChatColor.YELLOW.getChar(), ChatColor.GOLD.getChar())
				.replace(ChatColor.GRAY.getChar(), ChatColor.BLACK.getChar());
	}

}
