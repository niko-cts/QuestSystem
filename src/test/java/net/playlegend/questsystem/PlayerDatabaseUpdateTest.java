package net.playlegend.questsystem;

import net.playlegend.questsystem.database.DatabaseHandler;
import net.playlegend.questsystem.database.PlayerInfoDatabase;
import net.playlegend.questsystem.player.PlayerDatabaseInformationHolder;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class PlayerDatabaseUpdateTest {

    @Mock
    QuestPlayer fakePlayer;
    @Mock
    DatabaseHandler databaseHandler;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void tryUpdateAndExpectCorrectSQL() {
        Mockito.mockStatic(DatabaseHandler.class).when(DatabaseHandler::getInstance).thenReturn(databaseHandler);
        PlayerInfoDatabase playerDb = PlayerInfoDatabase.getInstance();
        UUID uuid = UUID.randomUUID();
        when(fakePlayer.getPlayer().getUniqueId()).thenReturn(uuid);

        PlayerDatabaseInformationHolder playerDatabaseInformationHolder = new PlayerDatabaseInformationHolder(false);
        playerDatabaseInformationHolder.getNewCompletedQuests().put(1, Timestamp.from(Instant.now()));
        when(playerDatabaseInformationHolder.isMarkLanguageDirty()).thenReturn(true);
        when(fakePlayer.getCurrentLanguage()).thenReturn(new Language(Locale.ENGLISH));
        when(fakePlayer.getPlayerDatabaseInformationHolder()).thenReturn(playerDatabaseInformationHolder);


        playerDb.updateAllPlayerData(fakePlayer);


    }

}
