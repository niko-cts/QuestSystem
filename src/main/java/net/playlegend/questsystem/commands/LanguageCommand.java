package net.playlegend.questsystem.commands;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.commands.handler.APICommand;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;

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
