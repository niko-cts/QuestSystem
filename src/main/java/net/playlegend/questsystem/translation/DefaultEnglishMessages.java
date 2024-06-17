package net.playlegend.questsystem.translation;

import java.util.Locale;

/**
 * Holds all default german messages.
 * @author Niko
 */
public class DefaultEnglishMessages extends AbstractDefaultMessages {

    public DefaultEnglishMessages() {
        super(Locale.ENGLISH);

        add(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM, "&7Do you really want to &cdelete &7this Quest? (Click here to confirm)");
        add(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_DELETE_SUCCESSFUL, "&7Quest was successfully deleted.");
        add(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_LIST_NO_QUESTS, "&7There are no quests listed.");
        add(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_LIST, "&7Every quest: &e${quest}");

        save();
    }
}
