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
	public static final String QUESTS_GUI_OVERVIEW_ACTIVE_NAME = GUI + "overview.active.name";
	public static final String QUESTS_GUI_OVERVIEW_ACTIVE_LORE = GUI + "overview.active.lore";
	public static final String QUESTS_GUI_OVERVIEW_PUBLIC_NAME = GUI + "overview.public.name";
	public static final String QUESTS_GUI_OVERVIEW_PUBLIC_LORE = GUI + "overview.public.lore";
	public static final String QUESTS_GUI_OVERVIEW_FOUND_NAME = GUI + "overview.found.name";
	public static final String QUESTS_GUI_OVERVIEW_FOUND_LORE = GUI + "overview.found.lore";
	public static final String QUESTS_GUI_OVERVIEW_COMPLETED_NAME = GUI + "overview.completed.name";
	public static final String QUESTS_GUI_OVERVIEW_COMPLETED_LORE = GUI + "overview.completed.lore";


	public static final String QUESTS_GUI_ACTIVE_DESCRIPTION_LORE = GUI + "active.description";
	public static final String QUESTS_GUI_ACTIVE_OVERVIEW = GUI + "active.overview";
	public static final String QUESTS_GUI_ACTIVE_LORE_STEP_COMPLETED = GUI + "active.step.completed";
	public static final String QUESTS_GUI_ACTIVE_LORE_STEP_TODO = GUI + "active.step.todo";
	public static final String QUESTS_GUI_ACTIVE_CANCEL_NAME = GUI + "active.cancel.name";
	public static final String QUESTS_GUI_ACTIVE_CANCEL_LORE = GUI + "active.cancel.lore";
	public static final String QUESTS_GUI_ACTIVE_TIME_LEFT_NAME = GUI + "active.time.name";
	public static final String QUESTS_GUI_ACTIVE_TIME_LEFT_LORE = GUI + "active.time.lore";

	// ACTIVE QUEST STEPS
	public static final String QUESTS_GUI_ACTIVE_STEPS_PREVIEW_NAME = GUI + "active.steps.preview.name";
	public static final String QUESTS_GUI_ACTIVE_STEPS_PREVIEW_LORE = GUI + "active.steps.preview.lore";
	public static final String QUESTS_GUI_ACTIVE_STEPS_TITLE = GUI + "active.steps.title.name";
	public static final String QUESTS_GUI_ACTIVE_STEPS_UNCOMPLETED = GUI + "active.steps.uncompleted";
	public static final String QUESTS_GUI_ACTIVE_STEPS_COMPLETED = GUI + "active.steps.completed";

	// DEFAULT QUEST STEPS
	public static final String QUESTS_GUI_NORMAL_STEPS_TITLE = GUI + "normal.steps.title.name";
	public static final String QUESTS_GUI_NORMAL_STEPS_INFO = GUI + "normal.steps.title.info";


	// FOUND, COMPLETED, PUBLIC QUESTS GUI
	public static final String QUESTS_GUI_FOUND_INFO = GUI + "found.info";
	public static final String QUESTS_GUI_COMPLETED_INFO = GUI + "completed.info";
	public static final String QUESTS_GUI_PUBLIC_INFO = GUI + "public.info";
	public static final String QUESTS_GUI_PUBLIC_LORE = GUI + "public.lore";
	public static final String QUESTS_GUI_QUEST_DETAILS_LORE = GUI + "details.lore";

	public static final String QUESTS_GUI_ACCEPT_CONFIRM_NAME = GUI + "accept.confirm.name";
	public static final String QUESTS_GUI_ACCEPT_CONFIRM_LORE = GUI + "accept.confirm.lore";
	public static final String QUESTS_GUI_ACCEPT_TITLE = GUI + "accept.title";
	public static final String QUESTS_GUI_ACCEPT_CONFIRM_NAME = GUI + "accept.confirm.name";
	public static final String QUESTS_GUI_ACCEPT_CONFIRM_LORE = GUI + "accept.confirm.lore";
	public static final String QUESTS_GUI_ACCEPT_REWARD = GUI + "accept.reward";



	public static final String QUESTS_GUI_BACK = GUI + "back";

	// GUI REWARDS
	public static final String QUESTS_GUI_REWARDS_PREVIEW_NAME = GUI + "rewards.preview.name";
	public static final String QUESTS_GUI_REWARDS_PREVIEW_LORE = GUI + "rewards.preview.lore";
	public static final String QUESTS_GUI_REWARDS_TITLE = GUI + "rewards.title";



	// STEPS
	public static final String QUESTS_STEP_CRAFT_PREVIEW = QUEST + "step.craft.preview";
	public static final String QUESTS_STEP_CRAFT_LORE = QUEST + "step.craft.lore";
	public static final String QUESTS_STEP_MINE_PREVIEW = QUEST + "step.mine.preview";
	public static final String QUESTS_STEP_MINE_LORE = QUEST + "step.mine.lore";
	public static final String QUESTS_STEP_KILL_PREVIEW = QUEST + "step.kill.preview";
	public static final String QUESTS_STEP_KILL_LORE = QUEST + "step.kill.lore";
	public static final String QUESTS_STEP_NPC_PREVIEW = QUEST + "step.npc.preview";
	public static final String QUESTS_STEP_NPC_LORE = QUEST + "step.npc.lore";

	// REWARDS
	public static final String QUESTS_REWARD_COINS_PREVIEW = QUEST + "reward.coins.preview";
	public static final String QUESTS_REWARD_COINS_NAME = QUEST + "reward.coins.name";
	public static final String QUESTS_REWARD_COINS_LORE = QUEST + "reward.coins.lore";
	public static final String QUESTS_REWARD_LVL_PREVIEW = QUEST + "reward.lvl.preview";
	public static final String QUESTS_REWARD_LVL_NAME = QUEST + "reward.lvl.name";
	public static final String QUESTS_REWARD_LVL_LORE = QUEST + "reward.lvl.lore";
	public static final String QUESTS_REWARD_ITEM_PREVIEW = QUEST + "reward.item.preview";
	public static final String QUESTS_REWARD_ITEM_LORE = QUEST + "reward.item.lore";


}
