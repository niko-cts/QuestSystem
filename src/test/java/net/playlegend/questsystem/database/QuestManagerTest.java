package net.playlegend.questsystem.database;

import chatzis.nikolas.questsystem.database.QuestDatabase;
import chatzis.nikolas.questsystem.quest.Quest;
import chatzis.nikolas.questsystem.quest.QuestManager;
import chatzis.nikolas.questsystem.quest.reward.CoinsReward;
import chatzis.nikolas.questsystem.quest.reward.LevelReward;
import chatzis.nikolas.questsystem.quest.steps.KillQuestStep;
import chatzis.nikolas.questsystem.quest.steps.TalkToNPCQuestStep;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class QuestManagerTest  extends AbstractDatabaseTest {

	MockedStatic<QuestDatabase> dbHandlerStatic;
	QuestManager questManager;

	@BeforeEach
	void setUp() throws SQLException {
		super.setUp();
		dbHandlerStatic = mockStatic(QuestDatabase.class);
		dbHandlerStatic.when(QuestDatabase::getInstance).thenReturn(db);

		questManager = new QuestManager();
	}

	@AfterEach
	public void tearDown() throws SQLException, NoSuchFieldException, IllegalAccessException {
		super.tearDown();
		dbHandlerStatic.close();
	}

	@Test
	public void addQuest() {
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
	public void addQuest_withMultipleSpeaks() {
		assertTrue(questManager.getQuests().isEmpty());

		UUID talkUUID = UUID.randomUUID();
		UUID talk2UUID = UUID.randomUUID();
		questManager.addQuest("testname", "testdescription",
				List.of(new LevelReward(10)),
				List.of(new TalkToNPCQuestStep(1, 1, 1, talkUUID),
						new TalkToNPCQuestStep(2, 2, 3, talk2UUID)),
				500, false, true);

		assertFalse(questManager.getQuests().isEmpty());
		Quest quest = questManager.getQuests().get(0);
		Quest expected = new Quest(quest.id(), "testname", "testdescription", false,
				List.of(new LevelReward(10)),
				List.of(new TalkToNPCQuestStep(1, 1, 1, talkUUID),
						new TalkToNPCQuestStep(2, 2, 3, talk2UUID)),
				500, true);
		assertEquals(expected, quest);


		// reload db
		questManager = new QuestManager();
		assertEquals(1, questManager.getQuests().size());
		assertEquals(expected, questManager.getQuests().get(0));
	}

	@Test
	public void updateQuest() {
		assertTrue(questManager.getQuests().isEmpty());

		questManager.addQuest("testname", "testdescription",
				List.of(new CoinsReward(10)),
				List.of(new TalkToNPCQuestStep(1, 1, 1, UUID.randomUUID())),
				3500, true, true);

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

		// reload db
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
