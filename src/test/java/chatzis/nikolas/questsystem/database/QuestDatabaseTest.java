package chatzis.nikolas.questsystem.database;

import chatzis.nikolas.questsystem.quest.reward.CoinsReward;
import chatzis.nikolas.questsystem.quest.steps.MineQuestStep;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class QuestDatabaseTest extends AbstractDatabaseTest {

    @Test
    public void addQuest_checkAdded() throws SQLException {
        Optional<Integer> questId = db.insertNewQuest("test", "test", List.of(new CoinsReward(10)), List.of(new MineQuestStep(1, 1, 1, Material.STONE)), 120, true, false);

        assertTrue(questId.isPresent());

        ResultSet allQuests = db.getAllQuests();
        assertTrue(allQuests.next());
        assertEquals(questId.get(), allQuests.getInt("id"));
    }


}
