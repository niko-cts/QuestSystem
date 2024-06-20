package net.playlegend.questsystem.quest.steps;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.util.MaterialConverterUtil;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CraftQuestStep extends QuestStep {

	private final ItemStack stackToCraft;
	private final String materialName;

	public CraftQuestStep(int id, int order, int maxAmount, ItemStack stackToCraft) {
		super(id, order, maxAmount);
		this.stackToCraft = stackToCraft;
		this.materialName = MaterialConverterUtil.convertMaterialToName(stackToCraft.getType());
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
		return event instanceof CraftItemEvent craftItem && craftItem.getRecipe().getResult().equals(stackToCraft);
	}

	/**
	 * Returns a one-liner that previews the quest step. E.g. "Mine block"
	 *
	 * @param language Language - the language to translate in
	 * @return String - the quest step explanation
	 */
	@Override
	public String getActiveTaskLine(Language language, int currentAmount) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_CRAFT_ACTIVE_LINE,
				List.of("${item}", "${amount}", "${maxamount}"),
				List.of(materialName, currentAmount, getMaxAmount()));
	}

	/**
	 * Returns a one-liner that explains the quest step. E.g. "Mine 10 blocks"
	 *
	 * @param language Language - the language to translate
	 * @return String - one-liner that explains the step
	 */
	@Override
	public String getTaskLine(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_CRAFT_NORMAL_LINE,
				List.of("${item}", "${maxamount}"),
				List.of(materialName, getMaxAmount()));
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
		return new ItemBuilder(stackToCraft)
				.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_CRAFT_ACTIVE_LORE,
								List.of("${item}", "${amount}", "${maxamount}"),
								List.of(materialName, currentAmount, getMaxAmount()))
						.split(";"))
				.addEnchantment(isStepComplete(currentAmount) ? Enchantment.ARROW_INFINITE : null, 1, true, false)
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
		return new ItemBuilder(stackToCraft)
				.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_CRAFT_NORMAL_LORE,
								List.of("${item}", "${maxamount}"),
								List.of(materialName, getMaxAmount()))
						.split(";"))
				.setAmount(Math.max(1, Math.min(getMaxAmount(), 64)))
				.craft();;
	}
}
