package net.playlegend.questsystem.quest.steps;

import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

/**
 * Will store an EntityType and amount.
 * This represents a kill quest:
 * The player should kill an entity.
 *
 * @author Niko
 */
public class KillQuestStep extends QuestStep {

	private final EntityType entityToKill;

	public KillQuestStep(int id, int order, int maxAmount, EntityType entityType) {
		super(id, order, maxAmount);
		this.entityToKill = entityType;
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
			return deathEvent.getEntityType() == entityToKill;
		}
		return false;
	}

	@Override
	public String getTaskName(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_KILL_NAME,
				List.of("${item}", "${amount}"),
				List.of(entityToKill.name().replace("_", "").toLowerCase(), getMaxAmount()));
	}

	@Override
	public String getTaskDescription(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_STEP_KILL_LORE);
	}
}
