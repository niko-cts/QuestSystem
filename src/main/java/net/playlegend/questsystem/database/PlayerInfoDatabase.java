package net.playlegend.questsystem.database;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.player.ActivePlayerQuest;
import net.playlegend.questsystem.player.PlayerDatabaseInformationHolder;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.steps.QuestStep;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerInfoDatabase {

    private static final String TABLE_PLAYER = "system_player";
    private static PlayerInfoDatabase instance;

    public static PlayerInfoDatabase getInstance() {
        if (instance == null)
            instance = new PlayerInfoDatabase();
        return instance;
    }

    private final DatabaseHandler databaseHandler;

    private PlayerInfoDatabase() {
        this.databaseHandler = DatabaseHandler.getInstance();
        databaseHandler.createTableIfNotExists(TABLE_PLAYER, List.of(
                "uuid VARCHAR(36) NOT NULL PRIMARY KEY",
                "last_logout TIMESTAMP",
                "language VARCHAR(36) NOT NULL DEFAULT 'en'",
                "coins INT NOT NULL DEFAULT 0"
        ));
    }

    public void insertPlayer(UUID uuid, String languageKey) {
        this.databaseHandler.insertIntoTable(List.of(TABLE_PLAYER),
                List.of(List.of(uuid.toString(), Timestamp.from(Instant.now()).toString(), languageKey, 0)));
    }

    public ResultSet getPlayerInfos(UUID uuid) {
        return this.databaseHandler.select(TABLE_PLAYER, List.of("*"), "WHERE uuid='" + uuid + "' LIMIT 1");
    }

    /**
     * Performs one big SQL Batch to update every player data accordingly.
     *
     * @param questPlayersWithLogout Map<QuestPlayer, Instant> - the quest player and logout instant
     */
    public void updatePlayerDataFromMultiplePlayers(Map<QuestPlayer, Instant> questPlayersWithLogout) {
        StringBuilder completeSQL = new StringBuilder();
        for (Map.Entry<QuestPlayer, Instant> entry : questPlayersWithLogout.entrySet()) {
            completeSQL.append(getUpdatePlayerSQLStatements(entry.getKey(), entry.getValue()));
        }
        if (!databaseHandler.execute(completeSQL.toString())) {
            QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not update all player data");
        }
    }

    /**
     * Performs a sql statement which contains every necessary information to update the players data accordingly.
     *
     * @param questPlayer QuestPlayer - the quest player to update
     * @param lastLogout  Instant - the lastLogout to set in the database
     */
    public void updateAllPlayerData(QuestPlayer questPlayer, Instant lastLogout) {
        if (!this.databaseHandler.execute(getUpdatePlayerSQLStatements(questPlayer, lastLogout)))
            QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not update player data");
    }

    /**
     * Returns a sql statement which contains every necessary information to update the players data accordingly.
     *
     * @param questPlayer QuestPlayer - the quest player to update
     * @param lastLogout  Instant - the lastLogout to set in the database
     * @return String - the sql statements
     */
    public String getUpdatePlayerSQLStatements(QuestPlayer questPlayer, Instant lastLogout) {
        UUID uuid = questPlayer.getUniqueId();
        PlayerDatabaseInformationHolder dbInfo = questPlayer.getPlayerDbInformationHolder();
        StringBuilder sql = new StringBuilder();
        if (dbInfo.isMarkActiveQuestDirty()) {
            Optional<ActivePlayerQuest> activePlayerQuest = questPlayer.getActivePlayerQuest();

            if (activePlayerQuest.isEmpty()) {
                // DELETE ACTIVE PLAYER QUEST
                sql.append(String.format("DELETE FROM %s WHERE uuid='%s' LIMIT 1;",
                        PlayerQuestDatabase.TABLE_PLAYER_ACTIVE_QUEST, uuid));
                sql.append(String.format("DELETE FROM %s WHERE uuid='%s';",
                        PlayerQuestDatabase.TABLE_PLAYER_ACTIVE_QUEST_STEPS, uuid));
            } else if (dbInfo.isNeedsInsertActiveDirty()) {
                // INSERT ACTIVE PLAYER QUEST
                ActivePlayerQuest quest = activePlayerQuest.get();
                sql.append(String.format(
                        "INSERT INTO %s VALUES('%s', %s, %s);",
                        PlayerQuestDatabase.TABLE_PLAYER_ACTIVE_QUEST, uuid, quest.getQuest().id(), quest.getSecondsLeft()
                ));
                for (Map.Entry<QuestStep<?>, Integer> entry : quest.getStepsWithAmounts().entrySet()) {
                    sql.append(String.format(
                            "INSERT INTO %s VALUES('%s', %s, %s);",
                            PlayerQuestDatabase.TABLE_PLAYER_ACTIVE_QUEST_STEPS,
                            uuid, entry.getKey().getId(), entry.getValue()
                    ));
                }
            } else {
                // UPDATE ACTIVE PLAYER QUEST
                ActivePlayerQuest quest = activePlayerQuest.get();
                sql.append(String.format(
                        "UPDATE %s SET quest_id=%s, time_left=%s WHERE uuid='%s' LIMIT 1;",
                        PlayerQuestDatabase.TABLE_PLAYER_ACTIVE_QUEST, quest.getQuest().id(), quest.getSecondsLeft(), uuid
                ));
                // DELETE STEPS BECAUSE, may be more id's than in new active quest
                sql.append(String.format("DELETE FROM %s WHERE uuid='%s';",
                        PlayerQuestDatabase.TABLE_PLAYER_ACTIVE_QUEST_STEPS, uuid));

                for (Map.Entry<QuestStep<?>, Integer> entry : quest.getStepsWithAmounts().entrySet()) {
                    sql.append(String.format(
                            "INSERT INTO %s VALUES('%s', %s, %s);",
                            PlayerQuestDatabase.TABLE_PLAYER_ACTIVE_QUEST_STEPS,
                            uuid, entry.getKey().getId(), entry.getValue()
                    ));
                }
            }
        }
        for (Map.Entry<Integer, Timestamp> newFoundQuest : dbInfo.getNewFoundQuests().entrySet()) {
            sql.append(String.format(
                    "INSERT INTO %s VALUES('%s', %s, '%s');",
                    PlayerQuestDatabase.TABLE_PLAYER_FOUND_QUESTS, uuid, newFoundQuest.getKey(), newFoundQuest.getValue()
            ));
        }

        for (Map.Entry<Integer, Timestamp> newCompletedQuest : dbInfo.getNewCompletedQuests().entrySet()) {
            sql.append(String.format(
                    "INSERT INTO %s VALUES('%s', %s, '%s');",
                    PlayerQuestDatabase.TABLE_PLAYER_COMPLETED_QUESTS, uuid, newCompletedQuest.getKey(), newCompletedQuest.getValue()
            ));
        }

        sql.append(String.format("UPDATE %s SET last_logout='%s'", TABLE_PLAYER, Timestamp.from(lastLogout)))
                .append(dbInfo.isMarkLanguageDirty() ? ", language='" + questPlayer.getLanguage().getLanguageKey() + "'" : "")
                .append(dbInfo.isMarkCoinsDirty() ? ", coins=" + questPlayer.getCoins() : "")
                .append(" WHERE uuid='").append(uuid).append("' LIMIT 1;");

        return sql.toString();
    }
}
