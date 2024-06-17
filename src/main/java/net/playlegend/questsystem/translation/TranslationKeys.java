package net.playlegend.questsystem.translation;

/**
 * This class holds key constants that represent a translatable message.
 *
 * @author Niko
 */
public class TranslationKeys {


	private TranslationKeys() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	private static final String QUEST = "quests.";
	private static final String Q_COMMAND = QUEST + "command.";
	private static final String QUEST_EVENT = QUEST + "event";
	public static final String QUESTS_EVENT_TIMER_EXPIRED = QUEST_EVENT + "timer.expired";
	public static final String QUESTS_EVENT_FINISHED = QUEST_EVENT + "finished";
	public static final String QUESTS_EVENT_SWITCHED = QUEST_EVENT + "switched";
	public static final String QUESTS_EVENT_STARTED = QUEST_EVENT + "started";
	public static final String QUESTS_EVENT_COUNTDOWN = QUEST_EVENT + "timer";
	public static final String QUESTS_EVENT_FOUND_NEW = QUEST_EVENT + "found";

	public static final String QUESTS_EVENT_JOINED_HAS_ACTIVE = QUEST_EVENT + "join.has";
	public static final String QUESTS_EVENT_JOINED_NO_ACTIVE = QUEST_EVENT + "join.none";

	public static final String QUESTS_DISPLAY_DAYS = QUEST + "display.days";
	public static final String QUESTS_DISPLAY_HOURS = QUEST + "display.hours";
	public static final String QUESTS_DISPLAY_MINUTES = QUEST + "display.minutes";
	public static final String QUESTS_DISPLAY_SECONDS = QUEST + "display.seconds";

	private static final String SYSTEM = QUEST + "system.";
	private static final String COMMAND = SYSTEM + "command.";
	public static final String SYSTEM_DISABLED_COMMAND = COMMAND + "disabled";
	public static final String SYSTEM_NO_PERMISSION = COMMAND + "nopermission";
	public static final String SYSTEM_COMMAND_USAGE = COMMAND + "usage";

	public static final String QUESTS_COMMAND_NOT_FOUND = Q_COMMAND + "error.notfound";

	public static final String QUESTS_COMMAND_ADMIN_USAGE = Q_COMMAND + "admin.usage";

	// /questadmin ...
	public static final String QUESTS_COMMAND_ADMIN_QUEST_USAGE = Q_COMMAND + "admin.quest.usage";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM = Q_COMMAND + "admin.quest.delete.confirmation";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_DELETE_SUCCESSFUL = Q_COMMAND + "admin.quest.delete.successful";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_LIST_NO_QUESTS = Q_COMMAND + "admin.quest.list.noquests";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_LIST = Q_COMMAND + "admin.quest.list.show";

	// /language
	public static final String QUESTS_COMMAND_LANGUAGE_USAGE = Q_COMMAND + "language.usage";
	public static final String QUESTS_COMMAND_LANGUAGE_UPDATED = Q_COMMAND + "language.updated";
	public static final String QUESTS_COMMAND_LANGUAGE_ILLEGAL = Q_COMMAND + "language.illegal";

	// /quest ...
	public static final String QUESTS_COMMAND_QUEST_USAGE = Q_COMMAND + "quest.default.usage";

	public static final String QUESTS_COMMAND_QUEST_CANCEL_USAGE = Q_COMMAND + "quest.cancel.usage";
	public static final String QUESTS_COMMAND_QUEST_CANCEL_SUCCESS = Q_COMMAND + "quest.cancel.canceled";
	public static final String QUESTS_COMMAND_QUEST_CANCEL_NOACTIVE = Q_COMMAND + "quest.cancel.noactivequest";

	public static final String QUESTS_COMMAND_QUEST_INFO_USAGE = Q_COMMAND + "quest.info.usage";
	public static final String QUESTS_COMMAND_QUEST_INFO_DISPLAY = Q_COMMAND + "quest.info.display";
	public static final String QUESTS_COMMAND_QUEST_INFO_NOACTIVE = Q_COMMAND + "quest.info.noactive";



	private static final String GUI = QUEST + "gui";
	public static final String QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_NAME = GUI + "overview.noactive.name";
	public static final String QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_LORE = GUI + "overview.noactive.lore";
	public static final String QUESTS_GUI_OVERVIEW_PUBLIC_NAME = GUI + "overview.public.name";
	public static final String QUESTS_GUI_OVERVIEW_PUBLIC_LORE = GUI + "overview.public.lore";
	public static final String QUESTS_GUI_OVERVIEW_FOUND_NAME = GUI + "overview.found.name";
	public static final String QUESTS_GUI_OVERVIEW_FOUND_LORE = GUI + "overview.found.lore";
	public static final String QUESTS_GUI_OVERVIEW_COMPLETED_NAME = GUI + "overview.completed.name";
	public static final String QUESTS_GUI_OVERVIEW_COMPLETED_LORE = GUI + "overview.completed.lore";


	public static final String QUESTS_GUI_OVERVIEW_ACTIVE_STEP_DETAILS_NAME = GUI + "overview.active.description.name";
	public static final String QUESTS_GUI_OVERVIEW_ACTIVE_DESCRIPTION_LORE = GUI + "overview.active.description.lore";
	public static final String QUESTS_GUI_OVERVIEW_ACTIVE_LORE = GUI + "overview.active.lore.overview";
	public static final String QUESTS_GUI_OVERVIEW_ACTIVE_LORE_STEP_COMPLETED = GUI + "overview.active.lore.step.completed";
	public static final String QUESTS_GUI_OVERVIEW_ACTIVE_LORE_STEP_TODO = GUI + "overview.active.lore.step.todo";
	public static final String QUESTS_GUI_OVERVIEW_ACTIVE_CANCEL_NAME = GUI + "overview.active.cancel.name";
	public static final String QUESTS_GUI_OVERVIEW_ACTIVE_CANCEL_LORE = GUI + "overview.active.cancel.lore";


	// STEPS
	public static final String QUESTS_STEP_CRAFT_NAME = QUEST + "step.craft.name";
	public static final String QUESTS_STEP_CRAFT_LORE = QUEST + "step.craft.lore";
	public static final String QUESTS_STEP_MINE_NAME = QUEST + "step.mine.name";
	public static final String QUESTS_STEP_MINE_LORE = QUEST + "step.mine.lore";
	public static final String QUESTS_STEP_KILL_NAME = QUEST + "step.kill.name";
	public static final String QUESTS_STEP_KILL_LORE = QUEST + "step.kill.lore";
	public static final String QUESTS_STEP_NPC_NAME = QUEST + "step.npc.name";
	public static final String QUESTS_STEP_NPC_LORE = QUEST + "step.npc.lore";


}
