package net.playlegend.questsystem.listener;

import lombok.RequiredArgsConstructor;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.player.PlayerHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Handles the join and quitting of a player.
 * @author Niko
 */
@RequiredArgsConstructor
public class QuestPlayerConnectionListener implements Listener {

    private final QuestSystem questSystem;
    private final PlayerHandler playerHandler;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new BukkitRunnable(){
            @Override
            public void run() {
                playerHandler.addPlayer(event.getPlayer());
            }
        }.runTaskAsynchronously(questSystem);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        new BukkitRunnable(){
            @Override
            public void run() {
                playerHandler.playerDisconnected(uuid);
            }
        }.runTaskAsynchronously(questSystem);
    }
}
