package net.playlegend.questsystem.quest.steps;

import lombok.Getter;
import net.playlegend.questsystem.QuestSystem;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;

/**
 * This enum holds every possible type of quest step.
 * Every type can instantiate a representative class, which holds information (usually materials, integers) the quest step needs to complete.
 *
 * @author Niko
 */
@Getter
public enum QuestStepType {

	CRAFT(CraftQuestStep.class, ItemStack.class), // base64 in database
	KILL(KillQuestStep.class, EntityType.class),
	MINE(MineQuestStep.class, Material.class),
	SPEAK(TalkToNPCQuestStep.class, UUID.class);

	private final Class<? extends QuestStep> questStepClass;
	private final Class<?> constructorParameter;

	QuestStepType(Class<? extends QuestStep> questStepClass, Class<?> constructorParameter) {
		this.questStepClass = questStepClass;
		this.constructorParameter = constructorParameter;
	}


	/**
	 * Gets the reward type based on the given class.
	 * Checks the {@link QuestStepType#questStepClass} if is instance of.
	 *
	 * @param questStepClass Class<? extends QuestStep> - the instance of the class
	 * @return RewardType - the instance of the reward
	 */
	public static QuestStepType getFromClass(Class<? extends QuestStep> questStepClass) {
		return Arrays.stream(values()).filter(questStepClass::isInstance).findFirst().orElseGet(() -> {
			QuestSystem.getInstance().getLogger().warning("QuestStep type of class is not implemented: " + questStepClass);
			return null;
		});
	}
}
