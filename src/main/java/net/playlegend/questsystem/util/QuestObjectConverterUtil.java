package net.playlegend.questsystem.util;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.quest.reward.RewardType;
import net.playlegend.questsystem.quest.steps.*;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

/**
 * This is a helper class to parse a given string from the database into the needed object.
 * Currently, it supports QuestStepType and QuestRewardType.
 * <br>
 * The string from the database is taken and will apply a conversion of that String. If the String is not in the expected format, will throw an error.
 *
 * @author Niko
 */
public class QuestObjectConverterUtil {

    /**
     * Converter method that tries to create the QuestStep object with the given special parameter.
     *
     * @param objectAsString String - the string from the database which needs to be converted to the object.
     * @param type           QuestStepType - the type of the quest, to get the object from.
     * @return QuestStep - the created step object
     * @throws IllegalArgumentException, IllegalStateException, IOException, ClassNotFoundException, ClassCastException - If the given object is wrong, multiple exception could be thrown.
     */
    public static QuestStep instantiateQuestStepFromTypeAndParameter(
            final QuestStepType type,
            final int stepId,
            final int order,
            final int maxAmount,
            final String objectAsString)
            throws IllegalArgumentException, IllegalStateException, IOException, ClassNotFoundException, ClassCastException {
        return switch (type) {
            case KILL -> new KillQuestStep(stepId, order, maxAmount, EntityType.valueOf(objectAsString));
            case SPEAK -> new TalkToNPCQuestStep(stepId, order, maxAmount, UUID.fromString(objectAsString));
            case MINE -> new MineQuestStep(stepId, order, maxAmount, Material.valueOf(objectAsString));
            case CRAFT ->
                    new CraftQuestStep(stepId, order, maxAmount, ItemToBase64ConverterUtil.fromBase64(objectAsString));
            default -> {
                QuestSystem.getInstance().getLogger().log(Level.WARNING, "{0} is not implemented in converter!", type);
                throw new IllegalStateException(type + " is not implemented in converter!");
            }
        };
    }


    /**
     * Converter method that tries to convert the given String from the ResultSet to the Object from the type.
     * The object that needs to be converted is {@link RewardType#getQuestRewardInstance(Object)}}.
     *
     * @param objectAsString String - the string from the database which needs to be converted to the object.
     * @param type           RewardType - the type of the reward, to get the object from.
     * @return Object - the parsed step object
     * @throws IllegalArgumentException, IllegalStateException, IOException, ClassNotFoundException, ClassCastException - If the given object is wrong, multiple exception could be thrown.
     */
    public static Object convertDatabaseStringToQuestRewardObject(final String objectAsString, final RewardType type)
            throws IllegalArgumentException, IllegalStateException, IOException, ClassNotFoundException, ClassCastException {
        if (Integer.class == type.getConstructorParameter() || int.class == type.getConstructorParameter())
            return Integer.parseInt(objectAsString);

        if (ItemStack.class == type.getConstructorParameter())
            return ItemToBase64ConverterUtil.fromBase64(objectAsString);

        QuestSystem.getInstance().getLogger().log(Level.WARNING, "{0} is not implemented in converter!", type);
        throw new IllegalStateException(type + " is not implemented in converter!");
    }

    public static String convertObjectToDatabaseString(Object paramterObject) throws IOException {
        if (paramterObject.getClass() == ItemStack.class)
            return ItemToBase64ConverterUtil.toBase64((ItemStack) paramterObject);
        if (paramterObject.getClass().isEnum()) {
            return ((Enum<?>) paramterObject).name();
        }
        return paramterObject.toString();
    }
}
