package net.playlegend.questsystem.npc;

import chatzis.nikolas.mc.npcsystem.NPC;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.database.NPCDatabase;
import net.playlegend.questsystem.events.PlayerClickedOnQuestNPCEvent;
import net.playlegend.questsystem.events.PlayerQuestUpdateEvent;
import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.quest.steps.QuestStepType;
import net.playlegend.questsystem.quest.steps.TalkToNPCQuestStep;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class loads all npc's regarding quests.
 * It checks whether a player clicked on one and triggers the {@link PlayerClickedOnQuestNPCEvent}.
 * Additionally, it spawns and destroys npc to specific players.
 *
 * @author Niko
 */
public class NPCManager implements Listener {

	private final Set<TaskNPC> taskNPCs;
	private final Set<FindNPC> findNPCs;
	private final NPCDatabase npcDatabase;

	public NPCManager(QuestSystem questSystem) {
		this.taskNPCs = Collections.synchronizedSet(new HashSet<>());
		this.findNPCs = Collections.synchronizedSet(new HashSet<>());
		npcDatabase = NPCDatabase.getInstance();

		Logger log = questSystem.getLogger();
		try (ResultSet npcSet = npcDatabase.getAllTaskNPCs()) {
			while (npcSet != null && npcSet.next()) {
				UUID uuid = UUID.fromString(npcSet.getString("uuid"));
				Location location = new Location(questSystem.getServer().getWorld(npcSet.getString("world")),
						npcSet.getDouble("x"), npcSet.getDouble("y"), npcSet.getDouble("z"), npcSet.getFloat("yaw"), npcSet.getFloat("pitch"));
				String name = npcSet.getString("name");
				Map<String, String> messages = new HashMap<>();
				try (ResultSet taskNPCsSet = npcDatabase.getNPCTaskMessages(uuid)) {
					while (taskNPCsSet != null && taskNPCsSet.next()) {
						messages.put(taskNPCsSet.getString("language"), taskNPCsSet.getString("content"));
					}
				} catch (SQLException exception) {
					log.log(Level.SEVERE, "Could not load task NPCs from database", exception);
				}

				if (!messages.isEmpty()) {
					addTaskNPC(uuid, name, location, messages);
				} else {
					log.log(Level.WARNING, "Task NPC '{0}'was not added because there were no messages", uuid);
				}
			}
			log.log(Level.INFO, "Loaded {0} task-NPC", taskNPCs.size());
		} catch (SQLException exception) {
			log.log(Level.SEVERE, "Could not load NPC from database", exception);
		}

		try (ResultSet findNPC = npcDatabase.getAllFindings()) {
			while (findNPC != null && findNPC.next()) {

				Location location = new Location(questSystem.getServer().getWorld(findNPC.getString("world")),
						findNPC.getDouble("x"), findNPC.getDouble("y"), findNPC.getDouble("z"), findNPC.getFloat("yaw"), findNPC.getFloat("pitch"));
				String name = findNPC.getString("name");

				int questId = findNPC.getInt("quest_id");
				UUID uuid = UUID.randomUUID();
				Optional<Quest> quest = questSystem.getQuestManager().getQuestById(questId);
				quest.ifPresentOrElse(
						q -> addFindNPC(uuid, name, location, q),
						() -> log.log(Level.WARNING, "Could not find a quest but is inserted in npc id=" + questId)
				);
			}
			log.log(Level.INFO, "Loaded {0} find-NPC", findNPCs.size());
		} catch (SQLException exception) {
			log.log(Level.SEVERE, "Could not load find NPCs from database", exception);
		}
	}


