package chatzis.nikolas.questsystem.util;

import chatzis.nikolas.questsystem.player.QuestTimerPlayer;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class QuestTimingsUtil {

    private QuestTimingsUtil() {
        throw new UnsupportedOperationException("QuestTimingsUtil cannot be instantiated");
    }

    /**
     * Calculates the delay the runnable should have to get the output the next countdown interval.
     * E.g. If seconds left is 610, the next notification should appear in 10 seconds.
     *
     * @param secondsLeft long - the total seconds left.
     * @return long - the next interval in seconds
     * @see QuestTimerPlayer
     */
    public static long calculateNextDuration(long secondsLeft) {
        if (secondsLeft >= 3600 && secondsLeft % 1800 == 0) return 1800; // every 30 min
        if (secondsLeft > 1800) return secondsLeft % 1800; // remainder of 30 min

        if (secondsLeft >= 1200 && secondsLeft % 600 == 0) return 600; // every 10 min
        if (secondsLeft > 600) return secondsLeft % 600; // remainder of 10 min

        if (secondsLeft == 600) return 300; // 5 mins
        if (secondsLeft > 300) return secondsLeft % 300; // remainder of 5 min

        if (secondsLeft >= 120 && secondsLeft % 60 == 0) return 60; // every 1 min
        if (secondsLeft > 60) return secondsLeft % 60; // remainder of 1 min

        if (secondsLeft == 60) return 30; // 30 seconds
        if (secondsLeft > 30) return secondsLeft % 30; // remainder of 30 seconds

        if (secondsLeft == 30) return 15; // 15 seconds
        if (secondsLeft > 15) return secondsLeft % 15; // remainder of 15 seconds

        if (secondsLeft == 15) return 5; // 5 seconds
        if (secondsLeft > 10) return secondsLeft % 10; // remainder of 10 seconds

        return 1; // last 10 seconds, run every second
    }


    /**
     * Converts the given seconds into a String splitting up to days till seconds.
     *
     * @param language     Language - the language to translate the time units
     * @param totalSeconds long - the total seconds.
     * @return String - the converted seconds
     */
    public static String convertSecondsToDHMS(Language language, long totalSeconds) {
        long days = totalSeconds / 86400;
        totalSeconds %= 86400;
        long hours = totalSeconds / 3600;
        totalSeconds %= 3600;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        StringBuilder builder = new StringBuilder();
        if (days != 0)
            builder.append(days).append(" ").append(language.translateMessage(TranslationKeys.QUESTS_DISPLAY_DAYS));
        if (hours != 0)
            builder.append(builder.isEmpty() ? "" : " ").append(hours).append(" ").append(language.translateMessage(TranslationKeys.QUESTS_DISPLAY_HOURS));
        if (minutes != 0 && days == 0)
            builder.append(builder.isEmpty() ? "" : " ").append(minutes).append(" ").append(language.translateMessage(TranslationKeys.QUESTS_DISPLAY_MINUTES));
        if ((days == 0 && hours == 0) && (minutes == 0 || seconds != 0)) {
            builder.append(builder.isEmpty() ? "" : " ").append(seconds).append(" ").append(language.translateMessage(TranslationKeys.QUESTS_DISPLAY_SECONDS));
        }

        return builder.toString();
    }

    /**
     * Calculates a new instant based on the seconds left minus (if runs offline) the time away.
     *
     * @param secondsLeft long - the seconds till the quest expires
     * @param runsOffline boolean - the countdown runs in offline, if true will subtract the duration (last login until now) from the secondsLeft
     * @param lastlogin   Timestamp - Last login of the player.
     * @return Instant - the new instant when the quest finishes
     */
    public static Instant calculatedInstantLeftAfterLogin(long secondsLeft, boolean runsOffline, Timestamp lastlogin) {
        // Get the current time
        Instant now = Instant.now();

        if (runsOffline) {
            // Convert lastlogin to Instant
            Instant lastLoginInstant = lastlogin.toInstant();

            // Calculate the duration between last login and now
            Duration timeAway = Duration.between(lastLoginInstant, now);

            // Subtract the duration from the secondsLeft
            secondsLeft -= timeAway.getSeconds();
        }

        // Calculate the new instant when the quest finishes
        return now.plusSeconds(secondsLeft + 1);
    }

    public static String formatDateTime(Instant time) {
        ZonedDateTime zonedDateTime = time.atZone(ZoneId.systemDefault());
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(zonedDateTime);
    }
}
