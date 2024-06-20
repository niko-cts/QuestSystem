package net.playlegend.questsystem.quest.builder;

import lombok.NonNull;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.reward.IQuestReward;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.quest.steps.QuestStepType;
import net.playlegend.questsystem.translation.Language;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.Consumer;

import java.util.*;

/**
 * This class is used to create whole new Quests.
 * It is instantiated once through a /questadmin create command.
 *
 * @author Niko
 */
public class QuestBuilder implements Listener {

    private static final Map<UUID, QuestBuilder> EDITING_PLAYERS = new HashMap<>();
    private static final int ITEM_SLOT_INSERT_INDEX = 1;

    public static void addPlayer(QuestPlayer questPlayer) {
        EDITING_PLAYERS.compute(questPlayer.getUniqueId(), (uuid, questBuilder) -> {
            if (questBuilder == null)
                questBuilder = new QuestBuilder(questPlayer);
            questBuilder.openMenu();
            return questBuilder;
        });
    }

    private static void removePlayer(UUID uuid) {
        EDITING_PLAYERS.computeIfPresent(uuid, (uuid1, questBuilder) -> {
            HandlerList.unregisterAll(questBuilder);
            return null;
        });
    }

    @NonNull final QuestPlayer questPlayer;
    @NonNull final Language language;
    private String name;
    private String description;
    final List<IQuestReward> rewards;
    final List<QuestStep> steps;
    private long finishTimeInSeconds;
    private boolean timerRunsOffline;
    private boolean isPublic;

    Consumer<ItemStack> addingItemMode;

    public QuestBuilder(@NonNull QuestPlayer questPlayer) {
        this.questPlayer = questPlayer;
        this.language = questPlayer.getLanguage();
        this.name = null;
        this.addingItemMode = null;
        this.description = null;
        this.rewards = new ArrayList<>();
        this.steps = new ArrayList<>();
        this.finishTimeInSeconds = 3600;
        this.timerRunsOffline = false;
        this.isPublic = false;
        QuestSystem.getInstance().getServer().getPluginManager().registerEvents(this, QuestSystem.getInstance());
        EDITING_PLAYERS.put(questPlayer.getUniqueId(), this);
    }

    public void openMenu() {
        // click
        RewardBuildingGUI.openAllSetRewards(this);
        // shiftclick
        RewardBuildingGUI.addNewRewardSelection(this);


    }

    private void addQuestStepMenu() {
        for (QuestStepType type : QuestStepType.values()) {
            if (type.getQuestStepClass() == Integer.class || type.getConstructorParameter() == int.class) {

            } else if (type.getConstructorParameter() == ItemStack.class) {
                menu.addItem();
            }
        }
    }


    private void finish() {
        if (name == null || description == null || steps.isEmpty()) {
            openMenu();
            return;
        }
        QuestSystem.getInstance().getQuestManager().addQuest(
                name, description, rewards, steps,
        );
    }

    @EventHandler
    public void onFinishBook(PlayerEditBookEvent event) {
        if (!event.getPlayer().getUniqueId().equals(questPlayer.getUniqueId())) return;
        BookMeta newBookMeta = event.getNewBookMeta();
        description = ChatColor.translateAlternateColorCodes('&',
                String.join(";", newBookMeta.getPages()));
        openMenu();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!event.getPlayer().getUniqueId().equals(questPlayer.getUniqueId())) return;
        if (addingItemMode != null) {
            addingItemMode.accept(event.getInventory().getItem(ITEM_SLOT_INSERT_INDEX));
            addingItemMode = null;
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removePlayer(event.getPlayer().getUniqueId());
    }
}
