package net.playlegend.questsystem.quest.steps;

import net.playlegend.questsystem.QuestPlayer;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

public class MineQuestStep extends QuestStep {

	private final Material blockToBreak;

	public MineQuestStep(int id, int maxAmount, Material blockToBreak) {
		super(id, maxAmount);
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
	public boolean checkIfPlayerDoesQuestStep(QuestPlayer player, Event event) {
		if (event instanceof BlockBreakEvent blockBreak) {
			return blockBreak.getBlock().getType() == blockToBreak;
		}
		return false;
	}
}
