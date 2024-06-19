package net.playlegend.questsystem.quest.steps;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.util.LocationUtil;
import chatzis.nikolas.mc.npcsystem.NPC;
import net.playlegend.questsystem.events.PlayerClickedOnQuestNPCEvent;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TalkToNPCQuestStep extends QuestStep {

	private final UUID npcUUID;

	public TalkToNPCQuestStep(int id, int order, int maxAmount, UUID npcUUID) {
		super(id, order, maxAmount);
		this.npcUUID = npcUUID;
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
		return event instanceof PlayerClickedOnQuestNPCEvent npcEvent && npcEvent.getNpcUUID().equals(npcUUID);
	}


	/**
	 * Returns a one-liner that previews the quest step. E.g. "Mine block"
	 *
	 * @param language Language - the language to translate in
	 * @return String - the quest step explanation
	 */
	@Override
	public String getActiveTaskLine(Language language, int currentAmount) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_PREVIEW,
				List.of("${name}", "${amount}", "${maxamount}"),
				List.of(getNPCName().orElse("unknown"), currentAmount, getMaxAmount()));
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
		Optional<NPC> npcOptional = NPC.getByUUID(npcUUID);

		if (npcOptional.isPresent()) {
			NPC npc = npcOptional.get();
			String[] skin = npc.getSkin();
			String name = npc.getName();
			String location = LocationUtil.locationToString(npc.getLocation());

			return new ItemBuilder(UsefulItems.PLAYER_HEAD)
					.setSkin(skin[0], skin[1])
					.setName(language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_LORE, "${name}", name))
					.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_LORE,
									List.of("${name}", "${amount}", "${maxamount}", "${location}"),
									List.of(name, currentAmount, getMaxAmount(), location))
							.split(";"))
					.addEnchantment(isStepComplete(currentAmount) ? Enchantment.ARROW_INFINITE : null, 1, true, false)
					.craft();
		} else {
			return new ItemBuilder(UsefulItems.SKELETON_SKULL)
					.setName(language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_LORE, "${name}", "unknown"))
					.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_LORE,
									List.of("${name}", "${amount}", "${maxamount}", "${location}"),
									List.of("unknown", currentAmount, getMaxAmount(), "unknown"))
							.split(";"))
					.addEnchantment(isStepComplete(currentAmount) ? Enchantment.ARROW_INFINITE : null, 1, true, false)
					.craft();
		}
	}

	private Optional<String> getNPCName() {
		return NPC.getByUUID(npcUUID).map(NPC::getName);
	}
}
