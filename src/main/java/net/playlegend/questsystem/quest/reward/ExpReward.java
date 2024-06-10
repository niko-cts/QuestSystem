package net.playlegend.questsystem.quest.reward;

import lombok.Data;
import net.playlegend.questsystem.QuestPlayer;
import org.bukkit.Sound;

@Data
public class ExpReward implements IQuestReward {

    private final int amount;

    /**
     * Will be called, when a player finished the quest.
     * The player should receive the reward.
     *
     * @param player {@link QuestPlayer} - the quest player who finished the quest.
     */
    @Override
    public void rewardPlayer(QuestPlayer player) {
        player.getPlayer().setLevel(player.getPlayer().getLevel() + getAmount());
        player.playSound(Sound.ENTITY_PLAYER_LEVELUP);
    }
}
