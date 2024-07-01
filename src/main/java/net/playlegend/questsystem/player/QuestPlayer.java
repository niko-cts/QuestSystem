package net.playlegend.questsystem.player;

import chatzis.nikolas.mc.nikoapi.NikoAPI;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.events.PlayerQuestUpdateEvent;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.QuestManager;
import net.playlegend.questsystem.quest.exception.QuestNotFoundException;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

/**
 * Class which stores all important information about the player and the quest.
 *
 * @author Niko
 */
public class QuestPlayer {
	@Getter
	private final Player player;
	@Getter
	private final Timestamp lastLogout;
	private final QuestTimerPlayer questTimer;
	@Getter
	private Language language;
	@Getter
	private int coins;

	private final Map<Quest, Timestamp> finishedQuests;
	private final Map<Quest, Timestamp> foundQuests;

	@Getter
	private final PlayerDatabaseInformationHolder playerDbInformationHolder;
	private ActivePlayerQuest activePlayerQuest;

	public QuestPlayer(@NonNull Player player, @NonNull Language language, @NonNull Timestamp lastLogout, int coins) {
		this.player = player;
		this.language = language;
		this.lastLogout = lastLogout;
		this.coins = coins;
		QuestManager questManager = QuestSystem.getInstance().getQuestManager();
		this.finishedQuests = questManager.loadCompletedQuestIdsByPlayer(player.getUniqueId());
		this.foundQuests = questManager.loadFoundQuestIdsByPlayer(player.getUniqueId());

		PlayerDatabaseInformationHolder tempDBInfo;
		try {
			ActivePlayerQuest activePlayerQuest = questManager.loadActiveQuestIdByPlayer(player.getUniqueId(), lastLogout).orElse(null);
			this.activePlayerQuest = activePlayerQuest;
			tempDBInfo = new PlayerDatabaseInformationHolder(activePlayerQuest == null);
		} catch (QuestNotFoundException exception) {
			tempDBInfo = new PlayerDatabaseInformationHolder(false);
			tempDBInfo.markActiveQuestDirty();
			this.activePlayerQuest = null;
			QuestSystem.getInstance().getLogger().info("Player had active quest which could not be found: " + exception.getMessage());
		}

		this.playerDbInformationHolder = tempDBInfo;
		this.questTimer = new QuestTimerPlayer(this);
		Bukkit.getScheduler().runTaskLater(QuestSystem.getInstance(), () -> {
			questUpdateEvent(PlayerQuestUpdateEvent.QuestUpdateType.JOINED);
			this.questTimer.startTimerIfActiveQuestPresent();
		}, 10L);
	}

	/**
	 * Will check and finish the active quest if done.
	 * Rewards the player and updates the scoreboard.
	 */
	public void checkAndFinishActiveQuest() {
		if (activePlayerQuest != null && activePlayerQuest.isQuestFinished()) {
			activePlayerQuest.getQuest().rewards().forEach(r -> r.rewardPlayer(this));
			Timestamp completedAt = Timestamp.from(Instant.now());
			finishedQuests.put(activePlayerQuest.getQuest(), completedAt);
			playerDbInformationHolder.addCompletedQuest(activePlayerQuest.getQuest().id(), completedAt);
			sendMessage(TranslationKeys.QUESTS_EVENT_FINISHED, "${name}", activePlayerQuest.getQuest().name());
			playSound(Sound.ENTITY_ENDER_DRAGON_GROWL);
			setActivePlayerQuest(null);
			questUpdateEvent(PlayerQuestUpdateEvent.QuestUpdateType.COMPLETED);
		}
	}

	public void foundQuest(Quest quest) {
		if (quest.isPublic()) return;
		foundQuests.computeIfAbsent(quest, q -> {
			Timestamp foundAt = Timestamp.from(Instant.now());
			playerDbInformationHolder.addFoundQuest(q.id(), foundAt);
			sendClickableMessage(TranslationKeys.QUESTS_EVENT_FOUND_NEW, TranslationKeys.QUESTS_EVENT_FOUND_NEW_HOVER, "${name}", q.name(), "/quest found");
			sendEvent(PlayerQuestUpdateEvent.QuestUpdateType.FIND);
			return foundAt;
		});
	}

