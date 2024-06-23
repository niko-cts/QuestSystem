package net.playlegend.questsystem.database;

import org.bukkit.Location;

import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NPCDatabase {

	private static final String TABLE_NPC = "system_quests_npc";
	private static final String TABLE_NPC_FINDINGS = "system_quests_npc_find";
	private static final String TABLE_NPC_STEPS = "system_quests_npc_step";
	private static NPCDatabase instance;

	public static NPCDatabase getInstance() {
		if (instance == null)
			instance = new NPCDatabase();
		return instance;
	}

	private final DatabaseHandler databaseHandler;

	private NPCDatabase() {
		this.databaseHandler = DatabaseHandler.getInstance();
		this.databaseHandler.createTableIfNotExists(TABLE_NPC, List.of(
				"uuid VARCHAR(36) NOT NULL PRIMARY KEY",
				"name VARCHAR(36) NOT NULL",
				"world VARCHAR(36) NOT NULL",
				"x DOUBLE NOT NULL",
				"y DOUBLE NOT NULL",
				"z DOUBLE NOT NULL",
				"yaw FLOAT NOT NULL",
				"pitch FLOAT NOT NULL"
		));
		this.databaseHandler.createTableIfNotExists(TABLE_NPC_FINDINGS, List.of(
				"uuid VARCHAR(36) NOT NULL PRIMARY KEY",
				"quest_id INT NOT NULL",
				"content TEXT NOT NULL",
				"FOREIGN KEY(uuid) REFERENCES " + TABLE_NPC + "(uuid) ON DELETE CASCADE",
				"FOREIGN KEY(quest_id) REFERENCES " + QuestDatabase.TABLE_QUEST_STEPS_INFO + "(quest_id) ON DELETE CASCADE"
		));
		this.databaseHandler.createTableIfNotExists(TABLE_NPC_STEPS, List.of(
				"uuid VARCHAR(36) NOT NULL",
				"quest_id INT NOT NULL",
				"step_id INT NOT NULL",
				"content TEXT NOT NULL",
				"PRIMARY KEY(uuid, quest_id, step_id)",
				"FOREIGN KEY(uuid) REFERENCES " + TABLE_NPC + "(uuid) ON DELETE CASCADE",
				"FOREIGN KEY(quest_id) REFERENCES " + QuestDatabase.TABLE_QUEST_STEPS_INFO + "(quest_id) ON DELETE CASCADE",
				"FOREIGN KEY(step_id) REFERENCES " + QuestDatabase.TABLE_QUEST_STEPS_INFO + "(id) ON DELETE CASCADE"
		));
	}

	public void createNPCForQuestFind(UUID uuid, String name, Location location, int questId, String content) {
		databaseHandler.insertIntoTable(List.of(TABLE_NPC, TABLE_NPC_FINDINGS),
				List.of(List.of(uuid.toString(), name, Objects.requireNonNull(location.getWorld()).getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()),
						List.of(uuid.toString(), questId, content)));
	}

	public void createFindingFromExistingNPC(UUID uuid, int questId, String content) {
		databaseHandler.insertIntoTable(List.of(TABLE_NPC_STEPS), List.of(List.of(uuid.toString(), questId, content)));
	}

	public void createNPCForQuestTask(UUID uuid, String name, Location location, int questId, int stepId, String text) {
		databaseHandler.insertIntoTable(List.of(TABLE_NPC, TABLE_NPC_STEPS),
				List.of(List.of(uuid.toString(), name, Objects.requireNonNull(location.getWorld()).getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()),
						List.of(uuid.toString(), questId, stepId, text)));
	}

	public void createQuestTaskFromExistingNPC(UUID uuid, int questId, int stepId, String text) {
		databaseHandler.insertIntoTable(List.of(TABLE_NPC_STEPS), List.of(List.of(uuid.toString(), questId, stepId, text)));
	}

	public ResultSet getAllNPC() {
		return databaseHandler.select(TABLE_NPC, List.of("*"), "");
	}

	public ResultSet getAllFindings(UUID npcUUID) {
		return databaseHandler.select(TABLE_NPC_FINDINGS, List.of("*"), "WHERE uuid='" + npcUUID + "' LIMIT 1");
	}

	public ResultSet getAllNPCTasks(UUID npcUUID) {
		return databaseHandler.select(TABLE_NPC_STEPS, List.of("*"), "WHERE uuid='" + npcUUID + "'");
	}
}
