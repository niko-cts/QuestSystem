package chatzis.nikolas.questsystem.quest.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class QuestNotFoundException extends IllegalArgumentException {

    private final int questId;
    private final String message;

}
