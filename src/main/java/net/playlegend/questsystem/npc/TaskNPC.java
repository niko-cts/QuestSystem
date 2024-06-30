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
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a task NPC.
 * This NPC should be clicked to complete {@link net.playlegend.questsystem.quest.steps.TalkToNPCQuestStep}
 *
 * @author Niko
 */
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


	public String toString(Language language) {
		StringBuilder questAndStepsNPCIsIn = new StringBuilder();
		for (Quest quest : QuestSystem.getInstance().getQuestManager().getQuests()) {
			for (QuestStep<?> step : quest.completionSteps()) {
				if (step.getType() == QuestStepType.SPEAK && step.getStepObject() instanceof UUID stepUUID && getUniqueID().equals(stepUUID)) {
					if (!questAndStepsNPCIsIn.isEmpty())
						questAndStepsNPCIsIn.append(", ");
					questAndStepsNPCIsIn.append(language.translateMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_LIST_TASK_ELEMENT_STEP,
							List.of("${questname}", "${stepid}"),
							List.of(quest.name(), step.getId())));
				}
			}
		}

		System.out.println(getUniqueID());
		return language.translateMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_LIST_TASK_ELEMENT,
				List.of("${uuid]", "${name}", "${location}", "${queststeps}"),
				List.of(getUniqueID(), getName(), LocationUtil.locationToString(getLocation()), questAndStepsNPCIsIn));
	}
}
