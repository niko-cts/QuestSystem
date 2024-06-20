package net.playlegend.questsystem.quest.builder;

import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Consumer;

import java.util.UUID;
import java.util.function.Function;

/**
 * Helper class to insert something in the anvil and return the result.
 *
 * @author Niko
 */
public class AnvilInsertionHelper {

    private AnvilInsertionHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * Opens an anvil and waits for insertion (rename). Then calls acceptable function. If a String is returned, will translate it to the error message.
     *
     * @param language            Language - to translate the messages in
     * @param descriptionTransKey String - description will be shown in the lore.
     * @param stringAcceptable    Function<String, String> - Applies the input. If null is returned insertion was successful, else returned String is error message key.
     */
    private static void insertStringInAnvilMenu(Language language,
                                                  String descriptionTransKey,
                                                  String replacement,
                                                  Function<String, String> stringAcceptable) {
        String description = language.translateMessage(descriptionTransKey, "${type}", replacement);
        // todo anvil

    }

    /**
     * Opens an insertStringInAnvilMenu and tries to parse given string with the function parser.
     * If this succeeds, calls the input consumer, else returns the error msg key.
     *
     * @param language            Language - to translate the messages
     * @param descriptionTransKey String - description which will be shown in the left right anvil corner
     * @param errorMsgKey         String - the error message if string could not be parsed
     * @param parser              Function<String, T> - tries to parse the anvil string to the object
     * @param input               Consumer<T> - will be called, when the input was successful
     * @param <T>                 - A Integer, Material or UUID - The object what needs to be inserted
     */
    private static <T> void acceptObjectInAnvilMenu(Language language,
                                                    String descriptionTransKey,
                                                    String replacement,
                                                    String errorMsgKey,
                                                    Function<String, T> parser,
                                                    Consumer<T> input) {
        insertStringInAnvilMenu(language, descriptionTransKey, replacement, s -> {
            try {
                input.accept(parser.apply(s));
                return null;
            } catch (IllegalArgumentException exception) {
                return errorMsgKey;
            }
        });
    }

    protected static void acceptStringInAnvilMenu(Language language, String descriptionTransKey, String replacement, Consumer<String> input) {
        insertStringInAnvilMenu(language, descriptionTransKey, replacement, s -> {
            input.accept(ChatColor.translateAlternateColorCodes('&', s));
            return null;
        });
    }

    protected static void acceptUUIDInAnvilMenu(Language language, String descriptionTransKey, String replacement, Consumer<UUID> input) {
        acceptObjectInAnvilMenu(language, descriptionTransKey, replacement,
                TranslationKeys.QUESTS_BUILDER_NOT_VALID_UUID, UUID::fromString, input);
    }

    protected static void acceptMaterialInAnvilMenu(Language language, String descriptionTransKey, String replacement, Consumer<Material> input) {
        acceptObjectInAnvilMenu(language, descriptionTransKey, replacement,
                TranslationKeys.QUESTS_BUILDER_NOT_VALID_MATERIAL, s -> Material.valueOf(s.toUpperCase()), input);
    }

    protected static void acceptNumberInAnvilMenu(Language language, String descriptionTransKey, String replacement, Consumer<Integer> input) {
        acceptObjectInAnvilMenu(language, descriptionTransKey, replacement,
                TranslationKeys.QUESTS_BUILDER_NOT_VALID_NUMBER, Integer::parseInt, input);
    }

    public static void acceptEntityType(Language language, String descriptionKey, String replacement, Consumer<Object> input) {
        acceptObjectInAnvilMenu(language, descriptionKey, replacement,
                TranslationKeys.QUESTS_BUILDER_NOT_VALID_ENTITYTYPE, EntityType::valueOf, input);
    }
}
