package chatzis.nikolas.questsystem.commands;

import chatzis.nikolas.questsystem.QuestSystem;
import chatzis.nikolas.questsystem.commands.handler.APICommand;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;

import java.util.Optional;
import java.util.stream.Collectors;

public class LanguageCommand extends APICommand {

	public LanguageCommand() {
		super("language", "", TranslationKeys.QUESTS_COMMAND_LANGUAGE_USAGE, "sprache");
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] args) {
		if (args.length == 0) {
			sendCommandUsage(questPlayer);
			return;
		}
		String name = args[0].toLowerCase();
		Optional<Language> language = QuestSystem.getInstance().getLanguageHandler().getLanguageByName(name);
		if (language.isPresent()) {
			questPlayer.setLanguage(language.get());
			questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_LANGUAGE_UPDATED);
		} else {
			questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_LANGUAGE_ILLEGAL, "${languages}",
					QuestSystem.getInstance().getLanguageHandler().getSupportedLanguages()
							.stream().map(Language::getName).collect(Collectors.joining(", ")));
		}
	}
}
