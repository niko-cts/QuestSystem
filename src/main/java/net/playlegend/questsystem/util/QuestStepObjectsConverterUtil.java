package net.playlegend.questsystem.util;

import net.playlegend.questsystem.QuestSystem;
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

public class QuestStepObjectsConverterUtil {

	/**
	 * Converter method that tries to convert the given String from the ResultSet to the Object from the type.
	 * The object that needs to be converted is {@link QuestStepType#getStepObject()}
	 *
	 * @param objectAsString String - the string from the database which needs to be converted to the object.
	 * @param type           QuestStepType - the type of the quest, to get the object from.
	 * @return Object - the parsed step object
	 * @throws IllegalArgumentException, IOException, ClassNotFoundException, ClassCastException - If the given object is wrong, multiple exception could be thrown.
	 */
	public static Object convertResultSetObjectToStepObject(final String objectAsString, final QuestStepType type)
			throws IllegalArgumentException, IOException, ClassNotFoundException, ClassCastException {
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
		if (!type.getStepObject().isInstance(object)) {
			throw new IllegalArgumentException("Converted to wrong instance!");
		}
		return object;
	}

}
