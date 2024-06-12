package net.playlegend.questsystem.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerClickedOnQuestNPCEvent extends Event {

    private final Player player;
    private final UUID npcUUID;

    /**
     * @return
     */
    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
