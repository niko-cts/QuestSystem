package net.playlegend.questsystem.listener;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.player.PlayerDatabaseInformationHolder;
import net.playlegend.questsystem.player.PlayerHandler;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.reward.QuestReward;
import net.playlegend.questsystem.quest.steps.MineQuestStep;
import net.playlegend.questsystem.quest.steps.QuestStep;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestStepCompletedTest {

    @Mock
    PlayerHandler playerHandler;
    @Mock
    Player player;

    @Mock
    QuestPlayer questPlayer;
    @Mock
    QuestSystem questSystem;

    QuestStepListener questStepListener;
    ServerMock server;
    WorldMock worldMock;

    ActivePlayerQuest activePlayerQuest;
    Map<QuestStep<?>, Integer> stepsWithAmount = new HashMap<>();
    List<QuestStep<?>> completionSteps = new ArrayList<>();
    List<QuestReward<?>> rewards = new ArrayList<>();

    private MockedStatic<QuestSystem> questSystemStatic;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        questSystemStatic = mockStatic(QuestSystem.class);
        questSystemStatic.when(QuestSystem::getInstance).thenReturn(questSystem);
        questStepListener = new QuestStepListener(playerHandler);
        server = MockBukkit.mock();
        worldMock = server.addSimpleWorld("mocked");
        UUID uuid = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(uuid);
        when(playerHandler.getPlayer(uuid)).thenReturn(questPlayer);
        activePlayerQuest = new ActivePlayerQuest(
                new Quest(
                        0, "", "", true, rewards, completionSteps, 10000, false
                ), stepsWithAmount, Instant.now().plusSeconds(10000));

        when(questPlayer.getActivePlayerQuest()).thenReturn(Optional.of(activePlayerQuest));

        Field playerDbInformationHolder = QuestPlayer.class.getDeclaredField("playerDbInformationHolder");
        playerDbInformationHolder.setAccessible(true);
        playerDbInformationHolder.set(questPlayer, new PlayerDatabaseInformationHolder(false));
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
        questSystemStatic.close();
    }

    @Test
    public void questCompleted_OnBlockbreak() {
        when(questPlayer.playerDidQuestStep(any(), any())).thenCallRealMethod();
        doNothing().when(questPlayer).questUpdateEvent(any());

        BlockMock block = worldMock.getBlockAt(0, 0, 0);
        block.setType(Material.STONE);
        QuestStep<?> questStep = new MineQuestStep(0, 0, 1, block.getType());
        stepsWithAmount.put(questStep, 0);
        completionSteps.add(questStep);

        questStepListener.onMine(new BlockBreakEvent(block, player));

        verify(questPlayer).checkAndFinishActiveQuest();
        assertTrue(activePlayerQuest.isQuestFinished());
    }


    @Test
    public void questIncompleted_OnMineWrongBlock() {
        BlockMock block = worldMock.getBlockAt(0, 0, 0);
        block.setType(Material.STONE);
        QuestStep<?> questStep = new MineQuestStep(0, 0, 1, Material.GRASS_BLOCK);
        stepsWithAmount.put(questStep, 0);
        completionSteps.add(questStep);

        questStepListener.onMine(new BlockBreakEvent(block, player));

        verify(questPlayer, never()).playerDidQuestStep(any(), any());
        verify(questPlayer, never()).checkAndFinishActiveQuest();
        assertFalse(activePlayerQuest.isQuestFinished());
    }

    @Test
    public void questStepCompleted_OnMineCorrectBlock_QuestUncompleted() {
        doNothing().when(questPlayer).questUpdateEvent(any());
        when(questPlayer.playerDidQuestStep(any(), any())).thenCallRealMethod();
        BlockMock block = worldMock.getBlockAt(0, 0, 0);
        block.setType(Material.STONE);
        QuestStep<?> questStep = new MineQuestStep(0, 0, 1, block.getType());
        stepsWithAmount.put(questStep, 0);
        completionSteps.add(questStep);

        QuestStep<?> questStep2 = new MineQuestStep(0, 1, 1, Material.GLASS);
        stepsWithAmount.put(questStep2, 0);
        completionSteps.add(questStep2);

        questStepListener.onMine(new BlockBreakEvent(block, player));

        verify(questPlayer).checkAndFinishActiveQuest();
        assertFalse(activePlayerQuest.isQuestFinished());
    }


    @Test
    public void questStepCompleted_OnMineCorrectBlock_twoStepsIncreased() {
        doNothing().when(questPlayer).questUpdateEvent(any());
        when(questPlayer.playerDidQuestStep(any(), any())).thenCallRealMethod();
        BlockMock block = worldMock.getBlockAt(0, 0, 0);
        block.setType(Material.STONE);
        QuestStep<?> questStep = new MineQuestStep(0, 0, 1, block.getType());
        stepsWithAmount.put(questStep, 0);
        completionSteps.add(questStep);

        QuestStep<?> questStep2 = new MineQuestStep(0, 0, 2, block.getType());
        stepsWithAmount.put(questStep2, 0);
        completionSteps.add(questStep2);

        questStepListener.onMine(new BlockBreakEvent(block, player));

        assertFalse(activePlayerQuest.isQuestFinished());

        questStepListener.onMine(new BlockBreakEvent(block, player));

        verify(questPlayer, times(2)).checkAndFinishActiveQuest();
        assertTrue(activePlayerQuest.isQuestFinished());
    }


}
