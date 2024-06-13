package net.playlegend.questsystem.database;

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
                "language VARCHAR(36) NOT NULL DEFAULT en-US"
        ));
    }

    public void insertPlayer(UUID uuid, String languageKey) {
        this.databaseHandler.insertIntoTable(List.of(TABLE_PLAYER), List.of(List.of(uuid.toString(), Timestamp.from(Instant.now()), languageKey)));
    }

    public ResultSet getPlayerInfos(UUID uuid) {
        return this.databaseHandler.select(TABLE_PLAYER, List.of("*"), "WHERE uuid='" + uuid + "' LIMIT 1");
    }

    public void updateAllPlayerData(QuestPlayer questPlayer) {
        UUID uuid = questPlayer.getPlayer().getUniqueId();
        PlayerDatabaseInformationHolder dbInfo = questPlayer.getPlayerDatabaseInformationHolder();
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
                        PlayerQuestDatabase.TABLE_PLAYER_ACTIVE_QUEST, uuid, quest.getActiveQuest().id(), quest.getSecondsLeft()
                ));
                for (Map.Entry<QuestStep, Integer> entry : quest.getStepsWithAmounts().entrySet()) {
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
                        PlayerQuestDatabase.TABLE_PLAYER_ACTIVE_QUEST, quest.getActiveQuest().id(), quest.getSecondsLeft(), uuid
                ));
                // DELETE STEPS BECAUSE, may be more id's than in new active quest
                sql.append(String.format("DELETE FROM %s WHERE uuid='%s';",
                        PlayerQuestDatabase.TABLE_PLAYER_ACTIVE_QUEST_STEPS, uuid));

                for (Map.Entry<QuestStep, Integer> entry : quest.getStepsWithAmounts().entrySet()) {
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
                    "INSERT INTO %s VALUES('%s', %s, %s)",
                    PlayerQuestDatabase.TABLE_PLAYER_FOUND_QUESTS, uuid, newFoundQuest.getKey(), newFoundQuest.getValue()
            ));
        }

        for (Map.Entry<Integer, Timestamp> newCompletedQuest : dbInfo.getNewCompletedQuests().entrySet()) {
            sql.append(String.format(
                    "INSERT INTO %s VALUES('%s', %s, %s)",
                    PlayerQuestDatabase.TABLE_PLAYER_COMPLETED_QUESTS, uuid, newCompletedQuest.getKey(), newCompletedQuest.getValue()
            ));
        }

        sql.append(String.format("UPDATE %s SET last_logout=%s", TABLE_PLAYER, Timestamp.from(Instant.now())))
                .append(dbInfo.isMarkLanguageDirty() ? ", language='" + questPlayer.getCurrentLanguage().getLanguageKey() + "'" : "")
                .append("WHERE uuid='").append(uuid).append("' LIMIT 1;");

        this.databaseHandler.executeSQL(sql.toString());
    }
}
