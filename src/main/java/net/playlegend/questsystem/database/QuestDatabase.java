package net.playlegend.questsystem.database;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.quest.reward.QuestReward;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.util.QuestObjectConverterUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class QuestDatabase {

    protected static final String TABLE_QUESTS = "system_quests_overview";
    protected static final String TABLE_QUEST_STEPS_INFO = "system_quests_steps";
    private static final String TABLE_QUEST_REWARD_INFO = "system_quests_rewards";
    private static final String TABLE_QUEST_REWARD_MAP = "system_quests_has_rewards";

    private static QuestDatabase instance;

    public static QuestDatabase getInstance() {
        if (instance == null)
            instance = new QuestDatabase();
        return instance;
    }

    private final DatabaseHandler dbHandler;

    /**
     * Creates all database tables regarding basic quest informations
     */
    private QuestDatabase() {
        this.dbHandler = DatabaseHandler.getInstance();


        dbHandler.createTableIfNotExists(TABLE_QUESTS, List.of(
                "id int NOT NULL PRIMARY KEY AUTO_INCREMENT",
                "name VARCHAR(36) NOT NULL",
                "description VARCHAR(1000) NOT NULL",
                "public BOOLEAN NOT NULL DEFAULT 1",
                "finish_time BIGINT default 3600",
                "timer_runs_offline BOOLEAN NOT NULL DEFAULT 1")
        );

        dbHandler.createTableIfNotExists(TABLE_QUEST_STEPS_INFO, List.of(
                "id INT NOT NULL",
                "quest_id INT NOT NULL",
                "type VARCHAR(36) NOT NULL",
                "amount INT NOT NULL DEFAULT 1",
                "step_order INT NOT NULL DEFAULT 1",
                "step_object VARCHAR(1000) NOT NULL",
                "PRIMARY KEY(id, quest_id)",
                "CONSTRAINT fk_stepForQuestId FOREIGN KEY(quest_id) REFERENCES " + TABLE_QUESTS + "(id)")
        );


        dbHandler.createTableIfNotExists(TABLE_QUEST_REWARD_INFO, List.of(
                "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT",
                "type VARCHAR(36) NOT NULL",
                "reward_object VARCHAR(1000) NOT NULL")
        );

        dbHandler.createTableIfNotExists(TABLE_QUEST_REWARD_MAP, List.of(
                "reward_id INT NOT NULL",
                "quest_id INT NOT NULL",
                "CONSTRAINT pk_rewardmaps PRIMARY KEY (reward_id, quest_id)",
                "CONSTRAINT fk_quest_id FOREIGN KEY (quest_id) REFERENCES " + TABLE_QUESTS + "(id)",
                "CONSTRAINT fk_reward_id FOREIGN KEY (reward_id) REFERENCES " + TABLE_QUEST_REWARD_INFO + "(id)")
        );
    }

    /**
     * Returns the query of all quests (overview of quests)
     * <p>SELECT * FROM {@link QuestDatabase#TABLE_QUESTS}</p>
     *
     * @return ResultSet - All quest infos
     */
    public ResultSet getAllQuests() {
        return this.dbHandler.select(TABLE_QUESTS, List.of("*"), "");
    }

    /**
     * Returns the query of every quest step for specified questId
     * <p>SELECT * FROM {@link QuestDatabase#TABLE_QUEST_STEPS_INFO} WHERE quest_id={param}</p>
     *
     * @param questId int - the quest id
     * @return ResultSet - All quest steps for the quest
     */
    public ResultSet getAllQuestSteps(int questId) {
        return this.dbHandler.select(TABLE_QUEST_STEPS_INFO, List.of("*"), "WHERE quest_id=" + questId);
    }

    /**
     * Returns the query of all all quest rewards + quest_id's
     * <p>SELECT reward_id, quest_id, type, reward_object FROM  {@link QuestDatabase#TABLE_QUEST_REWARD_INFO} JOIN {@link QuestDatabase#TABLE_QUEST_REWARD_MAP} AND quest_id={param}</p>
     *
     * @param questId int - the quest id
     * @return ResultSet - All quest rewards with the quest_id's
     */
    public ResultSet getAllQuestRewards(int questId) {
        return this.dbHandler.executeQuery(String.format(
                "SELECT type, reward_object FROM %s JOIN %s ON id=reward_id AND quest_id=%s",
                TABLE_QUEST_REWARD_INFO, TABLE_QUEST_REWARD_MAP, questId));
    }


    public void deleteQuest(int id) {
        this.dbHandler.delete(List.of(TABLE_QUEST_REWARD_MAP, TABLE_QUEST_STEPS_INFO, TABLE_QUESTS),
                List.of("WHERE quest_id=" + id, "WHERE quest_id=" + id, "WHERE id=" + id + " LIMIT 1"));
    }

    /**
     * Inserts new Quest, QuestStep, QuestRewards and maps the rewards with the quest
     *
     * @param name                String - quest name
     * @param description         String - quest description
     * @param rewards             List - all rewards the quest has
     * @param steps               List - all steps the quest has
     * @param finishTimeInSeconds long - the quest countdown in seconds
     * @param isPublic            boolean - if quest is public
     * @param timerRunsOffline    boolean - if countdown runs offline as well
     * @return Optional<Integer> - the id of the created quest
     */
    public Optional<Integer> insertNewQuest(String name, String description, List<QuestReward<?>> rewards, List<QuestStep<?>> steps, long finishTimeInSeconds, boolean isPublic, boolean timerRunsOffline) {

        Logger log = QuestSystem.getInstance().getLogger();

        Integer questId = this.dbHandler.insertIntoTableAndGetGeneratedKey(TABLE_QUESTS,
                List.of("name", "description", "public", "finish_time", "timer_runs_offline"),
                List.of(name, description, isPublic ? 1 : 0, finishTimeInSeconds, timerRunsOffline ? 1 : 0));
        if (questId == null) {
            log.severe("Could not insert quest in database");
            return Optional.empty();
        }

        List<QuestReward<?>> unaddedRewards = new ArrayList<>(rewards);
        Map<Integer, QuestReward<?>> existingRewards = new HashMap<>();
        boolean success = insertRewardObjectsAndPutExistingAndRemoveFromRewardObjects(unaddedRewards, existingRewards);
        if (!success) return Optional.empty();

        if (!unaddedRewards.isEmpty()) {
            StringBuilder insertRewards = new StringBuilder()
                    .append("INSERT INTO ").append(TABLE_QUEST_REWARD_INFO).append(" (type,reward_object) VALUES");
            Iterator<QuestReward<?>> notInDBRewardsIterator = unaddedRewards.iterator();
            while (notInDBRewardsIterator.hasNext()) {
                QuestReward<?> notInDBRewards = notInDBRewardsIterator.next();
                try {
                    insertRewards.append("('").append(notInDBRewards.getRewardType().name()).append("','")
                            .append(QuestObjectConverterUtil.convertObjectToDatabaseString(notInDBRewards.getRewardObject())).append("')");
                    if (notInDBRewardsIterator.hasNext())
                        insertRewards.append(",");
                } catch (IOException exception) {
                    log.log(Level.SEVERE, "Could not convert new RewardObject to database string", exception);
                }
            }
            insertRewards.append(";");
            if (!dbHandler.execute(insertRewards.toString())) {
                log.log(Level.SEVERE, "Could not insert new rewards in database");
                deleteQuest(questId);
                return Optional.empty();
            }
        }

        success = insertRewardObjectsAndPutExistingAndRemoveFromRewardObjects(unaddedRewards, existingRewards);
        if (!success) {
            log.log(Level.SEVERE, "A quest reward could not be retrieved by the database! Will not add quest! Remaining rewards: {0}",
                    unaddedRewards.stream().map(r -> r.getRewardType().name()).collect(Collectors.joining(",")));
            deleteQuest(questId);
            return Optional.empty();
        }

        if (!unaddedRewards.isEmpty()) {
            log.log(Level.WARNING, "There was a quest reward which could not be added to the database: {0}",
                    unaddedRewards.stream().map(r -> r.getRewardType().name()).collect(Collectors.joining(",")));
        }

        List<Object> allSteps = new ArrayList<>();
        Iterator<QuestStep<?>> newSteps = steps.iterator();
        while (newSteps.hasNext()) {
            QuestStep<?> step = newSteps.next();
            try {
                allSteps.add(step.getId());
                allSteps.add(questId);
                allSteps.add(step.getType().name());
                allSteps.add(step.getMaxAmount());
                allSteps.add(step.getOrder());
                allSteps.add(QuestObjectConverterUtil.convertObjectToDatabaseString(step.getStepObject()));
                if (newSteps.hasNext())
                    allSteps.add(null);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Could not convert new StepObject to database string", e);
            }
        }
        if (allSteps.isEmpty()) {
            log.log(Level.SEVERE, "There is no quest step which can be added to the quest. Aborting...");
            deleteQuest(questId);
            return Optional.empty();
        }

        List<String> tableNames = new ArrayList<>();
        List<List<Object>> allValues = new ArrayList<>();
        tableNames.add(TABLE_QUEST_STEPS_INFO);
        allValues.add(allSteps);

        if (!existingRewards.isEmpty()) {
            List<Object> allRewardMappings = new ArrayList<>();
            Iterator<Integer> rewardMapIterator = existingRewards.keySet().iterator();
            while (rewardMapIterator.hasNext()) {
                Integer rewardId = rewardMapIterator.next();
                allRewardMappings.add(rewardId);
                allRewardMappings.add(questId);
                if (rewardMapIterator.hasNext())
                    allRewardMappings.add(null);
            }
            tableNames.add(TABLE_QUEST_REWARD_MAP);
            allValues.add(allRewardMappings);
        }

        dbHandler.insertIntoTable(tableNames, allValues);
        return Optional.of(questId);
    }

    /**
     * Makes a sql request which returns the columns of all rewards which are in currentRewards. On return, will put the reward id into the map with the object and removes it from currentRewards.
     *
     * @param currentRewards  List - will check which reward in this list is in the database, after the methods all rewards which are not in the database will remain in this list
     * @param existingRewards Map - will put all rewards which are in the database in this map and adds their id
     * @return boolean - operation was successful
     */
    private boolean insertRewardObjectsAndPutExistingAndRemoveFromRewardObjects(List<QuestReward<?>> currentRewards, Map<Integer, QuestReward<?>> existingRewards) {
        try (ResultSet rewardSet = this.dbHandler.select(TABLE_QUEST_REWARD_INFO, List.of("*"),
                "WHERE type IN (" + currentRewards.stream().map(t -> "'" + t.getRewardType().name() + "'").collect(Collectors.joining(","))
                        + ") AND reward_object IN(" + currentRewards.stream().map(t -> "'" + t.getRewardObject().toString() + "'").collect(Collectors.joining(","))
                        + ")")) {
            while (rewardSet != null && rewardSet.next()) {
                String type = rewardSet.getString("type");
                String object = rewardSet.getString("reward_object");

                QuestReward<?> existingReward = currentRewards.stream().filter(f -> f.getRewardType().name().equals(type) && f.getRewardObject().toString().equals(object)).findFirst().orElseThrow();
                currentRewards.remove(existingReward);
                existingRewards.put(rewardSet.getInt("id"), existingReward);
            }
            return true;
        } catch (SQLException exception) {
            QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not get existing rewards in database!", exception);
            return false;
        }
    }
}
