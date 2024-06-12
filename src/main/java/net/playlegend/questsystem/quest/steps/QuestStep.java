package net.playlegend.questsystem.quest.steps;

import lombok.Data;
import net.playlegend.questsystem.player.QuestPlayer;
import org.bukkit.event.Event;

@Data
public abstract class QuestStep {

	private final int id;
	private final int maxAmount;

	public boolean isStepComplete(int currentAmount) {
		return currentAmount >= maxAmount;
	}

	/**
	 * Will be called when the event is triggered and the given player has the Quest with this active QuestStep.
	 *
	 * @param player QuestPlayer - the player who triggered the event
	 * @param event Event - the event that was triggered
	 * @return boolean - player did a quest step
	 */
	public abstract boolean checkIfPlayerDoesQuestStep(QuestPlayer player, Event event);

}

