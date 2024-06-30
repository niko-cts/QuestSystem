package net.playlegend.questsystem.gui.builder;

import chatzis.nikolas.mc.nikoapi.inventory.anvilGUI.AnvilGUI;
import chatzis.nikolas.mc.nikoapi.inventory.anvilGUI.AnvilSlot;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
     * Opens an anvil and waits for insertion (rename).
     * Then call acceptable function.
     * If a String is returned, it will translate it to the error message.
     *
     * @param questPlayer            QuestPlayer - to open the anvilgui to
     * @param descriptionTransKey String - description will be shown in the lore.
     * @param stringAcceptable    Function<String, String> - Applies the input. If null is returned insertion was successful, else returned String is error message key.
     */
    private static void insertStringInAnvilMenu(QuestPlayer questPlayer,
                                                String descriptionTransKey,
                                                String replacement,
                                                Function<String, String> stringAcceptable) {
        AnvilGUI anvilGUI = new AnvilGUI(questPlayer.getPlayer(), event -> {
            if (event.getSlot() == AnvilSlot.OUTPUT && !event.getName().isEmpty()) {
                String errorMsg = stringAcceptable.apply(event.getName());
                if (errorMsg != null) {
                    ItemStack clickedItem = event.getClickedItem();
                    ItemMeta itemMeta = clickedItem.getItemMeta();
                    if (itemMeta != null)
                        itemMeta.setDisplayName(questPlayer.getLanguage().translateMessage(errorMsg));
                    clickedItem.setItemMeta(itemMeta);
                } else {
                    event.setWillClose(true);
                }
            }
        });
        anvilGUI.setSlot(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.PAPER)
                .setName(replacement != null && !replacement.isEmpty() ? replacement : "...")
                .setLore(questPlayer.getLanguage().translateMessage(descriptionTransKey, "${input}", replacement)
                        .split(";")).craft());
        anvilGUI.open();
    }

    /**
     * Opens an insertStringInAnvilMenu and tries to parse given string with the function parser.
     * If this succeeds, calls the input consumer, else returns the error msg key.
     *
     * @param questPlayer            QuestPlayer - to open the gui
     * @param descriptionTransKey String - description which will be shown in the left right anvil corner
     * @param errorMsgKey         String - the error message if string could not be parsed
     * @param parser              Function<String, T> - tries to parse the anvil string to the object
     * @param input               Consumer<T> - will be called, when the input was successful
     * @param <T>                 - An Integer, Material or UUID - The object what needs to be inserted
     */
    private static <T> void acceptObjectInAnvilMenu(QuestPlayer questPlayer,
                                                    String descriptionTransKey,
                                                    String replacement,
                                                    String errorMsgKey,
                                                    Function<String, T> parser,
                                                    Consumer<T> input) {
        insertStringInAnvilMenu(questPlayer, descriptionTransKey, replacement, s -> {
            try {
                input.accept(parser.apply(s));
                return null;
            } catch (IllegalArgumentException exception) {
                return errorMsgKey;
            }
        });
    }

    protected static void acceptStringInAnvilMenu(QuestPlayer questPlayer, String descriptionTransKey, String replacement, Consumer<String> input) {
        insertStringInAnvilMenu(questPlayer, descriptionTransKey, replacement, s -> {
            input.accept(ChatColor.translateAlternateColorCodes('&', s));
            return null;
        });
    }

    protected static void acceptUUIDInAnvilMenu(QuestPlayer questPlayer, String descriptionTransKey, String replacement, Consumer<UUID> input) {
        acceptObjectInAnvilMenu(questPlayer, descriptionTransKey, replacement,
                TranslationKeys.QUESTS_BUILDER_NOT_VALID_UUID, UUID::fromString, input);
    }

    protected static void acceptMaterialInAnvilMenu(QuestPlayer questPlayer, String descriptionTransKey, String replacement, Consumer<Material> input) {
        acceptObjectInAnvilMenu(questPlayer, descriptionTransKey, replacement,
                TranslationKeys.QUESTS_BUILDER_NOT_VALID_MATERIAL, s -> Material.valueOf(s.toUpperCase()), input);
    }

    protected static void acceptNumberInAnvilMenu(QuestPlayer questPlayer, String descriptionTransKey, String replacement, Consumer<Integer> input) {
        acceptObjectInAnvilMenu(questPlayer, descriptionTransKey, replacement,
                TranslationKeys.QUESTS_BUILDER_NOT_VALID_NUMBER, Integer::parseInt, input);
    }

    public static void acceptEntityType(QuestPlayer questPlayer, String descriptionKey, String replacement, Consumer<Object> input) {
        acceptObjectInAnvilMenu(questPlayer, descriptionKey, replacement,
                TranslationKeys.QUESTS_BUILDER_NOT_VALID_ENTITYTYPE, EntityType::valueOf, input);
    }
}
