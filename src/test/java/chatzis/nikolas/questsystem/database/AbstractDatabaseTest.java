package chatzis.nikolas.questsystem.database;

import chatzis.nikolas.questsystem.QuestSystem;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractDatabaseTest {

	static final Logger LOG = Logger.getLogger(QuestDatabaseTest.class.getSimpleName());
	QuestDatabase db;

	@Mock
	FileConfiguration configuration;
	@Mock
	QuestSystem questSystem;
	MockedStatic<QuestSystem> questSystemStatic;
	MockedStatic<DriverManager> questDriverManager;
	Connection connection;

	@BeforeEach
	void setUp() throws SQLException {
		Properties info = new Properties();
		info.put("user", "sa");
		info.put("allowMultiQueries", true);
		connection = DriverManager.getConnection("jdbc:h2:mem:testcase", info);

		questSystemStatic = mockStatic(QuestSystem.class);
		questSystemStatic.when(QuestSystem::getInstance).thenReturn(questSystem);
		questDriverManager = mockStatic(DriverManager.class);
		questDriverManager.when(() -> DriverManager.getConnection(anyString())).thenReturn(connection);

		when(questSystem.getConfig()).thenReturn(configuration);
		when(questSystem.getLogger()).thenReturn(LOG);
		when(configuration.getString("database.host")).thenReturn("localhost");
		when(configuration.getString("database.user")).thenReturn("h2");
		when(configuration.getString("database.dbname")).thenReturn("");
		when(configuration.getString("database.pass")).thenReturn("");
		when(configuration.getInt("database.port")).thenReturn(3306);
		db = QuestDatabase.getInstance();
	}

	@AfterEach
	public void tearDown() throws SQLException, NoSuchFieldException, IllegalAccessException {
		questSystemStatic.close();
		questDriverManager.close();
		connection.close();
		for(Field field : new Field[]{
				QuestDatabase.class.getDeclaredField("instance"),
				DatabaseHandler.class.getDeclaredField("instance")
		}) {
			field.setAccessible(true);
			field.set(null, null);
		}
	}
}
