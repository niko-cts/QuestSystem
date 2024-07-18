package chatzis.nikolas.questsystem;

import chatzis.nikolas.questsystem.translation.DefaultEnglishMessages;
import chatzis.nikolas.questsystem.translation.DefaultGermanMessages;
import chatzis.nikolas.questsystem.translation.LanguageHandler;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import net.minecraft.util.Tuple;
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

import static org.junit.jupiter.api.Assertions.*;
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
	}

	@AfterEach
	public void tearDown() {
		questSystemStatic.close();
	}

	@Test
	public void checkAllConstants_AreTranslated_german() {
		when(questSystem.getLanguageHandler()).thenReturn(languageHandler);

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
		when(questSystem.getLanguageHandler()).thenReturn(languageHandler);

		new DefaultEnglishMessages();
		ArgumentCaptor<Locale> locale = ArgumentCaptor.forClass(Locale.class);
		ArgumentCaptor<Map<String, String>> messages = ArgumentCaptor.forClass(Map.class);
		verify(languageHandler).cacheAndSaveMessagesToConfig(locale.capture(), messages.capture());
		assertEquals(Locale.ENGLISH, locale.getValue());
		for (Tuple<String, String> keyWithFieldName : getKeys()) {
			assertTrue(messages.getValue().containsKey(keyWithFieldName.b()), keyWithFieldName.a() + " is not translated yet (" + keyWithFieldName.b() + ")");
		}
	}

	@Test
	public void check_allKeys_notAPrefix() {
		for (Tuple<String, String> key : getKeys()) {
			for (Tuple<String, String> other : getKeys()) {
				assertFalse(!other.b().equals(key.b()) && other.b().contains(key.b() + "."), "The translation " + key.a() + " [" + key.b() + "] is a prefix of " + other.a() + " [" + other.b() + "]");
			}
		}
	}


	@Test
	public void check_allKeys_unique() {
		for (Tuple<String, String> key : getKeys()) {
			assertEquals(1, getKeys().stream().filter(p -> p.b().equals(key.b())).count(), "The translation key exists more than once: " + key.a());
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
