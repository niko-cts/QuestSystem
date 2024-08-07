package chatzis.nikolas.questsystem.listener;

import chatzis.nikolas.questsystem.QuestSystem;
import chatzis.nikolas.questsystem.player.ActivePlayerQuest;
import chatzis.nikolas.questsystem.player.PlayerHandler;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import chatzis.nikolas.questsystem.util.QuestTimingsUtil;
import lombok.RequiredArgsConstructor;
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
		Bukkit.getScheduler().runTaskAsynchronously(QuestSystem.getInstance(), () ->
				playerHandler.addPlayer(event.getPlayer()).ifPresent(questPlayer -> new BukkitRunnable() {
					@Override
					public void run() {
						Optional<ActivePlayerQuest> activeQuest = questPlayer.getActivePlayerQuest();
						if (activeQuest.isPresent()) {
							ActivePlayerQuest activePlayerQuest = activeQuest.get();
							questPlayer.sendMessage(TranslationKeys.QUESTS_EVENT_JOINED_HAS_ACTIVE,
									List.of("${name}", "${duration}"),
									List.of(activePlayerQuest.getQuest().name(), QuestTimingsUtil.convertSecondsToDHMS(questPlayer.getLanguage(), activePlayerQuest.getSecondsLeft())));
						} else {
							questPlayer.sendClickableMessage(TranslationKeys.QUESTS_EVENT_JOINED_NO_ACTIVE_TEXT, TranslationKeys.QUESTS_EVENT_JOINED_NO_ACTIVE_HOVER, "/quest");
						}
					}
				}.runTaskLater(QuestSystem.getInstance(), 20L)));
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
