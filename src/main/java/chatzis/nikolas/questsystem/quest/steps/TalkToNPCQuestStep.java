package chatzis.nikolas.questsystem.quest.steps;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.util.LocationUtil;
import chatzis.nikolas.mc.npcsystem.NPC;
import chatzis.nikolas.questsystem.QuestSystem;
import chatzis.nikolas.questsystem.events.PlayerClickedOnQuestNPCEvent;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import chatzis.nikolas.questsystem.util.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public class TalkToNPCQuestStep extends QuestStep<UUID> {

	public TalkToNPCQuestStep(int id, int order, int maxAmount, UUID npcUUID) {
		super(QuestStepType.SPEAK, id, order, maxAmount, npcUUID);
	}

	/**
	 * Will be called when the event is triggered and the given player has the Quest with this active QuestStep.
	 *
	 * @param player QuestPlayer - the player who triggered the event
	 * @param event  Event - the event that was triggered
	 * @return int - 1 if player spoke to npc, 0 if not
	 */
	@Override
	public int checkIfEventExecutesQuestStep(QuestPlayer player, Event event) {
		if (event instanceof PlayerClickedOnQuestNPCEvent npcEvent && npcEvent.getNpcUUID().equals(getStepObject())) {
			String message = npcEvent.getMessages().get(player.getLanguage().getLanguageKey());
			if (message == null) {
				message = npcEvent.getMessages().get(QuestSystem.getInstance().getLanguageHandler().getFallbackLanguage().getLanguageKey());
				QuestSystem.getInstance().getLogger().log(Level.WARNING, "There was no npc-message for language {0} in speak-task {1} of quest {2}",
						new Object[]{player.getLanguage().getLanguageKey(), getId(), player.getActivePlayerQuest().map(a -> a.getQuest().name()).orElse("unknown")});
			}
			if (message != null) {
				player.openBook(new ItemBuilder(Material.WRITTEN_BOOK)
						.addPage(ColorUtil.convertToBlackColors(message).split(";")).craft());
			} else {
				QuestSystem.getInstance().getLogger().log(Level.WARNING, "There was no npc-message for default language {0} in speak-task {1} of quest {2}",
						new Object[]{QuestSystem.getInstance().getLanguageHandler().getFallbackLanguage().getLanguageKey(),
								getId(), player.getActivePlayerQuest().map(a -> a.getQuest().name()).orElse("unknown")});
			}
			return 1;
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
		return language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_ACTIVE_LINE,
				List.of("${order}", "${name}", "${amount}", "${maxamount}"),
				List.of(getOrder(), getNPCName().orElse("unknown"), currentAmount, getMaxAmount()));
	}

	/**
	 * Returns a one-liner that explains the quest step. E.g. "Mine 10 blocks"
	 *
	 * @param language Language - the language to translate
	 * @return String - one-liner that explains the step
	 */
	@Override
	public String getTaskLine(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_NORMAL_LINE,
				List.of("${order}", "${name}", "${maxamount}"),
				List.of(getOrder(), getNPCName().orElse("unknown"), getMaxAmount()));
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
		Optional<NPC> npcOptional = NPC.getByUUID(getStepObject());

		if (npcOptional.isPresent()) {
			NPC npc = npcOptional.get();
			String[] skin = npc.getSkin();
			String name = npc.getName();
			String location = LocationUtil.locationToString(npc.getLocation());

			return new ItemBuilder(UsefulItems.PLAYER_HEAD)
					.setSkin(skin[0], skin[1])
					.setName(ChatColor.YELLOW + name)
					.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_ACTIVE_LORE,
									List.of("${name}", "${amount}", "${maxamount}", "${location}"),
									List.of(name, currentAmount, getMaxAmount(), location))
							.split(";"))
					.addEnchantment(isStepComplete(currentAmount) ? Enchantment.ARROW_INFINITE : null, 1, true, false)
					.craft();
		} else {
			return new ItemBuilder(UsefulItems.SKELETON_SKULL)
					.setName("unknown")
					.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_ACTIVE_LORE,
									List.of("${name}", "${amount}", "${maxamount}", "${location}"),
									List.of("unknown", currentAmount, getMaxAmount(), "unknown"))
							.split(";"))
					.addEnchantment(isStepComplete(currentAmount) ? Enchantment.ARROW_INFINITE : null, 1, true, false)
					.craft();
		}
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
		Optional<NPC> npcOptional = NPC.getByUUID(getStepObject());

		if (npcOptional.isPresent()) {
			NPC npc = npcOptional.get();
			String[] skin = npc.getSkin();
			String name = npc.getName();
			String location = LocationUtil.locationToString(npc.getLocation());

			return new ItemBuilder(UsefulItems.PLAYER_HEAD)
					.setSkin(skin[0], skin[1])
					.setName(language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_NORMAL_NAME, "${name}", name))
					.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_NORMAL_LORE,
									List.of("${name}", "${maxamount}", "${location}"),
									List.of(name, getMaxAmount(), location))
							.split(";"))
					.craft();
		} else {
			return new ItemBuilder(UsefulItems.SKELETON_SKULL)
					.setName(language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_NORMAL_NAME, "${name}", "unknown"))
					.setLore(language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_NORMAL_LORE,
									List.of("${name}", "${maxamount}", "${location}"),
									List.of("unknown", getMaxAmount(), "unknown"))
							.split(";"))
					.craft();
		}
	}

	/**
	 * Returns a short description like "Mine 10 Stones"
	 *
	 * @param language Language - the language to translate
	 * @return String - the task name
	 */
	@Override
	public String getTaskName(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_NPC_NAME, "${name}", getNPCName().orElse("unknown"));
	}

	private Optional<String> getNPCName() {
		return NPC.getByUUID(getStepObject()).map(NPC::getName);
	}
}
