package net.playlegend.questsystem.databaseupdate;

import net.playlegend.questsystem.database.DatabaseHandler;
import net.playlegend.questsystem.database.PlayerInfoDatabase;
import net.playlegend.questsystem.player.PlayerDatabaseInformationHolder;
import net.playlegend.questsystem.player.QuestPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteQuestsFromPlayerDatabaseTest extends AbstractDatabaseTest{

    @Mock
    QuestPlayer fakePlayer;

    @Mock
    PlayerDatabaseInformationHolder playerDatabaseInformationHolder = new PlayerDatabaseInformationHolder(false);

    @BeforeEach
    public void init() {
        when(fakePlayer.getUniqueId()).thenReturn(uuid);
        when(fakePlayer.getPlayerDbInformationHolder()).thenReturn(playerDatabaseInformationHolder);
        when(playerDatabaseInformationHolder.isMarkActiveQuestDirty()).thenReturn(true);
        when(fakePlayer.getActivePlayerQuest()).thenReturn(Optional.empty());
    }

    @Test
    public void deleteActiveQuest_correctSQL() {
        Map<Integer, Timestamp> map = new HashMap<>();
        map.put(1, timestamp);

        try (MockedStatic<DatabaseHandler> mockStatic = mockStatic(DatabaseHandler.class)) {
            DatabaseHandler databaseHandler = mock(DatabaseHandler.class);
            mockStatic.when(DatabaseHandler::getInstance).thenReturn(databaseHandler);
            PlayerInfoDatabase playerDb = PlayerInfoDatabase.getInstance();

            verify(databaseHandler).createTableIfNotExists(anyString(), anyList());

            playerDb.updateAllPlayerData(fakePlayer, timestamp.toInstant());

            verify(databaseHandler).executeSQL(
                    "DELETE FROM system_quests_player_active_quest WHERE uuid='23375283-2638-4775-8734-7bcfc14c919c' LIMIT 1;" +
                            "DELETE FROM system_quests_player_active_steps WHERE uuid='23375283-2638-4775-8734-7bcfc14c919c';" +
                            "UPDATE system_player SET last_logout='2024-06-14 09:05:17.1716341' WHERE uuid='23375283-2638-4775-8734-7bcfc14c919c' LIMIT 1;"
            );

        }
    }

}
