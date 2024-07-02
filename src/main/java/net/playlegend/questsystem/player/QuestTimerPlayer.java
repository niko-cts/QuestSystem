package net.playlegend.questsystem.player;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.QuestTimingsUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

/**
 * This class adds a runnable for the active quest.
 * It will send a notify message in each interval.
 *
 * @author Niko
 */
public class QuestTimerPlayer {

	private final QuestPlayer questPlayer;
	private BukkitTask bukkitTask;

	public QuestTimerPlayer(QuestPlayer questPlayer) {
		this.questPlayer = questPlayer;
	}

	protected void checkExpiredAndStartTimerIfPresent() {
		questPlayer.checkIfExpired();
		questPlayer.getActivePlayerQuest().ifPresent(this::startTimer);
	}

	private void startTimer(ActivePlayerQuest activePlayerQuest) {
		long secondsLeft = activePlayerQuest.getSecondsLeft();
		if (secondsLeft < 1) {
			return;
		}
		cancelTask();

		long delay = QuestTimingsUtil.calculateNextDuration(secondsLeft);
		System.out.println(delay + " for " + secondsLeft);

		bukkitTask = new BukkitRunnable() {
			@Override
			public void run() {
				long currentSeconds = activePlayerQuest.getSecondsLeft();
				if (currentSeconds >= 0) {
					questPlayer.sendMessage(TranslationKeys.QUESTS_EVENT_COUNTDOWN,
							List.of("${name}", "${duration}"),
							List.of(activePlayerQuest.getQuest().name(), QuestTimingsUtil.convertSecondsToDHMS(questPlayer.getLanguage(), currentSeconds)));
				}
				checkExpiredAndStartTimerIfPresent();
			}
		}.runTaskLater(QuestSystem.getInstance(), 20L * delay);
	}

	public void cancelTask() {
		if (bukkitTask != null && !bukkitTask.isCancelled())
			bukkitTask.cancel();
	}
}
