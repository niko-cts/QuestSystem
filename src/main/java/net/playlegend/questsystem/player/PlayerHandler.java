package net.playlegend.questsystem.player;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.database.PlayerInfoDatabase;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.LanguageHandler;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class creates a new player in the database and stores the QuestPlayer
 *
 * @author Niko
 */
public class PlayerHandler {

	private final Map<UUID, QuestPlayer> questPlayerMap;
	private final PlayerInfoDatabase playerDb;

	public PlayerHandler() {
		this.questPlayerMap = new ConcurrentHashMap<>();
		this.playerDb = PlayerInfoDatabase.getInstance();
	}

	public void addPlayer(Player player) {
		UUID uuid = player.getUniqueId();
		LanguageHandler languageHandler = QuestSystem.getInstance().getLanguageHandler();
		Logger logger = QuestSystem.getInstance().getLogger();

		try (ResultSet resultSet = this.playerDb.getPlayerInfos(uuid)) {
			Language language;
			Timestamp lastLogout;
			if (resultSet != null && resultSet.next()) {
				Optional<Language> languageOptional = languageHandler.getLanguageByKey(resultSet.getString("language"));
				if (languageOptional.isPresent()) {
					language = languageOptional.get();
				} else {
					language = languageHandler.getFallbackLanguage();
					logger.log(Level.WARNING, "Could not get the language key {0} which was saved for {0}",
							new Object[]{resultSet.getString("language"), uuid});
				}
				lastLogout = resultSet.getTimestamp("last_logout");
			} else {
				language = QuestSystem.getInstance().getLanguageHandler().getFallbackLanguage();
				playerDb.insertPlayer(uuid, language.getLanguageKey());
				lastLogout = Timestamp.from(Instant.now());
			}

			this.questPlayerMap.put(uuid, new QuestPlayer(player, language, lastLogout));
		} catch (SQLException exception) {
			logger.log(Level.SEVERE, "Error while loading QuestPlayer", exception);
		}
	}

	/**
	 * Will remove the QuestPlayer from the map and update all information to the database.
	 * @param uuid UUID - the uuid of the player.
	 */
	public void playerDisconnected(UUID uuid) {
		this.questPlayerMap.computeIfPresent(uuid, (uuid1, questPlayer) -> {
			questPlayer.checkIfExpired();
			playerDb.updateAllPlayerData(questPlayer);
			return null;
		});
	}

}
