package net.playlegend.questsystem.database;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.quest.reward.CoinsReward;
import net.playlegend.questsystem.quest.steps.MineQuestStep;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestDatabaseTest {

    private static final Logger LOG = Logger.getLogger(QuestDatabaseTest.class.getSimpleName());
    QuestDatabase db;

    @Mock
    QuestSystem questSystem;
    MockedStatic<QuestSystem> questSystemStatic;
    @Mock
    FileConfiguration configuration;

    @BeforeEach
    void setUp() throws SQLException {
        var dbConnection = DriverManager.getConnection("jdbc:h2:mem:testcase", "sa", null);
        Statement stmt = dbConnection.createStatement();
        stmt.execute("create table foo (id integer)");
        dbConnection.commit();

        questSystemStatic = mockStatic(QuestSystem.class);
        questSystemStatic.when(QuestSystem::getInstance).thenReturn(questSystem);
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
    public void tearDown() {
        questSystemStatic.close();
    }

    @Test
    public void addQuest_checkAdded() throws SQLException {
        Optional<Integer> questId = db.insertNewQuest("test", "test",
                List.of(new CoinsReward(10)),
                List.of(new MineQuestStep(1, 1, 1, Material.STONE)),
                120, true, false);

        assertTrue(questId.isPresent());

        ResultSet allQuests = db.getAllQuests();
        assertTrue(allQuests.next());
        assertEquals(questId.get(), allQuests.getInt("id"));
    }


}
