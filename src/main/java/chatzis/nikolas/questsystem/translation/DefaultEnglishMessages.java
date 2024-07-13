package chatzis.nikolas.questsystem.translation;

import java.util.Locale;

import static chatzis.nikolas.questsystem.translation.TranslationKeys.*;

/**
 * Holds all default german messages.
 *
 * @author Niko
 */
public class DefaultEnglishMessages extends AbstractDefaultMessages {

    public DefaultEnglishMessages() {
        super(Locale.ENGLISH);

        add(QUESTS_NPC_FOUNDBOOK, "&6You found/n&l  ${name}&6!/n/n&0Quest description: /n${description}");
        add(QUESTS_NPC_FOUNDBOOK_ALREADY, "&6You already found this quest!");

        // Scoreboard
        add(QUESTS_SCOREBOARD_DISPLAYNAME, "&6QuestSystem");
        add(QUESTS_SCOREBOARD_ACTIVE_QUESTNAME, "&aActive quest");
        add(QUESTS_SCOREBOARD_ACTIVE_CURRENT_STEP, "&aCurrent task");
        add(QUESTS_SCOREBOARD_ACTIVE_FINISH_AT, "&eQuest ends on");
        add(QUESTS_SCOREBOARD_NO_ACTIVE_1, "&7No active quest");
        add(QUESTS_SCOREBOARD_NO_ACTIVE_2, "&7/quest - To start");
        add(QUESTS_SCOREBOARD_COINS, "&eCoins");

        add(QUESTS_SIGN_NO_ACTIVE_LINE_1, "No active quest");
        add(QUESTS_SIGN_NO_ACTIVE_LINE_2, "Execute /quest");
        add(QUESTS_SIGN_ACTIVE_LINE_1, "Quest &6${name}");
        add(QUESTS_SIGN_ACTIVE_LINE_2, "Next task:");
        add(QUESTS_SIGN_ACTIVE_LINE_3, "${task}");
        add(QUESTS_SIGN_ACTIVE_LINE_4, "${date}");
        // builder
        add(QUESTS_BUILDER_TITLE, "&6Quest builder");
        add(QUESTS_BUILDER_NOT_VALID_NUMBER, "&cYou inserted a invalid number");
        add(QUESTS_BUILDER_NOT_VALID_UUID, "&cYou inserted a invalid uuid");
        add(QUESTS_BUILDER_NOT_VALID_MATERIAL, "&cYou inserted a invalid material");
        add(QUESTS_BUILDER_NOT_VALID_ENTITYTYPE, "&cYou inserted a invalid entity type");
        add(QUESTS_BUILDER_MODIFY_INTEGER, "&7Insert a number for: ${input}");
        add(QUESTS_BUILDER_MODIFY_ITEM_INSERTION, "&7Insert an item;&7next to this one;&7and close the inventory.");
        add(QUESTS_BUILDER_MODIFY_STEPS_REMOVE, ";&7Task-id is: ${id};&7Click to &cremove &7the task.");
        add(QUESTS_BUILDER_MODIFY_REWARD_REMOVE, ";&7Click to &cremove &7the reward.");
        add(QUESTS_BUILDER_MODIFY_ADD, "&7Click to &aadd an entry");
        add(QUESTS_BUILDER_MODIFY_QUESTNAME, "&7Insert a name which;&7will be the questname");
        add(QUESTS_BUILDER_NAME_LORE, "&7This will be the name of the quest;&7Click to change it");
        add(QUESTS_BUILDER_DESCRIPTION_NAME, "&eQuest description");
        add(QUESTS_BUILDER_DESCRIPTION_LORE, "&7Click to change the quest description");
        add(QUESTS_BUILDER_DESCRIPTION_CLICK_TEXT, "&e[CLICK] &7And type in the quest description (';' for a line break). The description will be shown on the quest item.");
        add(QUESTS_BUILDER_DESCRIPTION_CLICK_HOVER, "&7Click and type in the quest description");
        add(QUESTS_BUILDER_REWARDS_LORE, ";&7Left click to add a new quest reward; ;&7Shift-Click to see;&7the current entries");
        add(QUESTS_BUILDER_STEPS_LORE, ";&7Left click to add a new quest step; ;&7Shift-Click to see;&7the current entries");
        add(QUESTS_BUILDER_TIMER_OFFLINE_NAME, "&eCountdown runs offline: ${active}");
        add(QUESTS_BUILDER_TIMER_OFFLINE_LORE, "&7Click to toggle;&7if the countdown continues offline");
        add(QUESTS_BUILDER_PUBLIC_NAME, "&eQuest is public: ${active}");
        add(QUESTS_BUILDER_PUBLIC_LORE, "&7Click to toggle;&7if the quest is public visible;&7and does not need to be;&7found by the player");
        add(QUESTS_BUILDER_TIMER_NAME, "&eQuest countdown");
        add(QUESTS_BUILDER_TIMER_LORE, "&7The seconds the player;&7needs to complete the quest;&7Current duration: &e${duration}");
        add(QUESTS_BUILDER_CREATE_NAME, "&aCreate the quest");
        add(QUESTS_BUILDER_CREATE_LORE, "&7Click to create the quest;&7You have to set the following:;&7- Set a name;&7- Set a description;&7- Set at least one task");
        add(QUESTS_BUILDER_CREATE_UN_FINISHED, "&cInvalid input! &7You have to set a quest name, description and at least one task!");
        add(QUESTS_BUILDER_CREATE_NAME_ALREADY_EXISTS, "&cThis quest name already exists!");

        add(QUESTS_BUILDER_SUCCESSFUL_CREATED, "&aQuest &e${name}&a was created");
        add(QUESTS_BUILDER_SUCCESSFUL_CREATED_ERROR, "&cThere was an error creating Quest &e${name}&7! Please take a look in the console.");
        add(QUESTS_BUILDER_SUCCESSFUL_UPDATED, "&aQuest &e${name}&a was updated");
        add(QUESTS_BUILDER_SUCCESSFUL_UPDATED_ERROR, "&cThere was an error updating Quest &e${name}&7! Please take a look in the console.");

        add(QUESTS_BUILDER_STEPS_CREATION_ORDER_NAME, "&7Current order: &e${order}");
        add(QUESTS_BUILDER_STEPS_CREATION_ORDER_LORE, "&7The order of a task;&7determines when it can be done;&7Tasks have to be completed in chronological order;&7A quest can have multiple;&7tasks with the same order;;&7Click to change the order");
        add(QUESTS_BUILDER_STEPS_CREATION_AMOUNT_NAME, "&7Current amount: &e${amount}");
        add(QUESTS_BUILDER_STEPS_CREATION_AMOUNT_LORE, "&7The amount to complete the task;&7This is e.g.:;&7'mine <amount> of blocks';&7Click to change");
        add(QUESTS_BUILDER_STEPS_CREATION_PARAMETER_NAME,"&eCurrent parameter: &6${parameter}");
        add(QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, "&7Insert the parameter of the task;&7For the Mining-Task it requires a material name;;&7This task requires the type: &e${input};;&7Click to insert parameter");
        add(QUESTS_BUILDER_STEPS_CREATION_ACCEPT_LORE, " ; ;&aClick to add");
        add(QUESTS_BUILDER_STEPS_CREATION_ERROR, "&cThere was an error creating the task item! &7Error is: &e${message}");


        // events
        add(QUESTS_EVENT_TIMER_EXPIRED, "&e${name} &7has &cexpired&7! &7Restart it via /quest");
        add(QUESTS_EVENT_FINISHED, "&aYou finished &e${name}&a!");
        add(QUESTS_EVENT_STEP_FINISHED, "&7You completed task '&e${task}&7'");
        add(QUESTS_EVENT_SWITCHED, "&7You &aswitched &7your active quest to &e${name}&7.");
        add(QUESTS_EVENT_STARTED, "&7The Quest &e${name}&7 has been started!");
        add(QUESTS_EVENT_COUNTDOWN, "&7You have &e${duration} &7left for the Quest &e${name}");
        add(QUESTS_EVENT_FOUND_NEW, "&7You found the quest &e'${name}&7' - You can start it via /quest");
        add(QUESTS_EVENT_CLICK_TO_OPEN_HOVER, "&7Click to open the Quest menu");
        add(QUESTS_EVENT_JOINED_HAS_ACTIVE, "&7You have an active quest &e'${name}' &7running! Time left: &e${duration}");
        add(QUESTS_EVENT_JOINED_NO_ACTIVE_TEXT, "&7You have no active quest running. Begin a new quest with &e/quest");
        add(QUESTS_EVENT_JOINED_NO_ACTIVE_HOVER, "&7Click to open your quest menu");

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
        add(QUESTS_COMMAND_ADMIN_USAGE, "questadmin create/list/remove/sign/npc");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST_USAGE, "questadmin list (<questname>) - Shows all or open a admin mode-GUI for a specific quest");
        add(QUESTS_COMMAND_ADMIN_QUEST_CREATE_USAGE, "questadmin create (description <description>)");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_USAGE, "questadmin delete <name>");

        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM_TEXT, "&7Do you really want to &cdelete &7this Quest? (Click here to confirm)");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM_HOVER, "&7Click to delete the quest &cpermanently&7!");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_SUCCESSFUL, "&7Quest was successfully deleted.");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST_NO_QUESTS, "&7There are no quests listed.");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST, "&7Every quest by name - Click to open specific menu: ");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST_HOVER, "&7Click to open &e${name}");
        add(QUESTS_COMMAND_ADMIN_SIGN_USAGE, "questadmin sign - Adds a sign");
        add(QUESTS_COMMAND_ADMIN_SIGN_NOT_SIGN, "&7You are &cnot &7looking at a sign.");
        add(QUESTS_COMMAND_ADMIN_SIGN_ADDED, "&7A new sign has been &aadded");

        add(QUESTS_COMMAND_ADMIN_NPC_USAGE, "questadmin npc find/task/list - Create/Update a find or task NPC. Alternatively list every available NPC's");
        add(QUESTS_COMMAND_ADMIN_NPC_LIST_USAGE, "questadmin npc list - Lists every available NPC");
        add(QUESTS_COMMAND_ADMIN_NPC_LIST_FIND, "&7All Find-NPC:");
        add(QUESTS_COMMAND_ADMIN_NPC_LIST_FIND_ELEMENT, "&7- &c[&7Find quest ${questname} &7through ${name} &7at ${location}&c]");
        add(QUESTS_COMMAND_ADMIN_NPC_LIST_TASK, "&7All Task-NPC:");
        add(QUESTS_COMMAND_ADMIN_NPC_LIST_TASK_ELEMENT, "&7- &c[&e${name} &7with uuid '${uuid} should be spoken to in &b(&7${queststeps}&b)&7. Located at ${location}&c]");
        add(QUESTS_COMMAND_ADMIN_NPC_LIST_TASK_ELEMENT_STEP, "&7Quest: ${questname} &7at Task-ID: &e${stepid}&7");
        add(QUESTS_COMMAND_ADMIN_NPC_FIND_SUCCESSFUL, "&aFind-NPC was created/updated. &7A NPC will spawn at this location. Players can find the Quest through the NPC.");
        add(QUESTS_COMMAND_ADMIN_NPC_FIND_DELETED, "&7Find-NPC was deleted.");
        add(QUESTS_COMMAND_ADMIN_NPC_TASK_SUCCESSFUL, "&aTask-NPC was created/updated. &7A NPC will spawn at this location. Player have to speak to this NPC to trigger the Quest-Task 'talk'!");
        add(QUESTS_COMMAND_ADMIN_NPC_TASK_DELETED, "&7Task-NPC was deleted.");
        add(QUESTS_COMMAND_ADMIN_NPC_FIND_USAGE, "questadmin npc find add/remove <questname> (<npcname>) - Create or delete an NPC who lets player find NPC");
        add(QUESTS_COMMAND_ADMIN_NPC_TASK_USAGE, "questadmin npc task add/remove <uuid> (<npcname> <language> <text>) - Create or delete an NPC who lets player complete the task");


        add(QUESTS_COMMAND_LANGUAGE_USAGE, "language <language>");
        add(QUESTS_COMMAND_LANGUAGE_UPDATED, "&aYour language has been changed.");
        add(QUESTS_COMMAND_LANGUAGE_ILLEGAL, "&7This language is &cnot &7available. Languages are: ${languages}");
        add(QUESTS_COMMAND_QUEST_USAGE, "quest");
        add(QUESTS_COMMAND_QUEST_CANCEL_USAGE, "quest cancel");
        add(QUESTS_COMMAND_QUEST_CANCEL_SUCCESS, "&7Your active quest has been &acanceled&7. Click here to open the Quest menu");
        add(QUESTS_COMMAND_QUEST_CANCEL_CONFIRM_TEXT, "&7[&eCLICK&7] &7Here to confirm the &ccancellation &7of the current active Quest. All progress will be lost!");
        add(QUESTS_COMMAND_QUEST_CANCEL_CONFIRM_HOVER, "&7Click to cancel the active quest");
        add(QUESTS_COMMAND_QUEST_CANCEL_NOACTIVE, "&7There is &cno &7active quest which you can cancel.");
        add(QUESTS_COMMAND_QUEST_INFO_USAGE, "quest info - Information about your active quest");
        add(QUESTS_COMMAND_QUEST_INFO_DISPLAY, "&7Quest &e${name} &7will exceed in &e${duration}&7. Your current task is: &e${todo}");
        add(QUESTS_COMMAND_QUEST_INFO_NOACTIVE, "&7There is &cno active quest&7. &aStart &7one using the GUI with /quest");

        add(QUESTS_COMMAND_QUEST_FIND_USAGE, "quest find <name> - Discovers a quest");
        add(QUESTS_COMMAND_QUEST_FIND_ALREADY_FOUND, "&cYou already found this quest");
        add(QUESTS_COMMAND_QUEST_FIND_ISPUBLIC, "&cThis quest is public and cannot be found");

        // GUI overview
        add(QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_NAME, "&7No active quest");
        add(QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_LORE, "&7Start a quest using;&7the '&efound&7' or '&epublic Quests&7' menu");
        add(QUESTS_GUI_OVERVIEW_ACTIVE_NAME, "&a${name} &eis running");
        add(QUESTS_GUI_OVERVIEW_ACTIVE_LORE, "&7${name} is currently your active quest.;&7Click to see details");
        add(QUESTS_GUI_OVERVIEW_PUBLIC_NAME, "&ePublic quests");
        add(QUESTS_GUI_OVERVIEW_PUBLIC_LORE, "&7Click to see all public quests.;&7These are temporary quest;&7which may exceed");
        add(QUESTS_GUI_OVERVIEW_FOUND_NAME, "&eFound quests");
        add(QUESTS_GUI_OVERVIEW_FOUND_LORE, "&7Click to see all quests you found.;&7You can find quests in the world");
        add(QUESTS_GUI_OVERVIEW_COMPLETED_NAME, "&eCompleted quests");
        add(QUESTS_GUI_OVERVIEW_COMPLETED_LORE, "&7Click to see all quests;&7you have already completed.;&7These quests can not be redone.");

        // GUI active
        add(QUESTS_GUI_ACTIVE_OVERVIEW_NAME, "&aQuest info");
        add(QUESTS_GUI_ACTIVE_OVERVIEW, "&7Quest will exceed in: &e${duration};;&7You completed &e${done} &7tasks;&7You need to complete &e${todo} &7more tasks:;");
        add(QUESTS_GUI_ACTIVE_LORE_STEP_COMPLETED, "&7&m${task}");
        add(QUESTS_GUI_ACTIVE_CANCEL_NAME, "&cCancel the quest");
        add(QUESTS_GUI_ACTIVE_CANCEL_LORE, "&7Click to cancel the quest.;&7All progress will be lost!;&7You will be able to restart the quest.");
        add(QUESTS_GUI_ACTIVE_TIME_LEFT_NAME, "&7Time left: &e${duration}");
        add(QUESTS_GUI_ACTIVE_STEPS_PREVIEW_NAME, "&eQuest tasks");
        add(QUESTS_GUI_STEPS_PREVIEW_LORE, ";&7Click to see task details");
        add(QUESTS_GUI_ACTIVE_STEPS_TITLE, "&6Active quest tasks");
        add(QUESTS_GUI_ACTIVE_STEPS_UNCOMPLETED, "&7All remaining tasks ->");
        add(QUESTS_GUI_ACTIVE_STEPS_COMPLETED, "&7All completed tasks ->");

        add(QUESTS_GUI_NORMAL_STEPS_TITLE, "&6Quest tasks");
        add(QUESTS_GUI_NORMAL_STEPS_INFO, "&7All tasks to complete ->");

        add(QUESTS_GUI_FOUND_INFO, "&6Found quests");
        add(QUESTS_GUI_COMPLETED_INFO, "&6Completed quests");
        add(QUESTS_GUI_PUBLIC_INFO_NAME, "&ePublic quests");
        add(QUESTS_GUI_PUBLIC_INFO_LORE, "&7A list of every public quest;&7you have not completed yet;;&7Public quests may expire;&7and can not be started");
        add(QUESTS_GUI_PUBLIC_LORE, " ;&7This quest is public.;&7It may exceed.;&eClick to see details.");
        add(QUESTS_GUI_QUEST_DETAILS_FOUND_LORE, " ;&7You found this quest on &e${time};&eClick to see details.");
        add(QUESTS_GUI_QUEST_DETAILS_COMPLETED_LORE, " ;&7You completed this quest on &e${time};&eClick to see details.");
        add(QUESTS_GUI_ACCEPT_START_NAME, "&eStart the quest");
        add(QUESTS_GUI_ACCEPT_START_LORE, "&7Click to start this quest;&7An active quest will be &ccanceled;&7You can restart it later on");
        add(QUESTS_GUI_ACCEPT_TITLE, "&6Start the quest");
        add(QUESTS_GUI_QUEST_TIME_NAME, "&eCompleted at");
        add(QUESTS_GUI_QUEST_TIME_LORE, "&7You completed this quest;&7on &e${time}");
        add(QUESTS_GUI_QUEST_STEPS_NAME, "&eAll quest tasks");
        add(QUESTS_GUI_QUEST_REWARD_NAME_HAS, "&eAll quest rewards");
        add(QUESTS_GUI_QUEST_REWARD_NAME_NONE, "&eNo quest rewards");
        add(QUESTS_GUI_QUEST_DETAILS_ITEM_LORE, ";&7Finish this quest in under &e${duration};&7There are/is &e${rewards} reward(s);&7and &e${tasks} task(s).");
        add(QUESTS_GUI_QUEST_TITLE, "&6Quest ${quest}");
        add(QUESTS_GUI_QUEST_ADMIN_MODIFY_NAME, "&eModify Quest");
        add(QUESTS_GUI_QUEST_ADMIN_MODIFY_LORE, "&7Click here;&7to change the Quest");
        add(QUESTS_GUI_QUEST_ADMIN_DELETE_NAME, "&cDelete Quest");
        add(QUESTS_GUI_QUEST_ADMIN_DELETE_LORE, "&7Click here;&7to &cdelete&7 the quest!");


        add(QUESTS_GUI_BACK, "&eBack");
        add(QUESTS_GUI_REWARDS_PREVIEW_NAME, "&eQuest rewards");
        add(QUESTS_GUI_REWARDS_PREVIEW_LORE, ";&7Click to open the rewards;&7for this quest");
        add(QUESTS_GUI_REWARDS_TITLE, "&6Quest rewards");

        // STEPS
        add(QUESTS_STEP_CRAFT_NAME, "&7Craft &e${maxamount} ${item}");
        add(QUESTS_STEP_CRAFT_NORMAL_LINE, "&e${order}&7. &7Craft &e${item} &a${maxamount} &7time(s)");
        add(QUESTS_STEP_CRAFT_ACTIVE_LINE, "&e${order}&7. &7Craft &e${item} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_CRAFT_NORMAL_LORE, "&7You need to craft;&7this item &e${maxamount} &7times.");
        add(QUESTS_STEP_CRAFT_ACTIVE_LORE, "&7You need to craft;&7this item &e${amount}&7/&e${maxamount} &7more times");

        add(QUESTS_STEP_MINE_NAME, "&7Mine &e${maxamount} ${item}");
        add(QUESTS_STEP_MINE_NORMAL_LINE, "&e${order}&7. &7Mine &e${item} &a${maxamount} &7time(s)");
        add(QUESTS_STEP_MINE_ACTIVE_LINE, "&e${order}&7. &7Mine &e${item} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_MINE_ALREADY_MINED, "&7You already &cmined &7this block at the same location!");
        add(QUESTS_STEP_MINE_NORMAL_LORE, "&7You need to mine;&7this block &e${maxamount} &7times.");
        add(QUESTS_STEP_MINE_ACTIVE_LORE, "&7You need to mine;&7this block &e&e${amount}&7/&e${maxamount} &7more times");

        add(QUESTS_STEP_KILL_NAME, "&7Kill &e${maxamount} ${entity}");
        add(QUESTS_STEP_KILL_NORMAL_LINE, "&e${order}&7. &7Kill &e${entity} &a${maxamount} &7time(s)");
        add(QUESTS_STEP_KILL_ACTIVE_LINE, "&e${order}&7. &7Kill &e${entity} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_KILL_NORMAL_LORE, "&7Kill &e${entity} &a${maxamount} &7time(s)");
        add(QUESTS_STEP_KILL_ACTIVE_LORE, "&7You need to kill;&7this entity &e&e${amount}&7/&e${maxamount} &7more time(s)");

        add(QUESTS_STEP_NPC_NAME, "&7Speak to &e${name}");
        add(QUESTS_STEP_NPC_NORMAL_LINE, "&e${order}&7. &7Speak to &e${name}");
        add(QUESTS_STEP_NPC_ACTIVE_LINE, "&e${order}&7. &7Speak to &e${name} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_NPC_NORMAL_NAME, "&eSpeak to ${name}");
        add(QUESTS_STEP_NPC_NORMAL_LORE, "&7Speak to &e${name} &a${maxamount} &7time(s);&7${name} &7is at &e${location}");
        add(QUESTS_STEP_NPC_ACTIVE_LORE, "&7You need to speak;&7to ${name} &e${amount}&7/&e${maxamount} &7more time(s);&7${name} &7is at &e${location}");

        // REWARDS
        add(QUESTS_REWARD_COINS_PREVIEW, "&e${amount} coins");
        add(QUESTS_REWARD_COINS_NAME, "&eCoins");
        add(QUESTS_REWARD_COINS_LORE, "&7You receive &e${amount} coins;&7on finishing the quest");
        add(QUESTS_REWARD_COINS_MESSAGE, "&7You receive &e${amount} coins");
        add(QUESTS_REWARD_LVL_PREVIEW, "&e${amount} level");
        add(QUESTS_REWARD_LVL_NAME, "&eLevel");
        add(QUESTS_REWARD_LVL_LORE, "&7You receive &e${amount} level;&7on finishing the quest");
        add(QUESTS_REWARD_LVL_MESSAGE, "&7You receive &e${amount} level");
        add(QUESTS_REWARD_ITEM_PREVIEW, "1 Item");
        add(QUESTS_REWARD_ITEM_LORE, "&7You will receive this item;&7on finishing the quest");
        add(QUESTS_REWARD_ITEM_MESSAGE, "&7You receive the Item: &e${name}");

        save();
    }
}
