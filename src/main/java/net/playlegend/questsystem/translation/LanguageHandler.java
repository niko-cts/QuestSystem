package net.playlegend.questsystem.translation;

import net.playlegend.questsystem.QuestSystem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class LanguageHandler {

	private static final String TRANSLATIONS_DIR = "messages/";

	// languageKey: [translatedKeys, message]
	private final Map<String, Map<String, String>> translatedMessages;
	private final Set<Language> supportedLanguages;
	private final File baseFolder;

	public LanguageHandler() {
		this.translatedMessages = new HashMap<>();
		this.supportedLanguages = new HashSet<>();
		this.baseFolder = new File(QuestSystem.getInstance().getDataFolder(), TRANSLATIONS_DIR);

		// load saved messages
		this.loadStoredMessages();
	}

	/**
	 * Returns a cached translation of the translationKey
	 *
	 * @param languageKey    String - the language key
	 * @param translationKey String - the translation key
	 * @return String - the
	 */
	public String getRawTranslation(String languageKey, String translationKey) {
		Map<String, String> messagesWithKeys = this.translatedMessages.get(languageKey);

		if (messagesWithKeys != null) {
			String message = messagesWithKeys.get(translationKey);
			if (message != null)
				return message;

			QuestSystem.getInstance().getLogger().warning("Could not find translation for " + languageKey + " key is:" + translationKey);
		} else {
			QuestSystem.getInstance().getLogger().warning("Could not find language " + languageKey);
		}

		// fallback on english
		messagesWithKeys = this.translatedMessages.get(Locale.ENGLISH.getLanguage());

		return messagesWithKeys != null ?
				messagesWithKeys.getOrDefault(languageKey, languageKey) : languageKey;
	}

	public Set<Language> getSupportedLanguages() {
		return new HashSet<>(supportedLanguages);
	}

	/**
	 * Saves every key, which has not been set yet. Additionally, stores new keys in the map.
	 *
	 * @param locale Locale - the language to save the message in.
	 * @param messages Map<String, String> - every translation key with the translated message to save
	 */
    protected void cacheAndSaveMessagesToConfig(Locale locale, Map<String, String> messages) {
		this.translatedMessages.compute(locale.getLanguage(), (s, translationMap) -> {

			File languageFile = new File(baseFolder, locale.getLanguage());
			boolean saveConfig = false;

			if (!languageFile.exists()) {
				try {
					saveConfig = languageFile.createNewFile();
				} catch (IOException exception) {
					QuestSystem.getInstance().getLogger()
							.log(Level.WARNING, "Could not create language file for {0}: {1}",
									new String[]{locale.getLanguage(), exception.getMessage()});
					return messages;
				}
			}

			var cfg = YamlConfiguration.loadConfiguration(languageFile);
			if (translationMap == null) {
				translationMap = new HashMap<>();
			}

			for (Map.Entry<String, String> entry : messages.entrySet()) {
				if (!translationMap.containsKey(entry.getKey())) {
					translationMap.put(entry.getKey(), entry.getValue());

                    if (!cfg.contains(entry.getKey())) {
                        cfg.set(entry.getKey(), entry.getValue());
                        saveConfig = true;
                    }
				}
			}

			if (saveConfig) {
				try {
					cfg.save(languageFile);
				} catch (IOException exception) {
					QuestSystem.getInstance().getLogger()
							.log(Level.WARNING, "Could not save language messages for {0}: {1}",
									new String[]{locale.getLanguage(), exception.getMessage()});
				}
			}

			return translationMap;
		});
	}

	/**
	 * Loads the stored messages and put it in the map
	 */
	private void loadStoredMessages() {
		if (!baseFolder.exists() && !baseFolder.mkdirs()) {
			QuestSystem.getInstance().getLogger().warning("Could not create directory for translations!");
			return;
		}

		File[] files = baseFolder.listFiles();
		if (files == null) {
			QuestSystem.getInstance().getLogger().warning("Could not load default translations!");
			return;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				continue;
			}

			Locale language = Locale.forLanguageTag(file.getName());
			if (language == null) {
				QuestSystem.getInstance().getLogger().log(Level.WARNING,
						"File name {0} is a non valid language key. This will not be added to the language message.");
				continue;
			}
			supportedLanguages.add(new Language(language));
			Map<String, String> messages = new HashMap<>();

			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			ConfigurationSection section = cfg.getConfigurationSection("");
			if (section != null) {
				for (String key : section.getKeys(false)) {
					messages.put(key, cfg.getString(key));
				}
			}

			translatedMessages.put(language.getLanguage(), messages);
		}
	}


}
