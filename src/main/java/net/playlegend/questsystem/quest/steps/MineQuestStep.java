package net.playlegend.questsystem.quest.steps;

import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class MineQuestStep extends QuestStep {

	private final Material blockToBreak;

	public MineQuestStep(int id, int order, int maxAmount, Material blockToBreak) {
		super(id, order, maxAmount);
		this.blockToBreak = blockToBreak;
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
		if (event instanceof BlockBreakEvent blockBreak) {
			return blockBreak.getBlock().getType() == blockToBreak;
		}
		return false;
	}

	@Override
	public String getTaskName(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_MINE_NAME,
				List.of("${item}", "${amount}"),
				List.of(blockToBreak.name().replace("_", "").toLowerCase(), getMaxAmount()));
	}

	@Override
	public String getTaskDescription(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_MINE_LORE);
	}
}
