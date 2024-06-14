package net.playlegend.questsystem;

import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.QuestTimingsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestTimingsTest {

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnCorrectDelay() {
        assertEquals(5, QuestTimingsUtil.calculateNextDuration(605));
        assertEquals(115, QuestTimingsUtil.calculateNextDuration(415));
        assertEquals(60, QuestTimingsUtil.calculateNextDuration(240));
        assertEquals(30, QuestTimingsUtil.calculateNextDuration(210));
        assertEquals(25, QuestTimingsUtil.calculateNextDuration(55));
        assertEquals(10, QuestTimingsUtil.calculateNextDuration(25));
        assertEquals(2, QuestTimingsUtil.calculateNextDuration(12));
        assertEquals(1, QuestTimingsUtil.calculateNextDuration(5));

        assertEquals(0, QuestTimingsUtil.calculateNextDuration(600));
        assertEquals(1800, QuestTimingsUtil.calculateNextDuration(3600));
        assertEquals(0, QuestTimingsUtil.calculateNextDuration(600));
    }

    @Mock
    Language mockedLanguage;

    @Test
    public void shouldReturnCorrectTimeInString() {
        Mockito.when(mockedLanguage.translateMessage(TranslationKeys.QUESTS_DISPLAY_DAYS))
                .thenReturn("day(s)");

        Mockito.when(mockedLanguage.translateMessage(TranslationKeys.QUESTS_DISPLAY_HOURS))
                .thenReturn("hour(s)");

        Mockito.when(mockedLanguage.translateMessage(TranslationKeys.QUESTS_DISPLAY_MINUTES))
                .thenReturn("minute(s)");

        Mockito.when(mockedLanguage.translateMessage(TranslationKeys.QUESTS_DISPLAY_SECONDS))
                .thenReturn("second(s)");

        assertEquals("1 day(s) 10 hour(s) ", QuestTimingsUtil.convertSecondsToDHMS(
                mockedLanguage, 3600 * 24 + 3600 * 10 + 251
        ));


        assertEquals("10 hour(s) 5 minute(s) ", QuestTimingsUtil.convertSecondsToDHMS(
                mockedLanguage, 3600 * 10 + 60 * 5 + 10
        ));


        assertEquals("7 minute(s) ", QuestTimingsUtil.convertSecondsToDHMS(
                mockedLanguage, 60 * 7
        ));


        assertEquals("7 minute(s) 30 second(s)", QuestTimingsUtil.convertSecondsToDHMS(
                mockedLanguage, 60 * 7 + 30
        ));
    }

    @Test
    public void calculatedInstantLeftAfterLogin_shouldBeCorrect() {
        assertEquals(Instant.now().plusSeconds(600),
                QuestTimingsUtil.calculatedInstantLeftAfterLogin(600, false,
                        Timestamp.from(Instant.now().minusSeconds(300))));
        assertEquals(Instant.now().plusSeconds(300),
                QuestTimingsUtil.calculatedInstantLeftAfterLogin(600, true,
                        Timestamp.from(Instant.now().minusSeconds(300))));
    }
}
