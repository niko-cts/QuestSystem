package net.playlegend.questsystem.database;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.QuestManager;
import net.playlegend.questsystem.quest.reward.CoinsReward;
import net.playlegend.questsystem.quest.reward.LevelReward;
import net.playlegend.questsystem.quest.steps.KillQuestStep;
import net.playlegend.questsystem.quest.steps.TalkToNPCQuestStep;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestManagerTest {

	private static final Logger LOG = Logger.getLogger(QuestDatabaseTest.class.getSimpleName());


	@Mock
	FileConfiguration configuration;
	@Mock
	QuestSystem questSystem;
	MockedStatic<QuestSystem> questSystemStatic;
	MockedStatic<DriverManager> questDriverManager;
	MockedStatic<QuestDatabase> dbHandlerStatic;

	Connection connection;
	QuestDatabase db;
	QuestManager questManager;

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

		dbHandlerStatic = mockStatic(QuestDatabase.class);
		dbHandlerStatic.when(QuestDatabase::getInstance).thenReturn(db);

		questManager = new QuestManager();
	}

	@AfterEach
	public void tearDown() throws SQLException {
		questSystemStatic.close();
		questDriverManager.close();
		dbHandlerStatic.close();
		connection.close();
	}


	@Test
	public void addQuest() throws SQLException {
		assertTrue(questManager.getQuests().isEmpty());

		UUID talkUUID = UUID.randomUUID();
		questManager.addQuest("testname", "testdescription",
				List.of(new CoinsReward(10)),
				List.of(new TalkToNPCQuestStep(1, 1, 1, talkUUID)),
				3500, true, true);

		assertFalse(questManager.getQuests().isEmpty());
		Quest quest = questManager.getQuests().get(0);
		assertEquals(new Quest(quest.id(), "testname", "testdescription", true,
				List.of(new CoinsReward(10)), List.of(new TalkToNPCQuestStep(1, 1, 1, talkUUID)), 3500, true),
				quest);

	}

	@Test
	public void updateQuest() throws SQLException {
		assertTrue(questManager.getQuests().isEmpty());
		addQuest();
		assertFalse(questManager.getQuests().isEmpty());
		Quest old = questManager.getQuests().get(0);

		LOG.info("Starting update");
		UUID talkUUID = UUID.randomUUID();
		questManager.updateQuest(old.id(),
				"newname", "testdescription",
				List.of(new LevelReward(10)),
				List.of(new TalkToNPCQuestStep(1, 1, 1, talkUUID),
						new KillQuestStep(2, 2, 1, EntityType.BAT)),
				100, false, true);
		assertEquals(1, questManager.getQuests().size());

		Quest quest = questManager.getQuests().get(0);
		assertEquals(new Quest(quest.id(), "newname", "testdescription", false,
						List.of(new LevelReward(10)),
						List.of(new TalkToNPCQuestStep(1, 1, 1, talkUUID), new KillQuestStep(2, 2, 1, EntityType.BAT)),
						100, true),
				quest);

		// reload from db
		questManager = new QuestManager();
		assertEquals(1, questManager.getQuests().size());
		quest = questManager.getQuests().get(0);
		assertEquals(new Quest(quest.id(), "newname", "testdescription", false,
						List.of(new LevelReward(10)),
						List.of(new TalkToNPCQuestStep(1, 1, 1, talkUUID), new KillQuestStep(2, 2, 1, EntityType.BAT)),
						100, true),
				quest);
		questManager = new QuestManager();
	}
}
