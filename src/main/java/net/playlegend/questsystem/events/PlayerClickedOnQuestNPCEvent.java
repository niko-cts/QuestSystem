package net.playlegend.questsystem.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerClickedOnQuestNPCEvent extends Event {

    private final Player player;
    private final UUID npcUUID;
    private final Map<String, String> messages;

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    public HandlerList getHandlers() {
        return handlerList;
    }

}
