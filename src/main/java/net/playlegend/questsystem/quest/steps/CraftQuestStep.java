package net.playlegend.questsystem.quest.steps;

import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CraftQuestStep extends QuestStep {

	private final ItemStack stackToCraft;

	public CraftQuestStep(int id, int order, int maxAmount, ItemStack stackToCraft) {
		super(id, order, maxAmount);
		this.stackToCraft = stackToCraft;
	}

	/**
	 * Will be called when the event is triggered and the given player has the Quest with this active QuestStep.
	 *
	 * @param player QuestPlayer - the player who triggered the event
	 * @param event  Event - the event that was triggered
	 * @return boolean - player did a quest step
	 */
	@Override
	public boolean checkIfEventExecutesQuestStep(QuestPlayer player, Event event) {
		if (event instanceof CraftItemEvent craftItem) {
			return craftItem.getRecipe().getResult().equals(stackToCraft);
		}
		return false;
	}

	@Override
	public String getTaskName(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_CRAFT_NAME,
				List.of("${item}", "${amount}"),
				List.of(stackToCraft.getType().name().replace("_", "").toLowerCase(), getMaxAmount()));
	}

	@Override
	public String getTaskDescription(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_CRAFT_LORE);
	}
}
