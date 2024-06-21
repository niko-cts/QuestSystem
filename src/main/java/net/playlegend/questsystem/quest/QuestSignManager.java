package net.playlegend.questsystem.quest;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Loads, adds, removes all quest signs.
 * These are stored in a txt file and will be loaded at initialization.
 *
 * @author Niko
 */
public class QuestSignManager extends APISubCommand implements Listener {

    private static final Set<Material> WHITELISTED_SIGNS = Arrays.stream(Material.values()).filter(m -> m.name().contains("SIGN"))
            .collect(Collectors.toSet());

    private final QuestSystem questSystem;
    private final File signFile;
    private final List<Location> signs;

    public QuestSignManager(QuestSystem questSystem) {
        super("sign", TranslationKeys.QUESTS_COMMAND_ADMIN_SIGN_USAGE);
        this.questSystem = questSystem;
        this.signs = new ArrayList<>();
        signFile = new File(questSystem.getDataFolder().getAbsoluteFile(), "signs.txt");
        try {
            if (!signFile.exists()) {
                if (signFile.createNewFile())
                    questSystem.getLogger().info("Created new sign file");
            } else {
                List<Location> toDelete = new ArrayList<>();
                for (String s : Files.readAllLines(signFile.toPath())) {
                    String[] locationString = s.split(",");
                    if (locationString.length != 4) {
                        questSystem.getLogger().log(Level.SEVERE, "Invalid sign file format {0}", s);
                        continue;
                    }
                    Location location = new Location(Bukkit.getWorld(locationString[0]),
                            Integer.parseInt(locationString[1]),
                            Integer.parseInt(locationString[2]),
                            Integer.parseInt(locationString[3]));
                    if (WHITELISTED_SIGNS.contains(location.getBlock().getType())) {
                        signs.add(location);
                    } else {
                        toDelete.add(location);
                    }
                }
                if (!toDelete.isEmpty()) {
                    questSystem.getLogger().log(Level.INFO, "Deleting illegal sign-positions {0}", toDelete.size());
                    toDelete.forEach(this::deleteSignIfExists);
                }
            }
        } catch (IOException exception) {
            questSystem.getLogger().log(Level.SEVERE, "Could not create Sign config", exception);
        }
    }


    public void updateSign(QuestPlayer questPlayer) {
        // todo
    }

    private void addNewSign(Location location) {
        signs.add(location);
        try {
            Files.write(signFile.toPath(), signs.stream().map(this::locationToString).toList(),
                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//                    new FileOutputStream(signFile), StandardCharsets.UTF_8
//            ));
//            writer.write(locationToString(location));
//            writer.close();
        } catch (IOException exception) {
            questSystem.getLogger().log(Level.SEVERE, "Could not add sign location", exception);
        }
    }

    private void deleteSignIfExists(Location location) {
        signs.remove(location);

        try {
            Files.write(signFile.toPath(), signs.stream().map(this::locationToString).toList(),
                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException exception) {
            questSystem.getLogger().log(Level.SEVERE, "Could not add sign location", exception);
        }
    }

    private String locationToString(Location location) {
        return Objects.requireNonNull(location.getWorld()).getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        if (!event.isCancelled() && this.signs.contains(location)) {
            this.signs.remove(location);
            new BukkitRunnable() {
                @Override
                public void run() {
                    deleteSignIfExists(location);
                }
            }.runTaskAsynchronously(questSystem);
        }
    }

    /**
     * Method that gets called if a subcommand is executed by a player.
     *
     * @param questPlayer {@link QuestPlayer} - the executor.
     * @param arguments   String[] - the command arguments.
     * @since 0.0.1
     */
    @Override
    public void onCommand(QuestPlayer questPlayer, String[] arguments) {
        Block sign = questPlayer.getPlayer().getTargetBlock(null, 10);
        if (!WHITELISTED_SIGNS.contains(sign.getType())) {
            questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_SIGN_NOT_SIGN);
            return;
        }
        questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_SIGN_ADDED);
        if (signs.contains(sign.getLocation()))
            return;

        signs.add(sign.getLocation());
        new BukkitRunnable() {
            @Override
            public void run() {
                addNewSign(sign.getLocation());
            }
        }.runTaskAsynchronously(questSystem);
    }
}
