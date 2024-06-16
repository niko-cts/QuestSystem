package net.playlegend.questsystem.database.player;

import net.playlegend.questsystem.database.DatabaseHandler;
import net.playlegend.questsystem.database.PlayerInfoDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckTableCreationTest {

    @Test
    public void createTableTest() throws IllegalAccessException, NoSuchFieldException {
        try (MockedStatic<DatabaseHandler> mockStatic = mockStatic(DatabaseHandler.class)) {

            Field instance = DatabaseHandler.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);

            DatabaseHandler databaseHandler = mock(DatabaseHandler.class);
            mockStatic.when(DatabaseHandler::getInstance).thenReturn(databaseHandler);
            PlayerInfoDatabase.getInstance();
            verify(databaseHandler).createTableIfNotExists(anyString(), anyList());
        }
    }

}
