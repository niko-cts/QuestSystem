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

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    public HandlerList getHandlers() {
        return handlerList;
    }

}
