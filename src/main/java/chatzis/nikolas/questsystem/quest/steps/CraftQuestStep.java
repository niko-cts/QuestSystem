package chatzis.nikolas.questsystem.quest.steps;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.util.MaterialConverterUtil;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CraftQuestStep extends QuestStep<ItemStack> {

	private final String materialName;

	public CraftQuestStep(int id, int order, int maxAmount, ItemStack stackToCraft) {
		super(QuestStepType.CRAFT, id, order, maxAmount, stackToCraft);
		this.materialName = MaterialConverterUtil.convertMaterialToName(stackToCraft.getType());
	}

	/**
	 * Will be called when the event is triggered and the given player has the Quest with this active QuestStep.
	 *
	 * @param player QuestPlayer - the player who triggered the event
	 * @param event  Event - the event that was triggered
	 * @return int - the amount the player crafted the item
	 */
	@Override
	public int checkIfEventExecutesQuestStep(QuestPlayer player, Event event) {
		if (event instanceof CraftItemEvent crafEvent) {
			ItemStack result = getCraftedItem(crafEvent);
			if (result.equals(getStepObject()))
				return result.getAmount();

			if (result.getAmount() != getStepObject().getAmount() && result.getType() == getStepObject().getType()) {
				// extra check,if player crafted another amount of task object
				ItemStack taskItemClone = getStepObject().clone();
				taskItemClone.setAmount(result.getAmount());
				return result.equals(taskItemClone) ? result.getAmount() : 0;
			}
		}
		return 0;
	}

	/**
	 * Returns the real item stack of the crafted item with shift click
	 * <a href="https://www.spigotmc.org/threads/get-accurate-crafting-result-from-shift-clicking.446520/">Source: spigot resource</a>
	 *
	 * @param evt CraftItemEvent - the event
	 * @return ItemStack - the item stack with accurate amount
	 */
	private ItemStack getCraftedItem(CraftItemEvent evt) {
		if (evt.isShiftClick()) {
			final ItemStack recipeResult = evt.getRecipe().getResult();
			final int resultAmt = recipeResult.getAmount(); // Bread = 1, Cookie = 8, etc.
			int leastIngredient = -1;
			for (ItemStack item : evt.getInventory().getMatrix()) {
				if (item != null && !item.getType().equals(Material.AIR)) {
					final int re = item.getAmount() * resultAmt;
					if (leastIngredient == -1 || re < leastIngredient) {
						leastIngredient = item.getAmount() * resultAmt;
					}
				}
			}
			ItemStack itemStack = new ItemStack(recipeResult);
			itemStack.setAmount(leastIngredient);
			return itemStack;
		}
		return evt.getCurrentItem();
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
				List.of("${order}", "${item}", "${amount}", "${maxamount}"),
				List.of(getOrder(), materialName, currentAmount, getMaxAmount()));
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
				List.of("${order}", "${item}", "${maxamount}"),
				List.of(getOrder(), materialName, getMaxAmount()));
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
		return new ItemBuilder(getStepObject())
				.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_CRAFT_NORMAL_LORE,
								List.of("${item}", "${maxamount}"),
								List.of(materialName, getMaxAmount()))
						.split(";"))
				.setAmount(Math.max(1, Math.min(getMaxAmount(), 64)))
				.craft();
	}

	@Override
	public String getTaskName(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_CRAFT_NAME,
				Arrays.asList("${item}", "${maxamount}"), Arrays.asList(materialName, getMaxAmount()));
	}
}
