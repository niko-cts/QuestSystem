package net.playlegend.questsystem.database;

import java.util.List;

public class NPCDatabase {

	private static final String TABLE_NPC = "system_quests_npc";
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
				"quest_id INT NOT NULL",
				"step_id INT NOT NULL",
				"x DOUBLE NOT NULL",
				"y DOUBLE NOT NULL",
				"z DOUBLE NOT NULL",
				"yaw FLOAT NOT NULL",
				"pitch FLOAT NOT NULL",
				"content TEXT NOT NULL",
				"PRIMARY KEY(quest_id, step_id)",
				"CONSTRAINT fk_npc_qid FOREIGN KEY(quest_id) REFERENCES " + QuestDatabase.TABLE_QUEST_STEPS_INFO + "(quest_id)",
				"CONSTRAINT fk_npc_sid FOREIGN KEY(step_id) REFERENCES " + QuestDatabase.TABLE_QUEST_STEPS_INFO + "(id)"
		));
	}

	// TODO
}
