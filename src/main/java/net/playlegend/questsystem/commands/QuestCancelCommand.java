package net.playlegend.questsystem.commands;

import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;

import java.util.Optional;

public class QuestCancelCommand extends APISubCommand {
	public QuestCancelCommand() {
		super("cancel", "", TranslationKeys.QUESTS_COMMAND_QUEST_CANCEL_USAGE, 0, "abbrechen");
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		Optional<ActivePlayerQuest> activePlayerQuest = questPlayer.getActivePlayerQuest();
		if (activePlayerQuest.isPresent()) {
			questPlayer.cancelActiveQuest();
			questPlayer.sendClickableQuestMessage(TranslationKeys.QUESTS_COMMAND_QUEST_CANCEL_SUCCESS, activePlayerQuest.get().getQuest());
		} else {
			questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_QUEST_CANCEL_NOACTIVE);
		}
	}
}
