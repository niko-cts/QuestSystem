package net.playlegend.questsystem.quest.steps;

import lombok.Data;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

@Data
public abstract class QuestStep {

    private final int id;
    private final int order;
    private final int maxAmount;

    public boolean isStepComplete(int currentAmount) {
        return currentAmount >= maxAmount;
    }

    /**
     * Will be called when the event is triggered and the given player has the Quest with this active QuestStep.
     *
     * @param player QuestPlayer - the player who triggered the event
     * @param event  Event - the event that was triggered
     * @return boolean - player did a quest step
     */
    public abstract boolean checkIfEventExecutesQuestStep(QuestPlayer player, Event event);

    /**
     * Returns a one-liner that explains the quest step in an active quest E.g. "Mine 5 more blocks"
     *
     * @param language Language - the language to translate in
     * @return String - the quest step explanation
     */
    public abstract String getActiveTaskLine(Language language, int currentAmount);

    /**
     * Returns a one-liner that explains the quest step. E.g. "Mine 10 blocks"
     *
     * @param language Language - the language to translate
     * @return String - one-liner that explains the step
     */
    public abstract String getTaskLine(Language language);

    /**
     * Returns an ItemStack that explains the quest step and inserts the active amount.
     * E.g., new ItemStack(Material.STONE).setLore("Mine this block 5 more times")
     *
     * @param language      Language - the language to translate in
     * @param currentAmount int - the current step amount
     * @return ItemStack - the item explaining the step
     */
    public abstract ItemStack getActiveTask(Language language, int currentAmount);


    /**
     * Returns an ItemStack that explains the quest step.
     * E.g., new ItemStack(Material.STONE).setLore("Mine this block 10 times")
     *
     * @param language Language - the language to translate in
     * @return ItemStack - the item explaining the step
     */
    public abstract ItemStack getTaskItem(Language language);
}

