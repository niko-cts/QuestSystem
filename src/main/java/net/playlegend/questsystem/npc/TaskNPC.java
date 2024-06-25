package net.playlegend.questsystem.npc;

import chatzis.nikolas.mc.nikoapi.util.LocationUtil;
import chatzis.nikolas.mc.npcsystem.NPC;
import chatzis.nikolas.mc.npcsystem.NPCSkin;
import lombok.Getter;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.events.PlayerClickedOnQuestNPCEvent;
import net.playlegend.questsystem.quest.steps.QuestStepType;
import org.bukkit.Location;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class TaskNPC extends NPC {

	private final Map<String, String> messages;
	private final String questAndStepsNPCIsIn;

	public TaskNPC(UUID uuid, String name, Location location, NPCSkin skin, Set<UUID> visibleTo, boolean withAi, Map<String, String> messages) {
		super(uuid, name, location, skin, visibleTo, withAi);
		this.messages = messages;

		click(event -> QuestSystem.getInstance().getServer().getPluginManager().callEvent(
				new PlayerClickedOnQuestNPCEvent(event.getPlayer().getPlayer(), event.getNPC().getUniqueID(), messages)
		));
		questAndStepsNPCIsIn = QuestSystem.getInstance().getQuestManager().getQuests().stream()
				.filter(q -> q.completionSteps().stream().anyMatch(s -> s.getType() == QuestStepType.SPEAK
				                                                        && s.getStepObject() instanceof UUID sUUID && sUUID.equals(getUniqueID())))
				.map(q -> q.name() + ": (" + q.completionSteps().stream().map(s -> s.getId() + "").collect(Collectors.joining(", ")) + ")").collect(Collectors.joining(", "));
	}


	@Override
	public String toString() {
		return "[" + getName() + " in Quests {" + questAndStepsNPCIsIn + "} at " + LocationUtil.locationToString(getLocation()) + "]";
	}

}
