package chatzis.nikolas.questsystem.quest.steps;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.util.MaterialConverterUtil;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MineQuestStep extends QuestStep<Material> {

	private final String materialName;
	private final Map<UUID, Set<Location>> minedBlocks;

	public MineQuestStep(int id, int order, int maxAmount, Material blockToBreak) {
		super(QuestStepType.MINE, id, order, maxAmount, blockToBreak);
		this.materialName = MaterialConverterUtil.convertMaterialToName(blockToBreak);
		this.minedBlocks = new HashMap<>();
	}

	/**
	 * Will be called when the event is triggered and the given player has the Quest with this active QuestStep.
	 *
	 * @param player QuestPlayer - the player who triggered the event
	 * @param event  Event - the event that was triggered
	 * @return int - the amount the player did with this event
	 */
	@Override
	public int checkIfEventExecutesQuestStep(QuestPlayer player, Event event) {
		if (event instanceof BlockBreakEvent blockBreak) {
			if (blockBreak.getBlock().getType() == getStepObject()) {
				Set<Location> minedBlocks = this.minedBlocks.compute(player.getUniqueId(), (uuid, locations) -> {
					if (locations == null)
						locations = new HashSet<>();
					return locations;
				});
				if (minedBlocks.contains(blockBreak.getBlock().getLocation())) {
					player.sendMessage(TranslationKeys.QUESTS_STEP_MINE_ALREADY_MINED);
					return 0;
				}

				minedBlocks.add(blockBreak.getBlock().getLocation());
				return 1;
			}
		}
		return 0;
	}

	/**
	 * Returns a one-liner that previews the quest step. E.g. "Mine block"
	 *
	 * @param language Language - the language to translate in
	 * @return String - the quest step explanation
	 */
	@Override
	public String getActiveTaskLine(Language language, int currentAmount) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_MINE_ACTIVE_LINE,
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
		return language.translateMessage(TranslationKeys.QUESTS_STEP_MINE_NORMAL_LINE,
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
				.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_MINE_ACTIVE_LORE,
								List.of("${item}", "${amount}", "${maxamount}"),
								List.of(materialName, currentAmount, getMaxAmount()))
						.split(";"))
				.setAmount(Math.max(1, Math.min(currentAmount, 64)))
				.addEnchantment(isStepComplete(currentAmount) ? Enchantment.ARROW_INFINITE : null, 1, true, false)
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
				.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_MINE_NORMAL_LORE,
								List.of("${item}", "${maxamount}"),
								List.of(materialName, getMaxAmount()))
						.split(";"))
				.setAmount(Math.max(1, Math.min(getMaxAmount(), 64)))
				.craft();
	}

	/**
	 * Returns a short description like "Mine 10 Stones"
	 *
	 * @param language Language - the language to translate
	 * @return String - the task name
	 */
	@Override
	public String getTaskName(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_MINE_NAME,
				Arrays.asList("${item}", "${maxamount}"), Arrays.asList(materialName, getMaxAmount()));
	}
}
