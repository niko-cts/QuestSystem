package net.playlegend.questsystem.database;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class PlayerInfoDatabase {

	private static final String TABLE_LANGUAGE = "system_player";
	private static PlayerInfoDatabase instance;

	public static PlayerInfoDatabase getInstance() {
		if (instance == null)
			instance = new PlayerInfoDatabase();
		return instance;
	}

	private final DatabaseHandler databaseHandler;

	private PlayerInfoDatabase() {
		this.databaseHandler = DatabaseHandler.getInstance();
		databaseHandler.createTableIfNotExists(TABLE_LANGUAGE, List.of(
				"uuid VARCHAR(36) NOT NULL PRIMARY KEY",
				"last_logout TIMESTAMP",
				"language VARCHAR(36) NOT NULL DEFAULT en-US"
		));
	}


	public void updatePlayerLanguage(UUID uuid, String language) {
		this.databaseHandler.update(List.of(TABLE_LANGUAGE), List.of(List.of("language")), List.of(List.of(language)), List.of(List.of(true)),
				List.of("WHERE uuid='" + uuid + "' LIMIT 1")
		);
	}

	public void updatePlayerLogout(UUID uuid, Timestamp timestamp) {
		this.databaseHandler.update(List.of(TABLE_LANGUAGE), List.of(List.of("last_logout")), List.of(List.of(timestamp.toString())), List.of(List.of(false)),
				List.of("WHERE uuid='" + uuid + "' LIMIT 1")
		);
	}

	public void insertPlayer(UUID uuid, String languageKey) {
		this.databaseHandler.insertIntoTable(List.of(TABLE_LANGUAGE), List.of(List.of(uuid.toString(), "null", languageKey)), List.of(List.of(true, false, true)));
	}

	public ResultSet getPlayerInfos(UUID uuid) {
		return this.databaseHandler.select(TABLE_LANGUAGE, List.of("*"), "WHERE uuid='" + uuid + "' LIMIT 1");
	}
}
