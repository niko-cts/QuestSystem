package chatzis.nikolas.questsystem.translation;

import chatzis.nikolas.questsystem.QuestSystem;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Abstract class to add default messages for each language.
 * @author Niko
 */
public abstract class AbstractDefaultMessages {

    // translationKey, translated message
    private final Map<String, String> defaultMessage;
    private final Locale locale;

    protected AbstractDefaultMessages(Locale locale) {
        this.defaultMessage = new HashMap<>();
        this.locale = locale;
    }


    /**
     * Adds the string to the hashmap. Does not save the key!
     * @param key String - translation key
     * @param message - String translated message
     */
    protected void add(String key, String message) {
        this.defaultMessage.put(key, message);
    }

    /**
     * This should eb called after every default message was added. <br>
     * This method will call {@link LanguageHandler#cacheAndSaveMessagesToConfig(Locale, Map)}
     * with the added values.
     */
    protected void save() {
        QuestSystem.getInstance().getLanguageHandler().cacheAndSaveMessagesToConfig(
                this.locale, getDefaultMessage()
        );
    }

    public Map<String, String> getDefaultMessage() {
        return new HashMap<>(defaultMessage);
    }
}
