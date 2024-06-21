package net.playlegend.questsystem.quest.steps;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.util.EntityTypeConverterUtil;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Will store an EntityType and amount.
 * This represents a kill quest:
 * The player should kill an entity.
 *
 * @author Niko
 */
public class KillQuestStep extends QuestStep<EntityType> {

	private final String entityName;

	public KillQuestStep(int id, int order, int maxAmount, EntityType entityType) {
		super(QuestStepType.KILL, id, order, maxAmount, entityType);
		this.entityName = EntityTypeConverterUtil.convertEntityToName(entityType);
	}

	/**
	 * Will be called when the event is triggered and the given player has the Quest with this active QuestStep.
	 *
	 * @param player QuestPlayer - the player who triggered the event
	 * @param event  Event - the event that was triggered
	 * @return boolean - player did a quest step
	 */
	@Override
	public boolean checkIfEventExecutesQuestStep(QuestPlayer player, Event event) {
		if (event instanceof EntityDeathEvent deathEvent) {
			return deathEvent.getEntityType() == getStepObject();
		}
		return false;
	}


	/**
	 * Returns a one-liner that previews the quest step. E.g. "Mine block"
	 *
	 * @param language Language - the language to translate in
	 * @return String - the quest step explanation
	 */
	@Override
	public String getActiveTaskLine(Language language, int currentAmount) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_KILL_ACTIVE_LINE,
				List.of("${entity}", "${amount}", "${maxamount}"),
				List.of(entityName, currentAmount, getMaxAmount()));
	}

	/**
	 * Returns a one-liner that explains the quest step. E.g. "Mine 10 blocks"
	 *
	 * @param language Language - the language to translate
	 * @return String - one-liner that explains the step
	 */
	@Override
	public String getTaskLine(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_KILL_NORMAL_LINE,
				List.of("${entity}", "${maxamount}"),
				List.of(entityName, getMaxAmount()));
	}

	/**
	 * Returns an ItemStack that explains the quest step.
	 * E.g., new ItemStack(Material.STONE).setLore("Mine this block 10 times")
	 *
	 * @param language Language - the language to translate in
	 * @return ItemStack - the item explaining the step
	 */
	@Override
	public ItemStack getActiveTask(Language language, int currentAmount) {
		return new ItemBuilder(getStepObject())
				.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_KILL_ACTIVE_LORE,
								List.of("${entity}", "${amount}", "${maxamount}"),
								List.of(entityName, currentAmount, getMaxAmount()))
						.split(";"))
				.setAmount(Math.max(1, Math.min(currentAmount, 64)))
				.craft();
	}

	/**
	 * Returns an ItemStack that explains the quest step.
	 * E.g., new ItemStack(Material.STONE).setLore("Mine this block 10 times")
	 *
	 * @param language Language - the language to translate in
	 * @return ItemStack - the item explaining the step
	 */
	@Override
	public ItemStack getTaskItem(Language language) {
		return new ItemBuilder(getStepObject())
				.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_KILL_NORMAL_LORE,
								List.of("${entity}", "${maxamount}"),
								List.of(entityName, getMaxAmount()))
						.split(";"))
				.setAmount(Math.max(1, Math.min(getMaxAmount(), 64)))
				.craft();
	}
}
