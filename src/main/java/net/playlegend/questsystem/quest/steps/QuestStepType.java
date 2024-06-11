package net.playlegend.questsystem.quest.steps;

import chatzis.nikolas.mc.npcsystem.event.PlayerInteractAtNPCEvent;
import lombok.Getter;
import net.playlegend.questsystem.QuestSystem;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.UUID;

/**
 * This enum holds every possible type of quest step.
 * Every type can instantiate a representative class, which holds information (usually materials, integers) the quest step needs to complete.
 *
 * @author Niko
 */
public enum QuestStepType {

	CRAFT(CraftItemEvent.class, CraftQuestStep.class, ItemStack.class),
	KILL(EntityDeathEvent.class, KillQuestStep.class, EntityType.class),
	MINE(BlockBreakEvent.class, MineQuestStep.class, Material.class),
	SPEAK(PlayerInteractAtNPCEvent.class, TalkToNPCQuestStep.class, UUID.class);

	private final Class<? extends QuestStep> questStepClass;
	@Getter
	private final Class<?> stepObject;

	// TODO impl trigger event
	QuestStepType(Class<? extends Event> toTriggerEvent, Class<? extends QuestStep> questStepClass, Class<?> stepObject) {
		this.questStepClass = questStepClass;
		this.stepObject = stepObject;
	}

	/**
	 * Instantiates a {@link QuestStep} class based on given parameters.
	 * The parameters should be: id of the step (as int), amount that takes to complete the step (as int), object to complete the step (must match the {@link QuestStepType#stepObject})
	 *
	 * @param parameters Object[] - the parameters the quest step class need. This should match the {@link QuestStepType#getConstructorParameters()}
	 * @return {@link QuestStep} - An instance of {@link QuestStepType#questStepClass}
	 * @throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException - When wrong parameters were inputted
	 */
	public QuestStep getQuestStepInstance(Object... parameters) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		return questStepClass.getConstructor(getConstructorParameters()).newInstance(parameters);
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

	private Class<?>[] getConstructorParameters() {
		return new Class<?>[]{int.class, int.class, stepObject};
	}

}
