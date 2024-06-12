package net.playlegend.questsystem.util;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.quest.reward.RewardType;
import net.playlegend.questsystem.quest.steps.QuestStepType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

/**
 * This is a helper class to parse a given string from the database into the needed object.
 * Currently, supports QuestStepType and QuestRewardType.
 * <br>
 * The string from the database is taken and will apply a conversion of that String. If the String is not in the expected format, will throw an error.
 * @author Niko
 */
public class QuestObjectConverterUtil {

	/**
	 * Converter method that tries to convert the given String from the ResultSet to the Object from the type.
	 * The object that needs to be converted is {@link QuestStepType#getConstructorStepClass()}
	 *
	 * @param objectAsString String - the string from the database which needs to be converted to the object.
	 * @param type           QuestStepType - the type of the quest, to get the object from.
	 * @return Object - the parsed step object
	 * @throws IllegalArgumentException, IllegalStateException, IOException, ClassNotFoundException, ClassCastException - If the given object is wrong, multiple exception could be thrown.
	 */
	public static Object convertDatabaseStringToQuestStepObject(final String objectAsString, final QuestStepType type)
			throws IllegalArgumentException, IllegalStateException, IOException, ClassNotFoundException, ClassCastException {
		Object object = switch (type) {
			case KILL -> EntityType.valueOf(objectAsString);
			case SPEAK -> UUID.fromString(objectAsString);
			case MINE -> Material.valueOf(objectAsString);
			// Credits to https://gist.github.com/graywolf336/8153678
			case CRAFT -> (ItemStack) new BukkitObjectInputStream(new ByteArrayInputStream(Base64Coder.decode(objectAsString))).readObject();
			default -> {
				QuestSystem.getInstance().getLogger().log(Level.WARNING, "{0} is not implemented in converter!", type);
				yield null;
			}
		};
		if (!type.getConstructorStepClass().isInstance(object)) {
			throw new IllegalStateException("Converted to wrong instance!");
		}
		return object;
	}


	/**
	 * Converter method that tries to convert the given String from the ResultSet to the Object from the type.
	 * The object that needs to be converted is {@link RewardType#getRewardClass()}.
	 *
	 * @param objectAsString String - the string from the database which needs to be converted to the object.
	 * @param type           RewardType - the type of the reward, to get the object from.
	 * @return Object - the parsed step object
	 * @throws IllegalArgumentException, IllegalStateException, IOException, ClassNotFoundException, ClassCastException - If the given object is wrong, multiple exception could be thrown.
	 */
	public static Object convertDatabaseStringToQuestRewardObject(final String objectAsString, final RewardType type)
			throws IllegalArgumentException, IllegalStateException, IOException, ClassNotFoundException, ClassCastException {
		Object object = switch (type) {
			case LVL, COINS -> Integer.parseInt(objectAsString);
			// Credits to https://gist.github.com/graywolf336/8153678
			case ITEM -> ItemToBase64ConverterUtil.fromBase64(objectAsString);
			default -> {
				QuestSystem.getInstance().getLogger().log(Level.WARNING, "{0} is not implemented in converter!", type);
				yield null;
			}
		};
		if (!type.getConstructorParameter().isInstance(object)) {
			throw new IllegalStateException("Converted to wrong instance!");
		}
		return object;
	}
}
