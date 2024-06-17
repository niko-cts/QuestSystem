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
	public static final String QUESTS_EVENT_CANCELED = QUEST_EVENT + "canceled";
	public static final String QUESTS_EVENT_STARTED = QUEST_EVENT + "started";
	public static final String QUESTS_EVENT_COUNTDOWN = QUEST_EVENT + "timer";
	public static final String QUESTS_EVENT_FOUND_NEW = QUEST_EVENT + "found";

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
	public static final String QUESTS_COMMAND_ADMIN_DESCRIPTION = Q_COMMAND + "admin.description";

	public static final String QUESTS_COMMAND_ADMIN_QUEST_USAGE = Q_COMMAND + "admin.quest.usage";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_DESCRIPTION = Q_COMMAND + "admin.quest.description";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM = Q_COMMAND + "admin.quest.delete.confirmation";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_DELETE_SUCCESSFUL = Q_COMMAND + "admin.quest.delete.successful";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_LIST_NO_QUESTS = Q_COMMAND + "admin.quest.list.noquests";
	public static final String QUESTS_COMMAND_ADMIN_QUEST_LIST = Q_COMMAND + "admin.quest.list.show";

}
