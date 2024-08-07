package chatzis.nikolas.questsystem.commands;

import chatzis.nikolas.questsystem.commands.handler.APISubCommand;
import chatzis.nikolas.questsystem.player.ActivePlayerQuest;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.TranslationKeys;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class QuestCancelCommand extends APISubCommand {

	private final Set<UUID> confirm;

	public QuestCancelCommand() {
		super("cancel", "", TranslationKeys.QUESTS_COMMAND_QUEST_CANCEL_USAGE, 0, "abbrechen");
		this.confirm = new HashSet<>();
	}


	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		Optional<ActivePlayerQuest> activePlayerQuest = questPlayer.getActivePlayerQuest();
		if (activePlayerQuest.isPresent()) {
			if (confirm.contains(questPlayer.getUniqueId())) {
				questPlayer.cancelActiveQuest();
				questPlayer.sendClickableQuestMessage(TranslationKeys.QUESTS_COMMAND_QUEST_CANCEL_SUCCESS, activePlayerQuest.get().getQuest());
				confirm.remove(questPlayer.getUniqueId());
			} else {
				questPlayer.sendClickableMessage(TranslationKeys.QUESTS_COMMAND_QUEST_CANCEL_CONFIRM_TEXT, TranslationKeys.QUESTS_COMMAND_QUEST_CANCEL_CONFIRM_HOVER, "/quest cancel");
				confirm.add(questPlayer.getUniqueId());
			}
		} else {

			questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_QUEST_CANCEL_NOACTIVE);
		}
	}
}
