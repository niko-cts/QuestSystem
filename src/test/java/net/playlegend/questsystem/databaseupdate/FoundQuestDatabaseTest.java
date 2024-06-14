package net.playlegend.questsystem.databaseupdate;

import net.playlegend.questsystem.database.DatabaseHandler;
import net.playlegend.questsystem.database.PlayerInfoDatabase;
import net.playlegend.questsystem.player.PlayerDatabaseInformationHolder;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class FoundQuestDatabaseTest extends AbstractDatabaseTest {

    @Mock
    QuestPlayer fakePlayer;

    @Mock
    PlayerDatabaseInformationHolder playerDatabaseInformationHolder = new PlayerDatabaseInformationHolder(false);

    @BeforeEach
    public void init() {
        when(fakePlayer.getUniqueId()).thenReturn(uuid);
        when(fakePlayer.getPlayerDbInformationHolder()).thenReturn(playerDatabaseInformationHolder);
        when(fakePlayer.getCurrentLanguage()).thenReturn(new Language(Locale.ENGLISH));
        when(playerDatabaseInformationHolder.isMarkLanguageDirty()).thenReturn(true);

    }
    @Test
    public void newFoundQuest_language_correctSQL() {
        Map<Integer, Timestamp> map = new HashMap<>();
        map.put(1, timestamp);

        when(playerDatabaseInformationHolder.getNewFoundQuests()).thenReturn(map);

        try (MockedStatic<DatabaseHandler> mockStatic = mockStatic(DatabaseHandler.class)) {
            DatabaseHandler databaseHandler = mock(DatabaseHandler.class);
            mockStatic.when(DatabaseHandler::getInstance).thenReturn(databaseHandler);
            PlayerInfoDatabase playerDb = PlayerInfoDatabase.getInstance();

            playerDb.updateAllPlayerData(fakePlayer, timestamp.toInstant());

            verify(databaseHandler).executeSQL(
                    "INSERT INTO system_quests_player_found VALUES('23375283-2638-4775-8734-7bcfc14c919c', 1, '2024-06-14 09:05:17.1716341');" +
                            "UPDATE system_player SET last_logout='2024-06-14 09:05:17.1716341', language='en' WHERE uuid='23375283-2638-4775-8734-7bcfc14c919c' LIMIT 1;"
            );
        }
    }


}
