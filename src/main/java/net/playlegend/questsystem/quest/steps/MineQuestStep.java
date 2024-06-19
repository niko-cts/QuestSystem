package net.playlegend.questsystem.quest.steps;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.util.MaterialConverterUtil;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MineQuestStep extends QuestStep {

	private final Material blockToBreak;
	private final String materialName;

	public MineQuestStep(int id, int order, int maxAmount, Material blockToBreak) {
		super(id, order, maxAmount);
		this.blockToBreak = blockToBreak;
		this.materialName = MaterialConverterUtil.convertMaterialToName(blockToBreak);
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
		if (event instanceof BlockBreakEvent blockBreak) {
			return blockBreak.getBlock().getType() == blockToBreak;
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
		return language.translateMessage(TranslationKeys.QUESTS_STEP_MINE_PREVIEW,
				List.of("${item}", "${amount}", "${maxamount}"),
				List.of(materialName, currentAmount, getMaxAmount()));
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
		return new ItemBuilder(blockToBreak)
				.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_MINE_LORE,
								List.of("${item}", "${amount}", "${maxamount}"),
								List.of(materialName, currentAmount, getMaxAmount()))
						.split(";"))
				.setAmount(Math.max(1, Math.min(currentAmount, 64)))
				.addEnchantment(isStepComplete(currentAmount) ? Enchantment.ARROW_INFINITE : null, 1, true, false)
				.craft();
	}
}
