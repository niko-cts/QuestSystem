package net.playlegend.questsystem.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.translation.Language;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Class which stores all important information about the player and the quest.
 * @author Niko
 */
@RequiredArgsConstructor
@Getter
@Setter
public class QuestPlayer {

    private final Player player;
    private Language currentLanguage;
    private int coins;

    private List<Quest> finishedQuests;
    private List<Quest> foundQuests;

    private Quest activeQuest;
    private QuestStep currentStep;



    public void sendMessage(String translationKey) {
        player.sendMessage(currentLanguage.translateMessage(translationKey));
    }

    public void sendMessage(String translationKey, String placeholder, String replacement) {
        player.sendMessage(currentLanguage.translateMessage(translationKey, placeholder, replacement));
    }

    public void sendMessage(String translationKey, List<String> placeholder, List<String> replacement) {
        player.sendMessage(currentLanguage.translateMessage(translationKey, placeholder, replacement));
    }

    public void playSound(Sound sound) {
        player.playSound(player, sound, 1, 1);
    }

    public void addItem(ItemStack... item) {
        player.getInventory().addItem(item);
    }
}
