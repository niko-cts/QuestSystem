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

	public static final String QUESTS_NPC_FOUNDBOOK = QUEST + "npc.found.foundbook";
	public static final String QUESTS_NPC_FOUNDBOOK_ALREADY = QUEST + "npc.found.alreadyfound";

	public static final String QUESTS_SCOREBOARD_DISPLAYNAME = QUEST + "scoreboard.displayname";
	public static final String QUESTS_SCOREBOARD_ACTIVE_QUESTNAME = QUEST + "scoreboard.active.questname";
	public static final String QUESTS_SCOREBOARD_ACTIVE_CURRENT_STEP = QUEST + "scoreboard.active.currentstep";
	public static final String QUESTS_SCOREBOARD_ACTIVE_FINISH_AT = QUEST + "scoreboard.active.finishat";
	public static final String QUESTS_SCOREBOARD_NO_ACTIVE_1 = QUEST + "scoreboard.noactive.1";
	public static final String QUESTS_SCOREBOARD_NO_ACTIVE_2 = QUEST + "scoreboard.noactive.2";
	public static final String QUESTS_SCOREBOARD_COINS = QUEST + "scoreboard.coins";


	public static final String QUESTS_SIGN_NO_ACTIVE_LINE_1 = QUEST + "sign.noactive.1";
	public static final String QUESTS_SIGN_NO_ACTIVE_LINE_2 = QUEST + "sign.noactive.2";
	public static final String QUESTS_SIGN_ACTIVE_LINE_1 = QUEST + "sign.active.1";
	public static final String QUESTS_SIGN_ACTIVE_LINE_2 = QUEST + "sign.active.2";
	public static final String QUESTS_SIGN_ACTIVE_LINE_3 = QUEST + "sign.active.3";
	public static final String QUESTS_SIGN_ACTIVE_LINE_4 = QUEST + "sign.active.4";

	private static final String BUILDER = QUEST + "builder.";
	public static final String QUESTS_BUILDER_NOT_VALID_NUMBER = BUILDER + "input.invalid.number";
	public static final String QUESTS_BUILDER_NOT_VALID_UUID = BUILDER + "input.invalid.uuid";
	public static final String QUESTS_BUILDER_NOT_VALID_MATERIAL = BUILDER + "input.invalid.material";
	public static final String QUESTS_BUILDER_NOT_VALID_ENTITYTYPE = BUILDER + "input.invalid.entitytype";
	public static final String QUESTS_BUILDER_MODIFY_INTEGER = BUILDER + "input.modify.integer";
	public static final String QUESTS_BUILDER_MODIFY_ITEM_INSERTION = BUILDER + "modify.item";
	public static final String QUESTS_BUILDER_MODIFY_STEPS_REMOVE = BUILDER + "input.modify.steps.remove";
	public static final String QUESTS_BUILDER_MODIFY_REWARD_REMOVE = BUILDER + "input.modify.reward.remove";
	public static final String QUESTS_BUILDER_MODIFY_ADD = BUILDER + "input.modify.add";
	public static final String QUESTS_BUILDER_MODIFY_QUESTNAME = BUILDER + "input.modify.questname";
	public static final String QUESTS_BUILDER_NAME_LORE = BUILDER + "questname.lore";
	public static final String QUESTS_BUILDER_DESCRIPTION_NAME = BUILDER + "description.name";
	public static final String QUESTS_BUILDER_DESCRIPTION_LORE = BUILDER + "description.lore";
	public static final String QUESTS_BUILDER_DESCRIPTION_CLICK_TEXT = BUILDER + "description.click.text";
	public static final String QUESTS_BUILDER_DESCRIPTION_CLICK_HOVER = BUILDER + "description.click.hover";
	public static final String QUESTS_BUILDER_REWARDS_LORE = BUILDER + "rewards.lore";
	public static final String QUESTS_BUILDER_STEPS_LORE = BUILDER + "steps.lore";
	public static final String QUESTS_BUILDER_TIMER_OFFLINE_NAME = BUILDER + "timer.runsoffline.name";
	public static final String QUESTS_BUILDER_TIMER_OFFLINE_LORE = BUILDER + "timer.runsoffline.lore";
	public static final String QUESTS_BUILDER_PUBLIC_NAME = BUILDER + "public.name";
	public static final String QUESTS_BUILDER_PUBLIC_LORE = BUILDER + "public.lore";
	public static final String QUESTS_BUILDER_TIMER_NAME = BUILDER + "timer.amount.name";
	public static final String QUESTS_BUILDER_TIMER_LORE = BUILDER + "timer.amount.lore";
	public static final String QUESTS_BUILDER_CREATE_NAME = BUILDER + "create.name";
	public static final String QUESTS_BUILDER_CREATE_LORE = BUILDER + "create.lore";
	public static final String QUESTS_BUILDER_CREATE_NAME_ALREADY_EXISTS = BUILDER + "create.namealreadyexists";
	public static final String QUESTS_BUILDER_SUCCESSFUL_CREATED = BUILDER + "successful.created.success";
	public static final String QUESTS_BUILDER_SUCCESSFUL_CREATED_ERROR = BUILDER + "successful.created.error";
	public static final String QUESTS_BUILDER_SUCCESSFUL_UPDATED = BUILDER + "successful.updated.success";
	public static final String QUESTS_BUILDER_SUCCESSFUL_UPDATED_ERROR = BUILDER + "successful.updated.error";
	public static final String QUESTS_BUILDER_STEPS_CREATION_ORDER_NAME = BUILDER + "steps.creation.order.name";
	public static final String QUESTS_BUILDER_STEPS_CREATION_ORDER_LORE = BUILDER + "steps.creation.order.lore";
	public static final String QUESTS_BUILDER_STEPS_CREATION_AMOUNT_NAME = BUILDER + "steps.creation.amount.name";
	public static final String QUESTS_BUILDER_STEPS_CREATION_AMOUNT_LORE = BUILDER + "steps.creation.amount.lore";
	public static final String QUESTS_BUILDER_STEPS_CREATION_PARAMETER_NAME = BUILDER + "steps.creation.parameter.name";
	public static final String QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE = BUILDER + "steps.creation.parameter.lore";
	public static final String QUESTS_BUILDER_STEPS_CREATION_ACCEPT_LORE = BUILDER + "steps.accept";
	public static final String QUESTS_BUILDER_STEPS_CREATION_ERROR = BUILDER + "steps.error";



	private static final String Q_COMMAND = QUEST + "command.";
	private static final String QUEST_EVENT = QUEST + "event";
	public static final String QUESTS_EVENT_TIMER_EXPIRED = QUEST_EVENT + "timer.expired";
	public static final String QUESTS_EVENT_FINISHED = QUEST_EVENT + "finished.quest";
	public static final String QUESTS_EVENT_STEP_FINISHED = QUEST_EVENT + "finished.task";
	public static final String QUESTS_EVENT_SWITCHED = QUEST_EVENT + "switched";
	public static final String QUESTS_EVENT_STARTED = QUEST_EVENT + "started";
	public static final String QUESTS_EVENT_COUNTDOWN = QUEST_EVENT + "timer.left";
	public static final String QUESTS_EVENT_FOUND_NEW = QUEST_EVENT + "found.text";
	public static final String QUESTS_EVENT_CLICK_TO_OPEN_HOVER = QUEST_EVENT + "message.hover";

	public static final String QUESTS_EVENT_JOINED_HAS_ACTIVE = QUEST_EVENT + "join.has";
	public static final String QUESTS_EVENT_JOINED_NO_ACTIVE_TEXT = QUEST_EVENT + "join.none.text";
	public static final String QUESTS_EVENT_JOINED_NO_ACTIVE_HOVER = QUEST_EVENT + "join.none.hover";

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
	public static final String QUESTS_COMMAND_ADMIN_QUEST_LIST_USAGE = Q_COMMAND + "admin.quest.list.usage";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_CREATE_USAGE = Q_COMMAND + "admin.quest.create.usage";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_DELETE_USAGE = Q_COMMAND + "admin.quest.delete.usage";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM_TEXT = Q_COMMAND + "admin.quest.delete.confirmation.text";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM_HOVER = Q_COMMAND + "admin.quest.delete.confirmation.hover";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_DELETE_SUCCESSFUL = Q_COMMAND + "admin.quest.delete.successful";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_LIST_NO_QUESTS = Q_COMMAND + "admin.quest.list.noquests";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_LIST = Q_COMMAND + "admin.quest.list.show.text";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_LIST_HOVER = Q_COMMAND + "admin.quest.list.show.hover";
	public static final String QUESTS_COMMAND_ADMIN_SIGN_USAGE = Q_COMMAND + "admin.sign.usage";
	public static final String QUESTS_COMMAND_ADMIN_SIGN_NOT_SIGN = Q_COMMAND + "admin.sign.notviewing";
	public static final String QUESTS_COMMAND_ADMIN_SIGN_ADDED = Q_COMMAND + "admin.sign.added";

	public static final String QUESTS_COMMAND_ADMIN_NPC_USAGE = Q_COMMAND + "admin.npc.usage";
	public static final String QUESTS_COMMAND_ADMIN_NPC_LIST_USAGE = Q_COMMAND + "admin.npc.list.usage";
	public static final String QUESTS_COMMAND_ADMIN_NPC_LIST_TASK = Q_COMMAND + "admin.npc.list.task.overview";
	public static final String QUESTS_COMMAND_ADMIN_NPC_LIST_TASK_ELEMENT = Q_COMMAND + "admin.npc.list.task.element.general";
	public static final String QUESTS_COMMAND_ADMIN_NPC_LIST_TASK_ELEMENT_STEP = Q_COMMAND + "admin.npc.list.task.element.questnstep";
	public static final String QUESTS_COMMAND_ADMIN_NPC_LIST_FIND = Q_COMMAND + "admin.npc.list.find.overview";
	public static final String QUESTS_COMMAND_ADMIN_NPC_LIST_FIND_ELEMENT = Q_COMMAND + "admin.npc.list.find.element";
	public static final String QUESTS_COMMAND_ADMIN_NPC_FIND_USAGE = Q_COMMAND + "admin.npc.find.usage";
	public static final String QUESTS_COMMAND_ADMIN_NPC_FIND_SUCCESSFUL = Q_COMMAND + "admin.npc.find.successful";
	public static final String QUESTS_COMMAND_ADMIN_NPC_FIND_DELETED = Q_COMMAND + "admin.npc.find.deleted";
	public static final String QUESTS_COMMAND_ADMIN_NPC_TASK_USAGE = Q_COMMAND + "admin.npc.task.usage";
	public static final String QUESTS_COMMAND_ADMIN_NPC_TASK_SUCCESSFUL = Q_COMMAND + "admin.npc.task.successful";
	public static final String QUESTS_COMMAND_ADMIN_NPC_TASK_DELETED = Q_COMMAND + "admin.npc.task.deleted";

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

	// quest find
	public static final String QUESTS_COMMAND_QUEST_FIND_USAGE = Q_COMMAND + "quest.find.usage";
	public static final String QUESTS_COMMAND_QUEST_FIND_ALREADY_FOUND = Q_COMMAND + "quest.find.alreadyfound";
	public static final String QUESTS_COMMAND_QUEST_FIND_ISPUBLIC = Q_COMMAND + "quest.find.ispublic";




	// GUI
	private static final String GUI = QUEST + "gui.";
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


	public static final String QUESTS_GUI_ACTIVE_OVERVIEW = GUI + "active.overview";
	public static final String QUESTS_GUI_ACTIVE_LORE_STEP_COMPLETED = GUI + "active.step.completed";
	public static final String QUESTS_GUI_ACTIVE_LORE_STEP_TODO = GUI + "active.step.todo";
	public static final String QUESTS_GUI_ACTIVE_CANCEL_NAME = GUI + "active.cancel.name";
	public static final String QUESTS_GUI_ACTIVE_CANCEL_LORE = GUI + "active.cancel.lore";
	public static final String QUESTS_GUI_ACTIVE_TIME_LEFT_NAME = GUI + "active.time.name";

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
	public static final String QUESTS_GUI_PUBLIC_INFO_NAME = GUI + "public.info.name";
	public static final String QUESTS_GUI_PUBLIC_INFO_LORE = GUI + "public.info.lore";
	public static final String QUESTS_GUI_PUBLIC_LORE = GUI + "public.lore";
	public static final String QUESTS_GUI_QUEST_DETAILS_FOUND_LORE = GUI + "details.found.lore";
	public static final String QUESTS_GUI_QUEST_DETAILS_COMPLETED_LORE = GUI + "details.completed.lore";

	public static final String QUESTS_GUI_ACCEPT_START_NAME = GUI + "accept.start.name";
	public static final String QUESTS_GUI_ACCEPT_START_LORE = GUI + "accept.start.lore";
	public static final String QUESTS_GUI_ACCEPT_TITLE = GUI + "accept.title";
	public static final String QUESTS_GUI_QUEST_TIME_NAME = GUI + "quest.time.name";
	public static final String QUESTS_GUI_QUEST_TIME_LORE = GUI + "quest.time.lore";
	public static final String QUESTS_GUI_QUEST_STEPS_NAME = GUI + "quest.steps.name";
	public static final String QUESTS_GUI_QUEST_REWARD_NAME_HAS = GUI + "quest.reward.name.has";
	public static final String QUESTS_GUI_QUEST_REWARD_NAME_NONE = GUI + "quest.reward.name.none";
	public static final String QUESTS_GUI_QUEST_DETAILS_ITEM_LORE = GUI + "quest.details.lore";
	public static final String QUESTS_GUI_ACCEPT_REWARD = GUI + "accept.reward";
	public static final String QUESTS_GUI_QUEST_ADMIN_MODIFY_NAME = GUI + "quest.modify.name";
	public static final String QUESTS_GUI_QUEST_ADMIN_MODIFY_LORE = GUI + "quest.modify.lore";
	public static final String QUESTS_GUI_QUEST_ADMIN_DELETE_NAME = GUI + "quest.delete.name";
	public static final String QUESTS_GUI_QUEST_ADMIN_DELETE_LORE = GUI + "quest.delete.lore";


	public static final String QUESTS_GUI_BACK = GUI + "back";

	// GUI REWARDS
	public static final String QUESTS_GUI_REWARDS_PREVIEW_NAME = GUI + "rewards.preview.name";
	public static final String QUESTS_GUI_REWARDS_PREVIEW_LORE = GUI + "rewards.preview.lore";
	public static final String QUESTS_GUI_REWARDS_TITLE = GUI + "rewards.title";



	// STEPS
	public static final String QUESTS_STEP_CRAFT_NORMAL_LINE = QUEST + "step.craft.preview.line";
	public static final String QUESTS_STEP_CRAFT_ACTIVE_LINE = QUEST + "step.craft.active.line";
	public static final String QUESTS_STEP_CRAFT_NORMAL_LORE = QUEST + "step.craft.normal.lore";
	public static final String QUESTS_STEP_CRAFT_ACTIVE_LORE = QUEST + "step.craft.active.lore";
	public static final String QUESTS_STEP_MINE_NORMAL_LINE = QUEST + "step.mine.preview.line";
	public static final String QUESTS_STEP_MINE_ACTIVE_LINE = QUEST + "step.mine.active.line";
	public static final String QUESTS_STEP_MINE_ALREADY_MINED = QUEST + "step.mine.active.alreadymined";
	public static final String QUESTS_STEP_MINE_NORMAL_LORE = QUEST + "step.mine.preview.lore";
	public static final String QUESTS_STEP_MINE_ACTIVE_LORE = QUEST + "step.mine.active.lore";
	public static final String QUESTS_STEP_KILL_NORMAL_LINE = QUEST + "step.kill.preview.line";
	public static final String QUESTS_STEP_KILL_ACTIVE_LINE = QUEST + "step.kill.active.line";
	public static final String QUESTS_STEP_KILL_NORMAL_LORE = QUEST + "step.kill.preview.lore";
	public static final String QUESTS_STEP_KILL_ACTIVE_LORE = QUEST + "step.kill.active.lore";
	public static final String QUESTS_STEP_NPC_NORMAL_LINE = QUEST + "step.npc.preview.line";
	public static final String QUESTS_STEP_NPC_ACTIVE_LINE = QUEST + "step.npc.active.line";
	public static final String QUESTS_STEP_NPC_NORMAL_NAME = QUEST + "step.npc.preview.name";
	public static final String QUESTS_STEP_NPC_NORMAL_LORE = QUEST + "step.npc.preview.lore";
	public static final String QUESTS_STEP_NPC_ACTIVE_LORE = QUEST + "step.npc.active.lore";

	// REWARDS
	public static final String QUESTS_REWARD_COINS_PREVIEW = QUEST + "reward.coins.preview";
	public static final String QUESTS_REWARD_COINS_MESSAGE = QUEST + "reward.coins.message";
	public static final String QUESTS_REWARD_COINS_NAME = QUEST + "reward.coins.name";
	public static final String QUESTS_REWARD_COINS_LORE = QUEST + "reward.coins.lore";

	public static final String QUESTS_REWARD_LVL_PREVIEW = QUEST + "reward.lvl.preview";
	public static final String QUESTS_REWARD_LVL_MESSAGE = QUEST + "reward.lvl.message";
	public static final String QUESTS_REWARD_LVL_NAME = QUEST + "reward.lvl.name";
	public static final String QUESTS_REWARD_LVL_LORE = QUEST + "reward.lvl.lore";

	public static final String QUESTS_REWARD_ITEM_PREVIEW = QUEST + "reward.item.preview";
	public static final String QUESTS_REWARD_ITEM_MESSAGE = QUEST + "reward.item.message";
	public static final String QUESTS_REWARD_ITEM_LORE = QUEST + "reward.item.lore";


}

