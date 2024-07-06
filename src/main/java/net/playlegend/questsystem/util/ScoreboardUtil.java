package net.playlegend.questsystem.util;

import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.translation.TranslationMessageHolder;
import org.bukkit.ChatColor;

import java.util.Optional;

/**
 * This class is a helper class to update a player's scoreboard.
 *
 * @author Niko
 */
public class ScoreboardUtil {

	private ScoreboardUtil() {
		throw new UnsupportedOperationException("ScoreboardUtil cannot be instantiated");
	}

	private static final long TASK_LIMIT_TO_SHOW = 5;

	/**
	 * Updates the scoreboard from the player.
	 *
	 * @param questPlayer QuestPlayer - the quest player
	 */
	public static void updateScoreboard(QuestPlayer questPlayer) {
		Language language = questPlayer.getLanguage();
		APIScoreboard scoreboard = new APIScoreboard("quest_scoreboard")
				.setDisplayName(new TranslationMessageHolder(TranslationKeys.QUESTS_SCOREBOARD_DISPLAYNAME));

		Optional<ActivePlayerQuest> activeQuestOptional = questPlayer.getActivePlayerQuest();
		if (activeQuestOptional.isPresent()) {
			ActivePlayerQuest activeQuest = activeQuestOptional.get();
			scoreboard.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_ACTIVE_QUESTNAME)
					.addLine(APIScoreboard.START_COL + ChatColor.YELLOW + activeQuest.getQuest().name())
					.addEmptyLine();
			activeQuest.getNextUncompletedSteps().stream().limit(TASK_LIMIT_TO_SHOW).forEach(nextStep -> {
				scoreboard.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_ACTIVE_CURRENT_STEP);
				scoreboard.addLine(APIScoreboard.START_COL + ChatColor.YELLOW + nextStep.getActiveTaskLine(language, activeQuest.getStepAmount(nextStep)));
			});
			scoreboard.addEmptyLine()
					.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_ACTIVE_FINISH_AT)
					.addLine(APIScoreboard.START_COL + ChatColor.GRAY + QuestTimingsUtil.formatDateTime(activeQuest.getTimeLeft()));
		} else {
			scoreboard.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_NO_ACTIVE_1)
					.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_NO_ACTIVE_2);
		}

		scoreboard.addEmptyLine()
				.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_COINS)
				.addLine(APIScoreboard.START_COL + ChatColor.GOLD + questPlayer.getCoins())
				.showPlayer(questPlayer);
	}

}
