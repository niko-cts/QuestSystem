package net.playlegend.questsystem.gui;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class GUIHelper {

	private GUIHelper() {
		throw new UnsupportedOperationException();
	}

	public static <T> void fillInventoryWithBackAndOpen(QuestPlayer questPlayer, CustomInventory menu, T backQuest, BiConsumer<QuestPlayer, T> goBack) {
		menu.setItem(menu.getInventory().getSize() - 1, UsefulItems.ARROW_LEFT()
						.setName(questPlayer.getLanguage().translateMessage(TranslationKeys.QUESTS_GUI_BACK)).craft(),
				new ClickAction(Sound.BLOCK_CHEST_OPEN) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						goBack.accept(questPlayer, backQuest);
					}
				});

		menu.fill(UsefulItems.BACKGROUND_BLACK);

		questPlayer.openCustomInv(menu);
	}
}
