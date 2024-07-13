package chatzis.nikolas.questsystem.commands;

import chatzis.nikolas.questsystem.commands.handler.APISubCommand;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import chatzis.nikolas.questsystem.util.QuestTimingsUtil;

import java.util.List;

public class QuestInfoCommand extends APISubCommand {

	public QuestInfoCommand() {
		super("info", TranslationKeys.QUESTS_COMMAND_QUEST_INFO_USAGE);
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		questPlayer.getActivePlayerQuest().ifPresentOrElse(
				activePlayerQuest -> questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_QUEST_INFO_DISPLAY,
						List.of("${name}", "${duration}", "${todo}"),
						List.of(activePlayerQuest.getQuest().name(),
								QuestTimingsUtil.convertSecondsToDHMS(questPlayer.getLanguage(), activePlayerQuest.getSecondsLeft()),
								activePlayerQuest.getNextUncompletedStep()
										.map(s -> s.getKey().getActiveTaskLine(questPlayer.getLanguage(), s.getValue()))
										.orElse("unknown")
						)),
				() -> questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_QUEST_INFO_NOACTIVE)
		);
	}
}
