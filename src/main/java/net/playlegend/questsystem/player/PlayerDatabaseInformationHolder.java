package net.playlegend.questsystem.player;

import lombok.Getter;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class only holds information about the current session of the player.
 * This is necessary to make sure the data gets inserted correct as soon as the QuestPlayer disconnects.
 *
 * @author Niko
 */
@Getter
public class PlayerDatabaseInformationHolder {

	private final ReentrantLock lock;

	private Map<Integer, Timestamp> newFoundQuests;
	private Map<Integer, Timestamp> newCompletedQuests;
	private boolean needsInsertActiveDirty;
	private boolean markLanguageDirty;
	private boolean markActiveQuestDirty;
	private boolean markCoinsDirty;

	public PlayerDatabaseInformationHolder(boolean needsInsertActiveDirty) {
		this.lock = new ReentrantLock();
		reset(needsInsertActiveDirty);
	}

	public void lockForDatabaseUpdate() {
		this.lock.lock();
	}

	public void unlock() {
		this.lock.unlock();
	}

	public void reset(boolean needsInsertActiveDirty) {
		this.needsInsertActiveDirty = needsInsertActiveDirty;
		this.newFoundQuests = new ConcurrentHashMap<>();
		this.newCompletedQuests = new ConcurrentHashMap<>();
		this.markActiveQuestDirty = false;
		this.markLanguageDirty = false;
	}

	protected void addFoundQuest(Integer questId, Timestamp foundAt) {
		this.lock.lock();
		try {
			this.newFoundQuests.put(questId, foundAt);
		} finally {
			this.lock.unlock();
		}
	}

	protected void addCompletedQuest(Integer questId, Timestamp foundAt) {
		this.lock.lock();
		try {
			this.newCompletedQuests.put(questId, foundAt);
		} finally {
			this.lock.unlock();
		}
	}

	protected void markActiveQuestDirty() {
		this.lock.lock();
		try {
			this.markActiveQuestDirty = true;
		} finally {
			this.lock.unlock();
		}
	}

	protected void markLanguageDirty() {
		this.lock.lock();
		try {
			this.markLanguageDirty = true;
		} finally {
			this.lock.unlock();
		}
	}

	protected void markCoinsDirty() {
		this.lock.lock();
		try {
			this.markCoinsDirty = true;
		} finally {
			this.lock.unlock();
		}
	}

}
