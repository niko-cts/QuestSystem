package net.playlegend.questsystem.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.playlegend.questsystem.player.QuestPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class PlayerQuestUpdateEvent extends Event {

	private final QuestPlayer player;
	private final QuestUpdateType type;

	@Getter
	private static final HandlerList handlerList = new HandlerList();

	public HandlerList getHandlers() {
		return handlerList;
	}

	public enum QuestUpdateType {
		JOINED,
		UPDATE_STATS,
		NEW_QUEST,
		QUEST_ENDED,
		STEP,
		FIND,
		COMPLETED;

		public boolean taskNPCNeedsUpdate() {
			return this == JOINED ||
			       this == NEW_QUEST ||
			       this == QUEST_ENDED ||
			       this == STEP ||
			       this == COMPLETED;
		}

		public boolean needsFindUpdate() {
			return this == JOINED || this == FIND;
		}
	}

}
