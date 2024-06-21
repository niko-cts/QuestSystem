package net.playlegend.questsystem;

import net.minecraft.util.Tuple;
import net.playlegend.questsystem.translation.DefaultEnglishMessages;
import net.playlegend.questsystem.translation.DefaultGermanMessages;
import net.playlegend.questsystem.translation.LanguageHandler;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AllTranslatedTest {

    @Mock
    LanguageHandler languageHandler;
    @Mock
    QuestSystem questSystem;
    MockedStatic<QuestSystem> questSystemStatic;


    @BeforeEach
    void setUp() {
        questSystemStatic = mockStatic(QuestSystem.class);
        questSystemStatic.when(QuestSystem::getInstance).thenReturn(questSystem);
        when(questSystem.getLanguageHandler()).thenReturn(languageHandler);
    }

    @AfterEach
    public void tearDown() {
        questSystemStatic.close();
    }

    @Test
    public void checkAllConstants_AreTranslated_german() {
        new DefaultGermanMessages();
        ArgumentCaptor<Locale> locale = ArgumentCaptor.forClass(Locale.class);
        ArgumentCaptor<Map<String, String>> messages = ArgumentCaptor.forClass(Map.class);
        verify(languageHandler).cacheAndSaveMessagesToConfig(locale.capture(), messages.capture());
        assertEquals(Locale.GERMAN, locale.getValue());
        for (Tuple<String, String> keyWithFieldName : getKeys()) {
            assertTrue(messages.getValue().containsKey(keyWithFieldName.b()), keyWithFieldName.a() + " is not translated yet (" + keyWithFieldName.b() + ")");
        }
    }
    @Test
    public void checkAllConstants_AreTranslated_english() {
        new DefaultEnglishMessages();
        ArgumentCaptor<Locale> locale = ArgumentCaptor.forClass(Locale.class);
        ArgumentCaptor<Map<String, String>> messages = ArgumentCaptor.forClass(Map.class);
        verify(languageHandler).cacheAndSaveMessagesToConfig(locale.capture(), messages.capture());
        assertEquals(Locale.ENGLISH, locale.getValue());
        for (Tuple<String, String> keyWithFieldName : getKeys()) {
            assertTrue(messages.getValue().containsKey(keyWithFieldName.b()), keyWithFieldName.a() + " is not translated yet (" + keyWithFieldName.b() + ")");
        }
    }

    private List<Tuple<String, String>> getKeys() {
        return Arrays.stream(TranslationKeys.class.getFields())
                .filter(AccessibleObject::trySetAccessible)
                .map(f -> {
                    try {
                        return new Tuple<String, String>(f.getName(), f.get(null).toString());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

}
