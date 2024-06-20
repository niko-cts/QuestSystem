package net.playlegend.questsystem.translation;

import java.util.Locale;

import static net.playlegend.questsystem.translation.TranslationKeys.*;

/**
 * Holds all default german messages.
 *
 * @author Niko
 */
public class DefaultEnglishMessages extends AbstractDefaultMessages {

    public DefaultEnglishMessages() {
        super(Locale.ENGLISH);
        // events
        add(QUESTS_EVENT_TIMER_EXPIRED, "&e${name} &7has &cexpired&7!");
        add(QUESTS_EVENT_FINISHED, "&aYou finished &e${name}&a!");
        add(QUESTS_EVENT_SWITCHED, "&7You &aswitched &7your active quest to &e${name}&7.");
        add(QUESTS_EVENT_STARTED, "&7The Quest &e${name}&7 has been started!");
        add(QUESTS_EVENT_COUNTDOWN, "&7You have &e${duration} &7left for the Quest &e${name}");
        add(QUESTS_EVENT_FOUND_NEW, "%7You found a &enew quest&7: &e${name}");
        add(QUESTS_EVENT_JOINED_HAS_ACTIVE, "&7You have an &aactive quest '&e${name}' &7running! Time left: &e${duration}");
        add(QUESTS_EVENT_JOINED_NO_ACTIVE, "&7You have no active quest running. Begin a new quest with &e/quest");

        add(QUESTS_DISPLAY_DAYS, "days");
        add(QUESTS_DISPLAY_HOURS, "hours");
        add(QUESTS_DISPLAY_MINUTES, "minutes");
        add(QUESTS_DISPLAY_SECONDS, "seconds");

        // System
        add(SYSTEM_DISABLED_COMMAND, "&cThis command has been disabled.");
        add(SYSTEM_NO_PERMISSION, "&cYou have insufficient permission for this command");

        // Commands
        add(SYSTEM_COMMAND_USAGE, "&7Execute the command with &e/${command}");
        add(QUESTS_COMMAND_NOT_FOUND, "&7This quest could &cnot &7be found.");
        add(QUESTS_COMMAND_ADMIN_USAGE, "questadmin");
        add(QUESTS_COMMAND_ADMIN_QUEST_USAGE, "questadmin quest create/delete/list");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM, "&7Do you really want to &cdelete &7this Quest? (Click here to confirm)");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_SUCCESSFUL, "&7Quest was successfully deleted.");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST_NO_QUESTS, "&7There are no quests listed.");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST, "&7Every quest: &e${quest}");
        add(QUESTS_COMMAND_ADMIN_SIGN_USAGE, "/questadmin sign - Adds a sign");
        add(QUESTS_COMMAND_ADMIN_SIGN_NOT_SIGN, "&7You are &cnot &7looking at a sign.");
        add(QUESTS_COMMAND_ADMIN_SIGN_ADDED, "&7A new sign has been &aadded");

        add(QUESTS_COMMAND_LANGUAGE_USAGE, "language <language>");
        add(QUESTS_COMMAND_LANGUAGE_UPDATED, "&aYour language has been changed.");
        add(QUESTS_COMMAND_LANGUAGE_ILLEGAL, "&7This language is &cnot &7available");
        add(QUESTS_COMMAND_QUEST_USAGE, "quest");
        add(QUESTS_COMMAND_QUEST_CANCEL_USAGE, "quest cancel");
        add(QUESTS_COMMAND_QUEST_CANCEL_SUCCESS, "&7Your active quest has been &acanceled&7. You can restart it via the /quest GUI");
        add(QUESTS_COMMAND_QUEST_CANCEL_NOACTIVE, "&7There is no active quest which you can cancel.");
        add(QUESTS_COMMAND_QUEST_INFO_USAGE, "quest info - Information about your active quest");
        add(QUESTS_COMMAND_QUEST_INFO_DISPLAY, "&7Quest &e${name} &7will exceed in &e${duration}&7. Your current task is: &e${todo}");
        add(QUESTS_COMMAND_QUEST_INFO_NOACTIVE, "&7There is &cno active quest&7. &aStart &7one using the GUI with /quest");

        // GUI overview
        add(QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_NAME, "&7No active quest");
        add(QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_LORE, "&7Start a quest using;&7the '&efound&7' or '&epublic Quests&7' menu");
        add(QUESTS_GUI_OVERVIEW_ACTIVE_NAME, "&a${name} &eis running");
        add(QUESTS_GUI_OVERVIEW_ACTIVE_LORE, "&7${name} is currently your active quest.;&7Click to see details");
        add(QUESTS_GUI_OVERVIEW_PUBLIC_NAME, "&ePublic quests");
        add(QUESTS_GUI_OVERVIEW_PUBLIC_LORE, "&7Click to see all public quests.;&7These are temporary quest;&7which may exceed");
        add(QUESTS_GUI_OVERVIEW_FOUND_NAME, "&eFound quests");
        add(QUESTS_GUI_OVERVIEW_FOUND_LORE, "&7Click to see all quests you found.:&7You can find quests in the world");
        add(QUESTS_GUI_OVERVIEW_COMPLETED_NAME, "&eCompleted quests");
        add(QUESTS_GUI_OVERVIEW_COMPLETED_LORE, "&7Click to see all quests;you have already completed.;&7These quests can not be redone.");

        // GUI active
        add(QUESTS_GUI_ACTIVE_DESCRIPTION_LORE, "&7Click to open the quest description");
        add(QUESTS_GUI_ACTIVE_OVERVIEW, "&7Quest will exceed in: &e${duration};&7You completed &e${done} &7tasks;&7You need to complete &e${todo} &7more tasks");
        add(QUESTS_GUI_ACTIVE_LORE_STEP_COMPLETED, "&7%m${order}. ${task}");
        add(QUESTS_GUI_ACTIVE_LORE_STEP_TODO, "&e${order}. ${task}");
        add(QUESTS_GUI_ACTIVE_CANCEL_NAME, "&cCancel the quest");
        add(QUESTS_GUI_ACTIVE_CANCEL_LORE, "&7Click to cancel the quest.;&7All progress will be lost!;&7You will be able to restart the quest.;&cThis can not be undo");
        add(QUESTS_GUI_ACTIVE_TIME_LEFT_NAME, "&eTime left");
        add(QUESTS_GUI_ACTIVE_TIME_LEFT_LORE, "&7You have &e${duration} &7left;&7to complete this task.");
        add(QUESTS_GUI_ACTIVE_STEPS_PREVIEW_NAME, "&eQuest tasks");
        add(QUESTS_GUI_ACTIVE_STEPS_PREVIEW_LORE, "&7Click to see all tasks to complete the quest");
        add(QUESTS_GUI_ACTIVE_STEPS_TITLE, "&6Active quest tasks");
        add(QUESTS_GUI_ACTIVE_STEPS_UNCOMPLETED, "&7All remaining tasks ->");
        add(QUESTS_GUI_ACTIVE_STEPS_COMPLETED, "&7All completed tasks ->");

        add(QUESTS_GUI_NORMAL_STEPS_TITLE, "&6Quest tasks");
        add(QUESTS_GUI_NORMAL_STEPS_INFO, "&7All tasks to complete ->");

        add(QUESTS_GUI_FOUND_INFO, "&6found quests");
        add(QUESTS_GUI_COMPLETED_INFO, "&6Completed quests");
        add(QUESTS_GUI_PUBLIC_INFO, "&ePublic quests");
        add(QUESTS_GUI_PUBLIC_LORE, " ;&7This quest is public.;&7It may exceed.&eClick to see details.");
        add(QUESTS_GUI_QUEST_DETAILS_LORE, " ;&7At &e${time};&eClick to see details.");
        add(QUESTS_GUI_ACCEPT_START_NAME, "&eStart the quest");
        add(QUESTS_GUI_ACCEPT_START_LORE, "&7Click to open the confirmation menu");
        add(QUESTS_GUI_ACCEPT_CONFIRM_NAME, "&eStart the quest");
        add(QUESTS_GUI_ACCEPT_CONFIRM_LORE, "&7Click to start the quest");
        add(QUESTS_GUI_ACCEPT_TITLE, "&6Start the quest");
        add(QUESTS_GUI_QUEST_TIME_NAME, "&eCompleted at");
        add(QUESTS_GUI_QUEST_TIME_LORE, "&7You completed this quest;&7on &e${time}");
        add(QUESTS_GUI_QUEST_STEPS_NAME, "&eAll quest tasks");
        add(QUESTS_GUI_QUEST_REWARD_NAME, "&eAll quest rewards");
        add(QUESTS_GUI_QUEST_DETAILS_ITEM_LORE, "&7Finish this quest in under &e${duration};&7There are/is &e${rewards} reward;&7and &e${tasks} task(s).");

        add(QUESTS_GUI_ACCEPT_REWARD, " ;&7Click to open a;&7detailed reward menu");
        add(QUESTS_GUI_BACK, "&eBack");
        add(QUESTS_GUI_REWARDS_PREVIEW_NAME, "&eQuest rewards");
        add(QUESTS_GUI_REWARDS_PREVIEW_LORE, "&7Click to open the rewards;&7for this quest");
        add(QUESTS_GUI_REWARDS_TITLE, "&6Quest rewards");

        // STEPS
        add(QUESTS_STEP_CRAFT_NORMAL_LINE, "&7Craft &e${item} &a${maxamount} &7time(s)");
        add(QUESTS_STEP_CRAFT_ACTIVE_LINE, "&7Craft &e${item} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_CRAFT_NORMAL_LORE, "&7You need to craft;&7this item &e${maxamount} &7times.");
        add(QUESTS_STEP_CRAFT_ACTIVE_LORE, "&7You need to craft;&7this item &e${amount} &7more times;&7(&e${amount}&7/&e${maxamount})");
        add(QUESTS_STEP_MINE_NORMAL_LINE, "&7Mine &e${item} &a${maxamount} &7time(s)");
        add(QUESTS_STEP_MINE_ACTIVE_LINE, "&7Mine &e${item} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_MINE_NORMAL_LORE, "&7You need to mine;&7this block &e${maxamount} &7times.");
        add(QUESTS_STEP_MINE_ACTIVE_LORE, "&7You need to mine;&7this block &e${amount} &7more times;&7(&e${amount}&7/&e${maxamount})");
        add(QUESTS_STEP_KILL_NORMAL_LINE, "&7Kill &e${entity} &a${maxamount} &7time(s)");
        add(QUESTS_STEP_KILL_ACTIVE_LINE, "&7Kill &e${entity} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_KILL_NORMAL_LORE, "&7Kill &e${entity} &a${maxamount} &7time(s)");
        add(QUESTS_STEP_KILL_ACTIVE_LORE, "&7You need to kill;&7this entity &e${amount} &7more time(s);&7(&e${amount}&7/&e${maxamount})");
        add(QUESTS_STEP_NPC_NORMAL_LINE, "&7Speak to &e${name} &a${maxamount} &7time(s)");
        add(QUESTS_STEP_NPC_ACTIVE_LINE, "&7Speak to &e${name} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_NPC_NORMAL_LORE, "&7Speak to &e${name} &a${maxamount} &7time(s);&7${name} is at &e${location}");
        add(QUESTS_STEP_NPC_ACTIVE_LORE, "&7You need to speak;&7to ${name} &e${amount} &7more time(s);&7${name} is at &e${location};&7(&e${amount}&7/&e${maxamount})");

        // REWARDS
        add(QUESTS_REWARD_COINS_PREVIEW, "&e${amount} coins");
        add(QUESTS_REWARD_COINS_NAME, "&eCoins");
        add(QUESTS_REWARD_COINS_LORE, "&7You receive &e${amount} coins;&7on finishing the quest");
        add(QUESTS_REWARD_LVL_PREVIEW, "&e${amount} level");
        add(QUESTS_REWARD_LVL_NAME, "&eLevel");
        add(QUESTS_REWARD_LVL_LORE, "&7You receive &e${amount} level;&7on finishing the quest");
        add(QUESTS_REWARD_ITEM_PREVIEW, "1 Item");
        add(QUESTS_REWARD_ITEM_LORE, "&7You will receive this item;&7on finishing the quest");

        save();
    }
}
