package net.playlegend.questsystem.npc;

import chatzis.nikolas.mc.nikoapi.util.LocationUtil;
import chatzis.nikolas.mc.npcsystem.NPC;
import chatzis.nikolas.mc.npcsystem.NPCSkin;
import lombok.Getter;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.events.PlayerClickedOnQuestNPCEvent;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.quest.steps.QuestStepType;
import org.bukkit.ChatColor;
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


	@Override
	public String toString() {
		StringBuilder questAndStepsNPCIsIn = new StringBuilder();
		for (Quest quest : QuestSystem.getInstance().getQuestManager().getQuests()) {
			for (QuestStep<?> step : quest.completionSteps()) {
				if (step.getType() == QuestStepType.SPEAK && step.getStepObject() instanceof UUID stepUUID && getUniqueID().equals(stepUUID)) {
					if (!questAndStepsNPCIsIn.isEmpty())
						questAndStepsNPCIsIn.append(", ");
					questAndStepsNPCIsIn.append("Q: ").append(quest.name()).append(ChatColor.GRAY).append(" - Task-ID: ").append(step.getId());
				}
			}
		}

		return ChatColor.RED + "[" + ChatColor.GRAY + getName() +
		       ChatColor.GRAY + " in " + ChatColor.BLUE + "{" + ChatColor.GRAY + questAndStepsNPCIsIn + ChatColor.BLUE + "} " + ChatColor.GRAY + "at " + LocationUtil.locationToString(getLocation()) + ChatColor.RED + "]" + ChatColor.GRAY;
	}

}