	/**
	 * Check if expired and send every needed update
	 */
	public void checkIfExpired() {
		checkAndFinishActiveQuest();
		if (activePlayerQuest != null && activePlayerQuest.getSecondsLeft() <= 0) {
			sendMessage(TranslationKeys.QUESTS_EVENT_TIMER_EXPIRED, "${name}", activePlayerQuest.getQuest().name());
			setActivePlayerQuest(null);
			questUpdateEvent(PlayerQuestUpdateEvent.QuestUpdateType.QUEST_ENDED);
		}
	}

	public void playerQuitServer() {
		checkIfExpired();
		this.questTimer.cancelTask();
	}

	/**
	 * Changes the active quest
	 *
	 * @param quest Quest - the quest to switch to
	 */
	public void switchActiveQuest(@NonNull Quest quest) {
		createAndSetPlayerQuest(quest);
		sendMessage(TranslationKeys.QUESTS_EVENT_SWITCHED, "${name}", quest.name());
		questUpdateEvent(PlayerQuestUpdateEvent.QuestUpdateType.NEW_QUEST);
	}

	public void startActiveQuest(@NonNull Quest quest) {
		createAndSetPlayerQuest(quest);
		sendMessage(TranslationKeys.QUESTS_EVENT_STARTED, "${name}", quest.name());
		questUpdateEvent(PlayerQuestUpdateEvent.QuestUpdateType.NEW_QUEST);
	}

	public void cancelActiveQuest() {
		setActivePlayerQuest(null);
		questUpdateEvent(PlayerQuestUpdateEvent.QuestUpdateType.QUEST_ENDED);
	}

	/**
	 * Adds one to the current step. Marks the active quest as dirty.
	 *
	 * @param quest       ActivePlayerQuest - the quest the player did the step in.
	 * @param step        QuestStep - the step which was done
	 * @param amountToAdd int - the amount the player did at once
	 * @return boolean - The questStep is finished
	 */
	public boolean playerDidQuestStep(ActivePlayerQuest quest, QuestStep<?> step, int amountToAdd) {
		playerDbInformationHolder.markActiveQuestDirty();
		boolean isDone = quest.playerDidQuestStep(step, amountToAdd);
		if (isDone) {
			questUpdateEvent(PlayerQuestUpdateEvent.QuestUpdateType.STEP);
			playSound(Sound.ENTITY_PLAYER_LEVELUP);
		}
		return isDone;
	}


	public void questUpdateEvent(@NonNull PlayerQuestUpdateEvent.QuestUpdateType type) {
		ScoreboardUtil.updateScoreboard(this);
		QuestSystem.getInstance().getQuestSignManager().updateSign(this);
		sendEvent(type);
	}

	private void sendEvent(@NonNull PlayerQuestUpdateEvent.QuestUpdateType type) {
		QuestSystem.getInstance().getServer().getPluginManager().callEvent(new PlayerQuestUpdateEvent(this, type));
	}


	/**
	 * Will create a new ActivePlayerQuest.
	 *
	 * @param quest Quest - the quest to create from
	 */
	private void createAndSetPlayerQuest(@NonNull Quest quest) {
		setActivePlayerQuest(new ActivePlayerQuest(quest, Instant.now().plusSeconds(quest.finishTimeInSeconds())));
	}

	/**
	 * Sets a new active player quest, restarts the countdown.
	 *
	 * @param activePlayerQuest ActivePlayerQuest - the active player quest (nullable)
	 */
	private void setActivePlayerQuest(ActivePlayerQuest activePlayerQuest) {
		if (this.activePlayerQuest == activePlayerQuest)
			return;
		this.questTimer.cancelTask();
		this.activePlayerQuest = activePlayerQuest;
		this.playerDbInformationHolder.markActiveQuestDirty();
		this.questTimer.startTimerIfActiveQuestPresent();
	}

