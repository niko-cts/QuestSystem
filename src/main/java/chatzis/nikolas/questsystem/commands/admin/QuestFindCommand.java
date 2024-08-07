package chatzis.nikolas.questsystem.commands.admin;

import chatzis.nikolas.questsystem.commands.handler.APISubCommand;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.quest.Quest;
import chatzis.nikolas.questsystem.translation.TranslationKeys;

public class QuestFindCommand extends APISubCommand {

    public QuestFindCommand() {
        super("find", "command.quest.admin.find", TranslationKeys.QUESTS_COMMAND_QUEST_FIND_USAGE, 1, "finden", "found", "discover");
    }

    /**
     * Method that gets called if a subcommand is executed by a player.
     *
     * @param questPlayer {@link QuestPlayer} - the executor.
     * @param arguments   String[] - the command arguments.
     * @since 0.0.1
     */
    @Override
    public void onCommand(QuestPlayer questPlayer, String[] arguments) {
        Quest quest = getQuestByNameOrMessageError(questPlayer, arguments[0]);
        if (quest != null) {
            if (quest.isPublic()) {
                questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_QUEST_FIND_ISPUBLIC);
                return;
            }
            if (questPlayer.getFoundQuests().containsKey(quest)) {
                questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_QUEST_FIND_ALREADY_FOUND);
                return;
            }

            questPlayer.foundQuest(quest);
        }
    }
}
