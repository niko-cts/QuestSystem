package chatzis.nikolas.questsystem.quest.exception;

public class QuestStepNotFoundException extends QuestNotFoundException{

    public QuestStepNotFoundException(int questId, String message) {
        super(questId, message);
    }
}
