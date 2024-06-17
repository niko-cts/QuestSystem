package net.playlegend.questsystem.listener;

import lombok.RequiredArgsConstructor;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.player.PlayerHandler;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.QuestTimingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Handles the join and quitting of a player.
 *
 * @author Niko
 */
@RequiredArgsConstructor
public class QuestPlayerConnectionListener implements Listener {

	private final QuestSystem questSystem;
	private final PlayerHandler playerHandler;

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(QuestSystem.getInstance(), () -> {
			playerHandler.addPlayer(event.getPlayer()).ifPresent(questPlayer -> {
				Optional<ActivePlayerQuest> activeQuest = questPlayer.getActivePlayerQuest();
				if (activeQuest.isPresent()) {
					ActivePlayerQuest activePlayerQuest = activeQuest.get();
					questPlayer.sendMessage(TranslationKeys.QUESTS_EVENT_JOINED_HAS_ACTIVE,
							List.of("${name}", "${duration}"),
							List.of(activePlayerQuest.getActiveQuest().name(), QuestTimingsUtil.convertSecondsToDHMS(questPlayer.getCurrentLanguage(), activePlayerQuest.getSecondsLeft())));
				} else {
					questPlayer.sendMessage(TranslationKeys.QUESTS_EVENT_JOINED_NO_ACTIVE);
				}
			});
		});
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		new BukkitRunnable() {
			@Override
			public void run() {
				playerHandler.playerDisconnected(uuid);
			}
		}.runTaskAsynchronously(questSystem);
	}
}
