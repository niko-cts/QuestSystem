package chatzis.nikolas.questsystem.database;

import org.bukkit.Location;

import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NPCDatabase {

	private static final String TABLE_NPC_STEPS_INFO = "system_quests_npc";
	private static final String TABLE_NPC_FINDINGS = "system_quests_npc_find";
	private static final String TABLE_NPC_STEPS_CONTENT = "system_quests_npc_step";
	private static NPCDatabase instance;

	public static NPCDatabase getInstance() {
		if (instance == null)
			instance = new NPCDatabase();
		return instance;
	}

	private final DatabaseHandler databaseHandler;

	private NPCDatabase() {
		this.databaseHandler = DatabaseHandler.getInstance();
		this.databaseHandler.createTableIfNotExists(TABLE_NPC_STEPS_INFO, List.of(
				"uuid VARCHAR(36) NOT NULL PRIMARY KEY",
				"name VARCHAR(36) NOT NULL",
				"world VARCHAR(36) NOT NULL",
				"x DOUBLE NOT NULL",
				"y DOUBLE NOT NULL",
				"z DOUBLE NOT NULL",
				"yaw FLOAT NOT NULL",
				"pitch FLOAT NOT NULL"
		));
		this.databaseHandler.createTableIfNotExists(TABLE_NPC_STEPS_CONTENT, List.of(
				"uuid VARCHAR(36) NOT NULL",
				"language VARCHAR(36) NOT NULL",
				"content TEXT NOT NULL",
				"PRIMARY KEY(uuid, language)",
				"FOREIGN KEY(uuid) REFERENCES " + TABLE_NPC_STEPS_INFO + "(uuid) ON DELETE CASCADE"
		));
		this.databaseHandler.createTableIfNotExists(TABLE_NPC_FINDINGS, List.of(
				"quest_id INT NOT NULL PRIMARY KEY",
				"name VARCHAR(36) NOT NULL",
				"world VARCHAR(36) NOT NULL",
				"x DOUBLE NOT NULL",
				"y DOUBLE NOT NULL",
				"z DOUBLE NOT NULL",
				"yaw FLOAT NOT NULL",
				"pitch FLOAT NOT NULL",
				"FOREIGN KEY(quest_id) REFERENCES " + QuestDatabase.TABLE_QUEST_STEPS_INFO + "(quest_id) ON DELETE CASCADE"
		));
	}

	public ResultSet getAllTaskNPCs() {
		return databaseHandler.selectAll(TABLE_NPC_STEPS_INFO);
	}

	public ResultSet getAllFindings() {
		return databaseHandler.selectAll(TABLE_NPC_FINDINGS);
	}

	public ResultSet getNPCTaskMessages(UUID npcUUID) {
		return databaseHandler.select(TABLE_NPC_STEPS_CONTENT, List.of("*"), "WHERE uuid=?", List.of(npcUUID.toString()));
	}

	public void updateTaskNPCAll(UUID uuid, String npcName, Location location, String language, String content) {
		this.databaseHandler.update(List.of(TABLE_NPC_STEPS_INFO, TABLE_NPC_STEPS_CONTENT),
				List.of(List.of("name", "world", "x", "y", "z", "yaw", "pitch"),
						List.of("language", "content")),
				List.of(List.of(npcName, Objects.requireNonNull(location.getWorld()).getName(),
						location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()),
						List.of(language, content)),
				List.of("WHERE uuid=?", "WHERE uuid=?"),
				List.of(List.of(uuid.toString()), List.of(uuid.toString())));
	}


	public void createTaskNPC(UUID uuid, String npcName, Location location, String languageKey, String content) {
		this.databaseHandler.insertIntoTable(List.of(TABLE_NPC_STEPS_INFO, TABLE_NPC_STEPS_CONTENT),
				List.of(List.of(uuid.toString(), npcName, Objects.requireNonNull(location.getWorld()).getName(),
								location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()),
						List.of(uuid.toString(), languageKey, content)));
	}

	public void createTaskNPCMessage(UUID uuid, String language, String content) {
		this.databaseHandler.insertIntoTable(List.of(TABLE_NPC_STEPS_CONTENT), List.of(List.of(uuid.toString(), language, content)));
	}

	public void updateFindNPC(int questId, String npcName, Location location) {
		this.databaseHandler.update(List.of(TABLE_NPC_FINDINGS),
				List.of(List.of("name", "world", "x", "y", "z", "yaw", "pitch")),
				List.of(List.of(npcName, Objects.requireNonNull(location.getWorld()).getName(),
						location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch())),
				List.of("WHERE quest_id=?"),
				List.of(List.of(questId)));
	}

	public void createFindNPC(int id, String npcName, Location location) {
		this.databaseHandler.insertIntoTable(List.of(TABLE_NPC_FINDINGS),
				List.of(List.of(id, npcName, Objects.requireNonNull(location.getWorld()).getName(),
						location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch())));
	}

	public void deleteFindNPC(int id) {
		this.databaseHandler.delete(List.of(TABLE_NPC_FINDINGS), List.of("WHERE quest_id=" + id));
	}

	public void deleteTaskNPC(UUID uuid) {
		this.databaseHandler.delete(List.of(TABLE_NPC_STEPS_INFO), List.of("WHERE uuid='" + uuid + "'"));
	}
}
