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
    public static final String QUESTS_EVENT_FINISHED = QUEST_EVENT + "finished";
    public static final String QUESTS_EVENT_TIMER_EXPIRED = QUEST_EVENT + "timer.expired";
    public static final String QUESTS_EVENT_SWITCHED = QUEST_EVENT + "switched";

}
