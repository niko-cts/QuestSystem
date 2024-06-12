package net.playlegend.questsystem.quest.steps;

import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.events.PlayerClickedOnQuestNPCEvent;
import org.bukkit.event.Event;

import java.util.UUID;

public class TalkToNPCQuestStep extends QuestStep {

	private final UUID npcUUID;

	public TalkToNPCQuestStep(int id, int maxAmount, UUID npcUUID) {
		super(id, maxAmount);
		this.npcUUID = npcUUID;
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
		if (event instanceof PlayerClickedOnQuestNPCEvent npcEvent) {
			return npcEvent.getNpcUUID().equals(npcUUID);
		}
		return false;
	}
}
