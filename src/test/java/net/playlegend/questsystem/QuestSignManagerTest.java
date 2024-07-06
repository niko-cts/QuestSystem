package net.playlegend.questsystem;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import net.playlegend.questsystem.quest.QuestSignManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestSignManagerTest {

    private static final Logger LOG = Logger.getLogger(QuestSignManagerTest.class.getSimpleName());
    private static final File SIGN_FILE = new File("signs.txt");

    @Mock
    QuestSystem questSystem;

    QuestSignManager questSignManager;


    ServerMock server;
    WorldMock worldMock;
    @Mock Player player;


    @BeforeEach
    public void setUp() {
        when(questSystem.getDataFolder()).thenReturn(new File(""));
        server = MockBukkit.mock();
        worldMock = server.addSimpleWorld("world");
    }

    @AfterEach
    public void onShutdown() {
        MockBukkit.unmock();
    }

    @AfterAll
    public static void deleteFile() {
        SIGN_FILE.delete();
    }

    @Test
    public void onInstantiating_checkFileCreated() {
        when(questSystem.getLogger()).thenReturn(LOG);
        questSignManager = new QuestSignManager(questSystem);
        assertTrue(SIGN_FILE.exists());
    }


    @Test
    public void fillTxt_Instantiate_checkListHasElements() throws IOException, IllegalAccessException, NoSuchFieldException {
        Files.write(SIGN_FILE.toPath(), List.of("world,0,0,0", "world,0,0,1"), StandardOpenOption.CREATE);
        worldMock.getBlockAt(0, 0, 0).setType(Material.ACACIA_SIGN);
        worldMock.getBlockAt(0, 0, 1).setType(Material.ACACIA_SIGN);

        questSignManager = new QuestSignManager(questSystem);

        Field signs = QuestSignManager.class.getDeclaredField("signs");
        signs.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<Location> locations = (Set<Location>) signs.get(questSignManager);
        assertEquals(2, locations.size());
        assertEquals(2, Files.readAllLines(SIGN_FILE.toPath()).size());
    }


    @Test
    public void fillTxt_Instantiate_checkListHasElements_OneIllegal_Deleted() throws IOException, IllegalAccessException, NoSuchFieldException {
        when(questSystem.getLogger()).thenReturn(LOG);
        Files.write(SIGN_FILE.toPath(), List.of("world,0,0,0", "world,0,0,1"));
        worldMock.getBlockAt(0, 0, 0).setType(Material.ACACIA_SIGN);

        questSignManager = new QuestSignManager(questSystem);

        Field signs = QuestSignManager.class.getDeclaredField("signs");
        signs.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<Location> locations = (Set<Location>) signs.get(questSignManager);
        assertEquals(1, locations.size());
        assertEquals(1, Files.readAllLines(SIGN_FILE.toPath()).size());
    }


    @Test
    public void fillTxt_instantiate_deleteOne_checkDeleted() throws IOException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Files.write(SIGN_FILE.toPath(), List.of("world,0,0,0", "world,0,0,1"));
        worldMock.getBlockAt(0, 0, 0).setType(Material.ACACIA_SIGN);
        worldMock.getBlockAt(0, 0, 1).setType(Material.ACACIA_SIGN);

        questSignManager = new QuestSignManager(questSystem);

        Location location = new Location(worldMock, 0, 0, 0);

        Field signs = QuestSignManager.class.getDeclaredField("signs");
        signs.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<Location> locations = (Set<Location>) signs.get(questSignManager);
        assertEquals(2, locations.size());

        assertTrue(locations.contains(location));

        Method method = QuestSignManager.class.getDeclaredMethod("deleteSign", Location.class);
        method.setAccessible(true);
        method.invoke(questSignManager, location);

        assertEquals(1, Files.readAllLines(SIGN_FILE.toPath()).size());
    }

    @Test
    public void fillTxt_instantiate_addElement() throws IOException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Files.write(SIGN_FILE.toPath(), List.of("world,0,0,0", "world,0,0,1"));
        worldMock.getBlockAt(0, 0, 0).setType(Material.ACACIA_SIGN);
        worldMock.getBlockAt(0, 0, 1).setType(Material.ACACIA_SIGN);

        questSignManager = new QuestSignManager(questSystem);

        Location location = new Location(worldMock, 0, 0, 2);

        Field signs = QuestSignManager.class.getDeclaredField("signs");
        signs.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<Location> locations = (Set<Location>) signs.get(questSignManager);
        assertEquals(2, locations.size());
        assertEquals(2, Files.readAllLines(SIGN_FILE.toPath()).size());

        Method method = QuestSignManager.class.getDeclaredMethod("addNewSign", Location.class);
        method.setAccessible(true);
        method.invoke(questSignManager, location);

        List<String> newLocations = Files.readAllLines(SIGN_FILE.toPath());
        assertEquals(3, newLocations.size());
        assertEquals(List.of("world,0,0,0", "world,0,0,1", "world,0,0,2"), newLocations);
    }
    @Test
    public void fillTxt_instantiate_deleteEvent() throws IOException, IllegalAccessException, NoSuchFieldException {
        Files.write(SIGN_FILE.toPath(), List.of("world,0,0,0", "world,0,0,1"));
        worldMock.getBlockAt(0, 0, 0).setType(Material.ACACIA_SIGN);
        worldMock.getBlockAt(0, 0, 1).setType(Material.ACACIA_SIGN);

        questSignManager = new QuestSignManager(questSystem);

        Location location = new Location(worldMock, 0, 0, 0);

        Field signs = QuestSignManager.class.getDeclaredField("signs");
        signs.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<Location> locations = (Set<Location>) signs.get(questSignManager);
        assertEquals(2, locations.size());
        assertEquals(2, Files.readAllLines(SIGN_FILE.toPath()).size());

        assertTrue(locations.contains(location));

        questSignManager.onBlockBreak(new BlockBreakEvent(worldMock.getBlockAt(0, 0, 0), player));

        assertFalse(locations.contains(location));
        assertEquals(1, locations.size());
        // assertEquals(1, Files.readAllLines(SIGN_FILE.toPath()).size()); cant be checked cause async
    }
}
