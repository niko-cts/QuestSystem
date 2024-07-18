package chatzis.nikolas.questsystem.database;

import chatzis.nikolas.mc.nikoapi.util.ReflectionHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class TablesCreatedTest extends AbstractDatabaseTest {

	MockedStatic<QuestDatabase> dbHandlerStatic;

	@BeforeEach
	void setUp() throws SQLException {
		super.setUp();
		dbHandlerStatic = mockStatic(QuestDatabase.class);
		dbHandlerStatic.when(QuestDatabase::getInstance).thenReturn(db);
	}

	@AfterEach
	public void tearDown() throws SQLException, NoSuchFieldException, IllegalAccessException {
		super.tearDown();
		dbHandlerStatic.close();
	}

	@Test
	public void check_tables_exist() {
		assertTrue(DatabaseHandler.getInstance().doesTableExist(ReflectionHelper.get(QuestDatabase.class, null, "TABLE_QUESTS")), "Quest table does not exist");
		assertTrue(DatabaseHandler.getInstance().doesTableExist(ReflectionHelper.get(QuestDatabase.class, null, "TABLE_QUEST_STEPS_INFO")), "Quest step table does not exist");
		assertTrue(DatabaseHandler.getInstance().doesTableExist(ReflectionHelper.get(QuestDatabase.class, null, "TABLE_QUEST_REWARD_INFO")), "Quest reward table does not exist");
	}
}
