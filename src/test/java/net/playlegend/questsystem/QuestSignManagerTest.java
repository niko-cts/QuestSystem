package net.playlegend.questsystem;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.playlegend.questsystem.quest.QuestSignManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestSignManagerTest {

    @Mock
    QuestSystem questSystem;

    QuestSignManager questSignManager;

    File file = new File("resources/signs.txt");

    ServerMock server;
    WorldMock worldMock;

    @BeforeEach
    public void setUp() {
        when(questSystem.getDataFolder()).thenReturn(new File("resources/"));
        server = MockBukkit.mock();
        worldMock = server.addSimpleWorld("world");
    }

    @AfterEach
    public void onShutdown() {
        MockBukkit.unmock();
    }

    @AfterAll
    public static void deleteFile() {
        new File("resources/signs.txt").delete();
    }

    @Test
    public void onInstantiating_checkFileCreated() {
        questSignManager = new QuestSignManager(questSystem);
        assertTrue(file.exists());
    }


    @Test
    public void fillTxt_Instantiate_checkListHasElements() throws IOException, IllegalAccessException, NoSuchFieldException {
        Files.write(file.toPath(), List.of("world,0,0,0", "world,0,0,1"));
        worldMock.getBlockAt(0, 0, 0).setType(Material.ACACIA_SIGN);
        worldMock.getBlockAt(0, 0, 1).setType(Material.ACACIA_SIGN);

        questSignManager = new QuestSignManager(questSystem);

        Field signs = QuestSignManager.class.getDeclaredField("signs");
        signs.setAccessible(true);
        List<Location> locations = (List<Location>) signs.get(questSignManager);
        assertEquals(2, locations.size());
        assertEquals(2, Files.readAllLines(file.toPath()).size());
    }


    @Test
    public void fillTxt_Instantiate_checkListHasElements_OneIllegal_Deleted() throws IOException, IllegalAccessException, NoSuchFieldException {
        Files.write(file.toPath(), List.of("world,0,0,0", "world,0,0,1"));
        worldMock.getBlockAt(0, 0, 0).setType(Material.ACACIA_SIGN);

        questSignManager = new QuestSignManager(questSystem);

        Field signs = QuestSignManager.class.getDeclaredField("signs");
        signs.setAccessible(true);
        List<Location> locations = (List<Location>) signs.get(questSignManager);
        assertEquals(1, locations.size());
        assertEquals(1, Files.readAllLines(file.toPath()).size());
    }


    @Test
    public void fillTxt_instantiate_deleteOne_checkDeleted() throws IOException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Files.write(file.toPath(), List.of("world,0,0,0", "world,0,0,1"));
        worldMock.getBlockAt(0, 0, 0).setType(Material.ACACIA_SIGN);
        worldMock.getBlockAt(0, 0, 1).setType(Material.ACACIA_SIGN);

        questSignManager = new QuestSignManager(questSystem);

        Location location = new Location(worldMock, 0, 0, 0);

        Field signs = QuestSignManager.class.getDeclaredField("signs");
        signs.setAccessible(true);
        List<Location> locations = (List<Location>) signs.get(questSignManager);
        assertEquals(2, locations.size());

        assertTrue(locations.contains(location));

        Method method = QuestSignManager.class.getDeclaredMethod("deleteSignIfExists", Location.class);
        method.setAccessible(true);
        method.invoke(questSignManager, location);

        assertEquals(1, Files.readAllLines(file.toPath()).size());
    }

    @Test
    public void fillTxt_instantiate_addElement() throws IOException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Files.write(file.toPath(), List.of("world,0,0,0", "world,0,0,1"));
        worldMock.getBlockAt(0, 0, 0).setType(Material.ACACIA_SIGN);
        worldMock.getBlockAt(0, 0, 1).setType(Material.ACACIA_SIGN);

        questSignManager = new QuestSignManager(questSystem);

        Location location = new Location(worldMock, 0, 0, 2);

        Field signs = QuestSignManager.class.getDeclaredField("signs");
        signs.setAccessible(true);
        List<Location> locations = (List<Location>) signs.get(questSignManager);
        assertEquals(2, locations.size());
        assertEquals(2, Files.readAllLines(file.toPath()).size());

        Method method = QuestSignManager.class.getDeclaredMethod("addNewSign", Location.class);
        method.setAccessible(true);
        method.invoke(questSignManager, location);

        assertEquals(3, Files.readAllLines(file.toPath()).size());
    }

    @Test
    public void fillTxt_instantiate_deleteEvent() throws IOException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Files.write(file.toPath(), List.of("world,0,0,0", "world,0,0,1"));
        worldMock.getBlockAt(0, 0, 0).setType(Material.ACACIA_SIGN);
        worldMock.getBlockAt(0, 0, 1).setType(Material.ACACIA_SIGN);

        questSignManager = new QuestSignManager(questSystem);

        Location location = new Location(worldMock, 0, 0, 0);

        Field signs = QuestSignManager.class.getDeclaredField("signs");
        signs.setAccessible(true);
        List<Location> locations = (List<Location>) signs.get(questSignManager);
        assertEquals(2, locations.size());

        assertTrue(locations.contains(location));

        BlockMock blockAt = worldMock.getBlockAt(0, 0, 0);
        PlayerMock playerMock = server.addPlayer();
        questSignManager.onBlockBreak(new BlockBreakEvent(blockAt, playerMock));

        assertFalse(locations.contains(location));
        assertEquals(1, Files.readAllLines(file.toPath()).size());
    }
}
