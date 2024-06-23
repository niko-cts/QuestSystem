package net.playlegend.questsystem.npc;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.npcsystem.NPC;
import chatzis.nikolas.mc.npcsystem.NPCSystem;
import chatzis.nikolas.mc.npcsystem.event.NPCClickEvent;
import chatzis.nikolas.mc.npcsystem.event.PlayerInteractAtNPCEvent;
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
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class loads all npc's regarding quests.
 * It checks whether a player clicked on one and triggers the {@link PlayerClickedOnQuestNPCEvent}.
 * Additionally, it spawns and destroys npc to specific players.
 *
 * @author Niko
 */
public class NPCManager implements Listener, NPCClickEvent {

	private final Map<NPC, String> taskNPCs;
	private final Map<NPC, Quest> findNPCs;

	public NPCManager(QuestSystem questSystem) {
		this.taskNPCs = new HashMap<>();
		this.findNPCs = new HashMap<>();

		NPCDatabase database = NPCDatabase.getInstance();
		Logger log = questSystem.getLogger();
		NPCSystem.getInstance().registerNPCListener(this);
		try (ResultSet npcSet = database.getAllNPC()) {
			while (npcSet != null && npcSet.next()) {
				UUID uuid = UUID.fromString(npcSet.getString("uuid"));
				Location location = new Location(questSystem.getServer().getWorld(npcSet.getString("world")),
						npcSet.getDouble("x"), npcSet.getDouble("y"), npcSet.getDouble("z"), npcSet.getFloat("yaw"), npcSet.getFloat("pitch"));
				String name = npcSet.getString("name");

				try (ResultSet taskNPCsSet = database.getAllNPCTasks(uuid)) {
					while (taskNPCsSet != null && taskNPCsSet.next()) {
						taskNPCs.put(new NPC(uuid, name, location, NPC.DEFAULT_SKIN, new HashSet<>(List.of(uuid))), taskNPCsSet.getString("content"));
					}
				} catch (SQLException exception) {
					log.log(Level.SEVERE, "Could not load task NPCs from database", exception);
				}
				try (ResultSet findNpc = database.getAllFindings(uuid)) {
					while (findNpc != null && findNpc.next()) {
						int questId = findNpc.getInt("quest_id");
						Optional<Quest> quest = questSystem.getQuestManager().getQuestById(questId);
						quest.ifPresentOrElse(
								q -> findNPCs.put(new NPC(uuid, name, location, NPC.DEFAULT_SKIN, new HashSet<>(List.of(uuid))), q),
								() -> log.log(Level.WARNING, "Could not find a quest but is inserted in npc id=" + questId)
						);

					}
				} catch (SQLException exception) {
					log.log(Level.SEVERE, "Could not load task NPCs from database", exception);
				}

			}
		} catch (SQLException exception) {
			log.log(Level.SEVERE, "Could not load NPC from database", exception);
		}
	}

	@EventHandler
	public void onQuestUpdate(PlayerQuestUpdateEvent event) {
		QuestPlayer player = event.getPlayer();
		Optional<ActivePlayerQuest> activePlayerQuestOptional = player.getActivePlayerQuest();
		Set<UUID> spawnedNPCs = new HashSet<>();
		if (activePlayerQuestOptional.isPresent()) {
			for (QuestStep<?> step : activePlayerQuestOptional.get().getNextUncompletedSteps()) {
				if (step.getType() == QuestStepType.SPEAK && step instanceof TalkToNPCQuestStep talkStep) {
					for (NPC taskNPC : taskNPCs.keySet()) {
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
			taskNPCs.keySet().forEach(n -> n.destroy(player.getPlayer()));
		}

		if (event.getType() == PlayerQuestUpdateEvent.QuestUpdateType.FIND || event.getType() == PlayerQuestUpdateEvent.QuestUpdateType.JOINED) {
			for (Map.Entry<NPC, Quest> entry : findNPCs.entrySet()) {
				player.getFoundQuests().compute(entry.getValue(), (quest, timestamp) -> {
					if (timestamp != null) {
						if (!spawnedNPCs.contains(entry.getKey().getUniqueID())) {
							entry.getKey().show(event.getPlayer().getPlayer());
						}
					} else {
						entry.getKey().destroy(event.getPlayer().getPlayer());
					}

					return timestamp;
				});
			}
		}
	}

	@Override
	public void npcClicked(PlayerInteractAtNPCEvent event) {
		taskNPCs.computeIfPresent(event.getNPC(), (npc, s) -> {
			QuestSystem.getInstance().getServer().getPluginManager().callEvent(
					new PlayerClickedOnQuestNPCEvent(event.getPlayer().getPlayer(), event.getNPC().getUniqueID(), s)
			);
			return s;
		});
		findNPCs.computeIfPresent(event.getNPC(), (npc, quest) -> {
			QuestPlayer player = QuestSystem.getInstance().getPlayerHandler().getPlayer(event.getPlayer().getPlayer());
			if (player != null && !player.getFoundQuests().containsKey(quest)) {
				player.foundQuest(quest);

				String foundBook = player.getLanguage().translateMessage(TranslationKeys.QUESTS_NPC_FOUNDBOOK,
						List.of("${name}", "{description}"),
						List.of(quest.name(), player.getLanguage().translateMessage(quest.description()).replace(";", "\n")));

				player.openBook(new ItemBuilder(Material.WRITTEN_BOOK).addPage(foundBook).craft());
			}
			return quest;
		});
	}
}
