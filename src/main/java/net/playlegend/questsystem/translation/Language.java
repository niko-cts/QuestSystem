package net.playlegend.questsystem.translation;

import lombok.Getter;
import lombok.NonNull;
import net.playlegend.questsystem.QuestSystem;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Locale;

@Getter
public class Language {

    private final String languageKey;
    private final String name;

    public Language(@NonNull Locale locale) {
        this.languageKey = locale.getLanguage();
        this.name = locale.getDisplayLanguage(locale);
    }

    /**
     * Gets the cached message from the translation key, changes the color.
     *
     * @param translationKey String - the translation key which will be converted
     * @return String - the translated and formatted message.
     */
    public String translateMessage(String translationKey) {
        return ChatColor.translateAlternateColorCodes('&',
                QuestSystem.getInstance().getLanguageHandler().getRawTranslation(
                        languageKey, translationKey
                ));
    }

    /**
     * Gets the cached message from the translation key, changes the color format and replaces a placeholder in the message.
     *
     * @param translationKey String - the translation key which will be converted
     * @param placeholder    String - the placeholder the replacement will be at
     * @param replacement    String - the replacement of the placeholder
     * @return String - the translated and formatted message.
     */
    public String translateMessage(String translationKey, String placeholder, String replacement) {
        return translateMessage(translationKey).replace(placeholder, replacement);
    }


    /**
     * Gets the cached message from the translation key, changes the color format and replaces a placeholder in the message.
     *
     * @param translationKey String - the translation key which will be converted
     * @param placeholder    List<String> - the placeholders the replacement will be at
     * @param replacement    List<String> - the replacement of the placeholder
     * @return String - the translated and formatted message.
     */
    public String translateMessage(String translationKey, List<String> placeholder, List<String> replacement) {
        if (placeholder.size() != replacement.size()) {
            throw new IllegalStateException("Placeholder and replacements of the message are not the same size");
        }

        String messsage = translateMessage(translationKey);
        for (int i = 0; i < placeholder.size(); i++) {
            messsage = messsage.replace(placeholder.get(i), replacement.get(i));
        }

        return messsage;
    }

}
