package net.playlegend.questsystem.commands;

import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.QuestTimingsUtil;

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
								activePlayerQuest.getNextUncompletedSteps())),
				() -> questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_QUEST_INFO_NOACTIVE)
		);
	}
}
