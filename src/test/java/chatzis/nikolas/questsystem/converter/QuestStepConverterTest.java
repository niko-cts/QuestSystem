package chatzis.nikolas.questsystem.converter;

import be.seeseemelk.mockbukkit.MockBukkit;
import chatzis.nikolas.questsystem.quest.steps.*;
import chatzis.nikolas.questsystem.util.ItemToBase64ConverterUtil;
import chatzis.nikolas.questsystem.util.QuestObjectConverterUtil;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QuestStepConverterTest {

	@BeforeEach
	public void setUp() {
		 MockBukkit.mock();
	}

	@AfterEach
	public void tearDown() {
		MockBukkit.unmock();
	}

	@Test
	public void stepConverter_TypeKill_thenInstantiate() throws IOException, ClassNotFoundException {
		KillQuestStep questStep = new KillQuestStep(0, 0, 0, EntityType.BAT);

		QuestStep<?> createdQuestStep =
				QuestObjectConverterUtil.instantiateQuestStepFromTypeAndParameter(QuestStepType.KILL, 0,0 ,0, EntityType.BAT.toString());
		assertEquals(questStep, createdQuestStep);
	}

	@Test
	public void stepConverter_TypeSpeak_thenInstantiate() throws IOException, ClassNotFoundException {
		UUID uuid = UUID.randomUUID();
		TalkToNPCQuestStep questStep = new TalkToNPCQuestStep(0, 0, 0, uuid);

		QuestStep<?> createdQuestStep =
				QuestObjectConverterUtil.instantiateQuestStepFromTypeAndParameter(QuestStepType.SPEAK, 0, 0,0, uuid.toString());
		assertEquals(questStep, createdQuestStep);
	}


	@Test
	public void stepConverter_TypeMine_thenInstantiate() throws IOException, ClassNotFoundException {
		MineQuestStep questStep = new MineQuestStep(0, 0, 0, Material.STONE);

		QuestStep<?> createdQuestStep =
				QuestObjectConverterUtil.instantiateQuestStepFromTypeAndParameter(QuestStepType.MINE, 0,0, 0, Material.STONE.toString());
		assertEquals(questStep, createdQuestStep);
	}


	@Test
	public void stepConverter_TypeCraft_thenInstantiate() throws IOException, ClassNotFoundException {
		ItemStack itemStack = new ItemStack(Material.IRON_SWORD);
		CraftQuestStep questStep = new CraftQuestStep(0, 0, 0, itemStack);

		String base64FromDatabase = ItemToBase64ConverterUtil.toBase64(itemStack);
		assertNotNull(base64FromDatabase);

		QuestStep<?> createdQuestStep =
				QuestObjectConverterUtil.instantiateQuestStepFromTypeAndParameter(QuestStepType.CRAFT, 0, 0,0, base64FromDatabase);
		assertEquals(questStep, createdQuestStep);
	}

	@Test
	public void check_AllStepsAreTested() {
		for (QuestStepType type : QuestStepType.values()) {
			try {
				QuestObjectConverterUtil.instantiateQuestStepFromTypeAndParameter(type, 0, 0, 0, "");
			} catch (IllegalStateException exception) {
				assertEquals(0, 1, "There is a quest step which is not implemtend: " + type);
			} catch (Throwable ignored) {
				// will throw something, because "" is given
			}
		}
		assertEquals(4, QuestStepType.values().length, "There may be some quest steps which have not been tested yet");
	}

}
