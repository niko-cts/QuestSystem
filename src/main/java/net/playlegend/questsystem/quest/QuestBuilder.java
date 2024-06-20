package net.playlegend.questsystem.quest;

import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.reward.IQuestReward;
import net.playlegend.questsystem.quest.steps.QuestStep;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;

public class QuestBuilder implements Listener {

    private static final Map<UUID, QuestBuilder> editingPlayers = new HashMap<>();

    private final QuestPlayer questPlayer;
    private String name;
    private String description = null;
    private final List<IQuestReward> rewards;
    private final List<QuestStep> steps;

    public QuestBuilder(QuestPlayer questPlayer) {
        this.questPlayer = questPlayer;
        this.name = null;
        this.description = null;
        this.rewards = new ArrayList<>();
        this.steps = new ArrayList<>();
    }

    public void openMenu() {

    }

    public void openSteps() {

    }

    private void finish() {
        if (name == null || description == null || steps.isEmpty()) {
            openMenu();
            return;
        }
    }

    @EventHandler
    public void onFinishBook(PlayerEditBookEvent event) {
        if (!event.getPlayer().getUniqueId().equals(questPlayer.getPlayer().getUniqueId())) return;
        BookMeta newBookMeta = event.getNewBookMeta();
        description = ChatColor.translateAlternateColorCodes('&',
                String.join(";", newBookMeta.getPages()));
        openMenu();
    }

}