	/**
	 * Initializes a new {@link PlayerDatabaseInformationHolder}.
	 * This should be called as soon as the player data was updated or newly retrieved.
	 */
	public void databaseUpdatedResetEntries() {
		this.playerDbInformationHolder.reset(this.activePlayerQuest == null);
	}

	/**
	 * Changes the language and updates the scoreboard.
	 *
	 * @param language Language - the new language of the player
	 */
	public void setLanguage(Language language) {
		this.language = language;
		this.playerDbInformationHolder.markLanguageDirty();
		questUpdateEvent(PlayerQuestUpdateEvent.QuestUpdateType.UPDATE_STATS);
	}

	/**
	 * Changes the number of coins and updates the scoreboard.
	 *
	 * @param coins int - the new coins of the player
	 */
	public void setCoins(int coins) {
		this.coins = coins;
		this.playerDbInformationHolder.markCoinsDirty();
		questUpdateEvent(PlayerQuestUpdateEvent.QuestUpdateType.UPDATE_STATS);
	}


	public void sendMessage(String translationKey) {
		player.sendMessage(language.translateMessage(translationKey));
	}

	public void sendMessage(String translationKey, String placeholder, Object replacement) {
		player.sendMessage(language.translateMessage(translationKey, placeholder, replacement));
	}

	public void sendMessage(String translationKey, List<String> placeholder, List<Object> replacement) {
		player.sendMessage(language.translateMessage(translationKey, placeholder, replacement));
	}

	public void playSound(Sound sound) {
		player.playSound(player, sound, 1, 1);
	}

	public void addItem(ItemStack... item) {
		player.getInventory().addItem(item);
	}

	public UUID getUniqueId() {
		return player.getUniqueId();
	}


	public void sendClickableMessage(String translationKey, String hoverKey, String command) {
		TextComponent textComponent = new TextComponent(language.translateMessage(translationKey));
		textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		if (hoverKey != null)
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(language.translateMessage(hoverKey))));
		player.spigot().sendMessage(textComponent);
	}

	public void sendClickableMessage(String translationKey, String hoverKey, String placeholder, String replacement, String command) {
		TextComponent textComponent = new TextComponent(language.translateMessage(translationKey, placeholder, replacement));
		textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		if (hoverKey != null)
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(language.translateMessage(hoverKey))));
		player.spigot().sendMessage(textComponent);
	}

	public void openCustomInv(CustomInventory menu) {
		Bukkit.getScheduler().runTask(QuestSystem.getInstance(), () -> {
			APIPlayer apiPlayer = NikoAPI.getInstance().getPlayerHandler().getPlayer(getPlayer());
			if (apiPlayer != null) menu.open(apiPlayer);
		});
	}

	public void openBook(ItemStack writtenBook) {
		APIPlayer apiPlayer = NikoAPI.getInstance().getPlayerHandler().getPlayer(player.getUniqueId());
		if (apiPlayer != null) apiPlayer.openBook(writtenBook);
	}

	public List<Quest> getUnfinishedPublicQuests() {
		return QuestSystem.getInstance().getQuestManager().getPublicQuests().stream().filter(q -> !foundQuests.containsKey(q)).toList();
	}

	public Optional<ActivePlayerQuest> getActivePlayerQuest() {
		return Optional.ofNullable(activePlayerQuest);
	}

	public void lockDatabaseUpdate() {
		this.playerDbInformationHolder.lockForDatabaseUpdate();
	}

	public void unlockDatabaseUpdate() {
		this.playerDbInformationHolder.unlock();
	}

	public Map<Quest, Timestamp> getFoundQuests() {
		return new HashMap<>(foundQuests);
	}

	public Map<Quest, Timestamp> getFinishedQuests() {
		return new HashMap<>(finishedQuests);
	}
}