	/**
	 * Shows or destroys a NPC if necessary. Checks for a task and find npcs.
	 *
	 * @param event PlayerQuestUpdateEvent - when a quest was updated.
	 */
	@EventHandler
	public void onQuestUpdate(PlayerQuestUpdateEvent event) {
		QuestPlayer player = event.getPlayer();
		Optional<ActivePlayerQuest> activePlayerQuestOptional = player.getActivePlayerQuest();
		Set<UUID> spawnedNPCs = new HashSet<>();
		if (activePlayerQuestOptional.isPresent()) {
			for (QuestStep<?> step : activePlayerQuestOptional.get().getNextUncompletedSteps()) {
				if (step.getType() == QuestStepType.SPEAK && step instanceof TalkToNPCQuestStep talkStep) {
					for (NPC taskNPC : taskNPCs) {
						if (taskNPC.getUniqueID().equals(talkStep.getStepObject())) {
							spawnedNPCs.add(taskNPC.getUniqueID());
							if (!taskNPC.isPlayerAllowedToSee(player.getPlayer()))
								taskNPC.show(player.getPlayer());
						} else
							taskNPC.destroy(player.getPlayer());
					}
				}
			}
		} else {
			taskNPCs.forEach(n -> n.destroy(player.getPlayer()));
		}

		if (event.getType() == PlayerQuestUpdateEvent.QuestUpdateType.JOINED || event.getType() == PlayerQuestUpdateEvent.QuestUpdateType.QUEST_ENDED) {
			for (FindNPC npc : findNPCs) {
				player.getFoundQuests().compute(npc.getQuest(), (quest, timestamp) -> {
					if (timestamp == null && !spawnedNPCs.contains(npc.getUniqueID())) {
						npc.show(player.getPlayer());
					} else if (timestamp != null) {
						npc.destroy(player.getPlayer());
					}
					return timestamp;
				});
			}
		}
	}

	public void insertTaskNPC(UUID uuid, String npcName, Location location, String languageKey, String content) {
		Optional<TaskNPC> npc = taskNPCs.stream().filter(n -> n.getUniqueID().equals(uuid)).findFirst();
		npc.ifPresentOrElse(taskNPC -> {
					Bukkit.getScheduler().runTask(QuestSystem.getInstance(), () -> taskNPC.teleport(location));
					if (taskNPC.getMessages().containsKey(languageKey))
						this.npcDatabase.updateTaskNPCAll(uuid, npcName, location, languageKey, content);
					else {
						taskNPC.getMessages().put(languageKey, content);
						this.npcDatabase.createTaskNPCMessage(uuid, languageKey, content);
					}
				},
				() -> {
					addTaskNPC(uuid, npcName, location, Map.of(languageKey, content));
					this.npcDatabase.createTaskNPC(uuid, npcName, location, languageKey, content);
				});
	}

	public void insertFindNPC(Quest quest, String npcName, Location location) {
		findNPCs.stream().filter(n -> n.getQuest().equals(quest)).findFirst().ifPresentOrElse(
				npc -> {
					Bukkit.getScheduler().runTask(QuestSystem.getInstance(), () -> npc.destroy());
					findNPCs.remove(npc);
					this.npcDatabase.updateFindNPC(quest.id(), npcName, location);
				},
				() -> this.npcDatabase.createFindNPC(quest.id(), npcName, location)
		);
		addFindNPC(UUID.randomUUID(), npcName, location, quest);
	}

	private void addTaskNPC(UUID uuid, String name, Location location, Map<String, String> messages) {
		new BukkitRunnable() {
			@Override
			public void run() {
				TaskNPC taskNPC = new TaskNPC(uuid, name, location, NPC.DEFAULT_SKIN, new HashSet<>(Set.of(uuid)), false, new HashMap<>());
				taskNPC.getMessages().putAll(messages);
				taskNPCs.add(taskNPC);
			}
		}.runTask(QuestSystem.getInstance());
	}

	private void addFindNPC(UUID uuid, String name, Location location, Quest quest) {
		new BukkitRunnable() {
			@Override
			public void run() {
				findNPCs.add(new FindNPC(uuid, name, location, NPC.DEFAULT_SKIN, new HashSet<>(Set.of(uuid)), false, quest));
			}
		}.runTask(QuestSystem.getInstance());
	}

	public String getTaskNPCList() {
		return taskNPCs.stream().map(Object::toString).collect(Collectors.joining("\n- "));
	}

	public String getFindNPCList() {
		return findNPCs.stream().map(Object::toString).collect(Collectors.joining("\n- "));
	}

	public void deleteFindNPC(Quest quest) {
		findNPCs.stream().filter(n -> n.getQuest().id() == quest.id()).findFirst().ifPresent(npc -> {
			npc.destroy();
			findNPCs.remove(npc);
			npcDatabase.deleteFindNPC(npc.getQuest().id());
		});
	}

	public void deleteTaskNPC(UUID uuid) {
		taskNPCs.stream().filter(n -> n.getUniqueID().equals(uuid)).findFirst().ifPresent(npc -> {
			npc.destroy();
			taskNPCs.remove(npc);
			npcDatabase.deleteTaskNPC(uuid);
		});
	}

}
