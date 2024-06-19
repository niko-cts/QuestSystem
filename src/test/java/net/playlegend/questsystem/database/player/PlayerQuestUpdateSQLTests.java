package net.playlegend.questsystem.database.player;

import net.playlegend.questsystem.database.DatabaseHandler;
import net.playlegend.questsystem.database.PlayerInfoDatabase;
import net.playlegend.questsystem.player.PlayerDatabaseInformationHolder;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PlayerQuestUpdateSQLTests {

	UUID uuid = UUID.fromString("23375283-2638-4775-8734-7bcfc14c919c");
	Timestamp timestamp = Timestamp.valueOf("2024-06-14 09:05:17.1716341");

	@Mock
	DatabaseHandler databaseHandler;

	@Mock
	QuestPlayer fakePlayer;

	@Mock
	PlayerDatabaseInformationHolder playerDatabaseInformationHolder = new PlayerDatabaseInformationHolder(false);

	MockedStatic<DatabaseHandler> mockStatic;

	PlayerInfoDatabase playerInfoDatabase;


	@BeforeEach
	public void init() {
		mockStatic = mockStatic(DatabaseHandler.class);
		mockStatic.when(DatabaseHandler::getInstance).thenReturn(databaseHandler);

		playerInfoDatabase = PlayerInfoDatabase.getInstance();
		when(fakePlayer.getUniqueId()).thenReturn(uuid);
		when(fakePlayer.getPlayerDbInformationHolder()).thenReturn(playerDatabaseInformationHolder);
	}

	@AfterEach
	public void tearDown() {
		mockStatic.close();
	}

	@Test
	public void newCompletedQuest_withoutLanguage_correctSQL() {
		Map<Integer, Timestamp> map = new HashMap<>();
		map.put(1, timestamp);
		when(playerDatabaseInformationHolder.getNewCompletedQuests()).thenReturn(map);

		assertEquals(
				"INSERT INTO system_quests_player_completed VALUES('23375283-2638-4775-8734-7bcfc14c919c', 1, '2024-06-14 09:05:17.1716341');" +
				"UPDATE system_player SET last_logout='2024-06-14 09:05:17.1716341' WHERE uuid='23375283-2638-4775-8734-7bcfc14c919c' LIMIT 1;",
				playerInfoDatabase.getUpdatePlayerSQLStatements(fakePlayer, timestamp.toInstant()));

	}

	@Test
	public void deleteActiveQuest_correctSQL() {
		Map<Integer, Timestamp> map = new HashMap<>();
		map.put(1, timestamp);

		when(playerDatabaseInformationHolder.isMarkActiveQuestDirty()).thenReturn(true);
		when(fakePlayer.getActivePlayerQuest()).thenReturn(Optional.empty());

		assertEquals(
				"DELETE FROM system_quests_player_active_quest WHERE uuid='23375283-2638-4775-8734-7bcfc14c919c' LIMIT 1;" +
				"DELETE FROM system_quests_player_active_steps WHERE uuid='23375283-2638-4775-8734-7bcfc14c919c';" +
				"UPDATE system_player SET last_logout='2024-06-14 09:05:17.1716341' WHERE uuid='23375283-2638-4775-8734-7bcfc14c919c' LIMIT 1;",
				playerInfoDatabase.getUpdatePlayerSQLStatements(fakePlayer, timestamp.toInstant()));
	}

	@Test
	public void newFoundQuest_language_correctSQL() {
		Map<Integer, Timestamp> map = new HashMap<>();
		map.put(1, timestamp);

		when(fakePlayer.getLanguage()).thenReturn(new Language(Locale.ENGLISH));
		when(playerDatabaseInformationHolder.isMarkLanguageDirty()).thenReturn(true);
		when(playerDatabaseInformationHolder.getNewFoundQuests()).thenReturn(map);

		assertEquals(
				"INSERT INTO system_quests_player_found VALUES('23375283-2638-4775-8734-7bcfc14c919c', 1, '2024-06-14 09:05:17.1716341');" +
				"UPDATE system_player SET last_logout='2024-06-14 09:05:17.1716341', language='en' WHERE uuid='23375283-2638-4775-8734-7bcfc14c919c' LIMIT 1;",
				playerInfoDatabase.getUpdatePlayerSQLStatements(fakePlayer, timestamp.toInstant()));
	}

}
