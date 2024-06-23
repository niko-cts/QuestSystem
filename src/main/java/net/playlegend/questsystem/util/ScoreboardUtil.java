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

	/**
	 * Updates the scoreboard from the player.
	 *
	 * @param questPlayer QuestPlayer - the quest player
	 */
	public static void updateScoreboard(QuestPlayer questPlayer) {
		Language language = questPlayer.getLanguage();
		APIScoreboard scoreboard = new APIScoreboard("quest_scoreboard");
		scoreboard.setDisplayName(new TranslationMessageHolder(TranslationKeys.QUESTS_SCOREBOARD_DISPLAYNAME));

		Optional<ActivePlayerQuest> activeQuestOptional = questPlayer.getActivePlayerQuest();
		if (activeQuestOptional.isPresent()) {
			ActivePlayerQuest activeQuest = activeQuestOptional.get();
			scoreboard.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_ACTIVE_QUESTNAME);
			scoreboard.addLine(APIScoreboard.START_COL + " " + activeQuest.getQuest().name());
			scoreboard.addEmptyLine();
			activeQuest.getNextUncompletedStep().ifPresent(nextStep -> {
				scoreboard.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_ACTIVE_CURRENT_STEP);
				scoreboard.addLine(APIScoreboard.START_COL + " " + nextStep.getKey().getActiveTaskLine(language, nextStep.getValue()));
			});
			scoreboard.addEmptyLine();
			scoreboard.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_ACTIVE_FINISH_AT);
			scoreboard.addLine(APIScoreboard.START_COL + " " + QuestTimingsUtil.formatDateTime(activeQuest.getTimeLeft()));
		} else {
			scoreboard.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_NO_ACTIVE_1);
			scoreboard.addEmptyLine();
			scoreboard.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_NO_ACTIVE_2);
		}

		scoreboard.addEmptyLine();
		scoreboard.addTranslatableLine(TranslationKeys.QUESTS_SCOREBOARD_COINS);
		scoreboard.addLine(APIScoreboard.START_COL + ChatColor.GOLD + " " + questPlayer.getCoins());

		scoreboard.showPlayer(questPlayer);
	}

}
