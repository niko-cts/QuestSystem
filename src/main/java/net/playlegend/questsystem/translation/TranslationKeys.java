package net.playlegend.questsystem.translation;

/**
 * This class holds key constants that represent a translatable message.
 * @author Niko
 */
public class TranslationKeys {



    private TranslationKeys() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final String QUEST = "quests.";
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
}
