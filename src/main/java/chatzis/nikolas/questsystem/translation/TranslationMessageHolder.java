package chatzis.nikolas.questsystem.translation;

import lombok.Getter;

import java.util.List;

@Getter
public class TranslationMessageHolder {

	private final String translationKey;
	private final List<String> placeholders;
	private final List<Object> replacements;

	public TranslationMessageHolder(String translationKey, List<String> placeholders, List<Object> replacements) {
		this.translationKey = translationKey;
		this.placeholders = placeholders;
		this.replacements = replacements;
	}

	public TranslationMessageHolder(String translationKey) {
		this(translationKey, List.of(), List.of());
	}

	public String translate(Language language) {
		return language.translateMessage(translationKey, placeholders, replacements);
	}
}
