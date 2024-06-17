package net.playlegend.questsystem.listener;

import lombok.RequiredArgsConstructor;
import net.playlegend.questsystem.player.PlayerHandler;
import net.playlegend.questsystem.player.QuestPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;

@RequiredArgsConstructor
public class QuestStepListener implements Listener {

    private final PlayerHandler playerHandler;

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        checkConditionsAndCallSteps(event.getPlayer(), event);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            checkConditionsAndCallSteps(player, event);
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            checkConditionsAndCallSteps(event.getEntity().getKiller(), event);
        }
    }

    /**
     * Checks if the event executes a quest step.
     * Checks if a quest step is finished.
     * If so, calls {@link QuestPlayer#checkAndFinishActiveQuest()}
     *
     * @param player Player - player who made the event
     * @param event Event - triggered event
     */
    private void checkConditionsAndCallSteps(Player player, Event event) {
        if (event instanceof Cancellable cancellable && cancellable.isCancelled()) return;
        QuestPlayer questPlayer = playerHandler.getPlayer(player.getUniqueId());
        if (questPlayer == null) return;
        questPlayer.getActivePlayerQuest()
                .flatMap(quest ->
                        quest.getNextUncompletedSteps().stream()
                                .filter(step -> step.checkIfEventExecutesQuestStep(questPlayer, event))
                                // adds one to the current amount and returns true if a step is now completed
                                .filter(step -> questPlayer.playerDidQuestStep(quest, step)).findAny())
                // CHECK QUEST FINISHED
                .ifPresent(step -> questPlayer.checkAndFinishActiveQuest());
    }
}
