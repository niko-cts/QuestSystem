package net.playlegend.questsystem;

import net.playlegend.questsystem.quest.steps.*;
import net.playlegend.questsystem.util.ItemToBase64ConverterUtil;
import net.playlegend.questsystem.util.QuestObjectConverterUtil;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QuestStepConverterTest {

	@Test
	public void stepConverter_TypeKill_thenInstantiate() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		KillQuestStep questStep = new KillQuestStep(1, 0,1, EntityType.BAT);

		Object entityType = QuestObjectConverterUtil.convertDatabaseStringToQuestStepObject(EntityType.BAT.toString(), QuestStepType.KILL);
		assertNotNull(entityType);
		assertEquals(EntityType.BAT, entityType);

		QuestStep createdQuestStep = QuestStepType.KILL.getQuestStepInstance(1, 0, 1, EntityType.BAT);
		assertEquals(questStep, createdQuestStep);
	}

	@Test
	public void stepConverter_TypeSpeak_thenInstantiate() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		UUID uuid = UUID.randomUUID();
		TalkToNPCQuestStep questStep = new TalkToNPCQuestStep(1, 0,1, uuid);

		Object convertedUUID = QuestObjectConverterUtil.convertDatabaseStringToQuestStepObject(uuid.toString(), QuestStepType.SPEAK);
		assertNotNull(convertedUUID);
		assertEquals(uuid, convertedUUID);

		QuestStep createdQuestStep = QuestStepType.SPEAK.getQuestStepInstance(1, 0, 1, uuid);
		assertEquals(questStep, createdQuestStep);
	}


	@Test
	public void stepConverter_TypeMine_thenInstantiate() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		MineQuestStep questStep = new MineQuestStep(1, 0,1, Material.STONE);

		Object convertedUUID = QuestObjectConverterUtil.convertDatabaseStringToQuestStepObject(Material.STONE.toString(), QuestStepType.MINE);
		assertNotNull(convertedUUID);
		assertEquals(Material.STONE, convertedUUID);

		QuestStep createdQuestStep = QuestStepType.MINE.getQuestStepInstance(1, 0, 1, Material.STONE);
		assertEquals(questStep, createdQuestStep);
	}


	@Test
	public void stepConverter_TypeCraft_thenInstantiate() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		ItemStack itemStack = new ItemStack(Material.IRON_SWORD);
		CraftQuestStep questStep = new CraftQuestStep(1, 0,1, itemStack);

		String base64FromDatabase = ItemToBase64ConverterUtil.toBase64(itemStack);
		assertNotNull(base64FromDatabase);

		Object convertedUUID = QuestObjectConverterUtil.convertDatabaseStringToQuestStepObject(base64FromDatabase, QuestStepType.CRAFT);
		assertNotNull(convertedUUID);
		assertEquals(itemStack, convertedUUID);

		QuestStep createdQuestStep = QuestStepType.CRAFT.getQuestStepInstance(1, 0, 1, itemStack);
		assertEquals(questStep, createdQuestStep);
	}

	@Test
	public void check_AllStepsAreTested() {
		for (QuestStepType type : QuestStepType.values()) {
			try {
				QuestObjectConverterUtil.convertDatabaseStringToQuestStepObject("", type);
			} catch (IllegalStateException exception) {
				assertEquals(0, 1, "There is a quest step which is not implemtend: " + type);
			} catch (Throwable ignored) {
				// will throw something, because "" is given
			}
		}
		assertEquals(4, QuestStepType.values().length, "There may be some quest steps which have not been tested yet");
	}

}
