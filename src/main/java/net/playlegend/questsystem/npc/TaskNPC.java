package net.playlegend.questsystem.npc;

import chatzis.nikolas.mc.npcsystem.NPC;
import chatzis.nikolas.mc.npcsystem.NPCSkin;
import lombok.Getter;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.events.PlayerClickedOnQuestNPCEvent;
import org.bukkit.Location;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
public class TaskNPC extends NPC {

	private final Map<String, String> messages;

	public TaskNPC(UUID uuid, String name, Location location, NPCSkin skin, Set<UUID> visibleTo, boolean withAi, Map<String, String> messages) {
		super(uuid, name, location, skin, visibleTo, withAi);
		this.messages = messages;

		click(event -> QuestSystem.getInstance().getServer().getPluginManager().callEvent(
				new PlayerClickedOnQuestNPCEvent(event.getPlayer().getPlayer(), event.getNPC().getUniqueID(), messages)
		));
	}

}
