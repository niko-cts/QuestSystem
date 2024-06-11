package net.playlegend.questsystem;

import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.quest.steps.QuestStepType;
import net.playlegend.questsystem.util.QuestStepObjectsConverterUtil;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestQuestSteps {

	@Test
	public void stepConverter_TypeKill() throws IOException, ClassNotFoundException {
		Object entityType = QuestStepObjectsConverterUtil.convertResultSetObjectToStepObject(EntityType.BAT.toString(), QuestStepType.KILL);
		assertNotNull(entityType);
		assertEquals(EntityType.BAT, entityType);

	}

}
