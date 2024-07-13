package chatzis.nikolas.questsystem.quest.reward;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;


/**
 * CoinsReward will reward the player with coins.
 *
 * @author Niko
 */
public class CoinsReward extends QuestReward<Integer> {

    public CoinsReward(Integer amount) {
        super(RewardType.COINS, amount);
    }

    /**
     * Will be called, when a player finished the quest.
     * The player should receive the reward.
     *
     * @param player {@link QuestPlayer} - the quest player who finished the quest.
     */
    @Override
    public void rewardPlayer(QuestPlayer player) {
        player.setCoins(player.getCoins() + getRewardObject());
        player.playSound(Sound.ENTITY_PLAYER_LEVELUP);
        player.sendMessage(TranslationKeys.QUESTS_REWARD_COINS_MESSAGE, "${amount}", getRewardObject());
    }

    /**
     * Returns a one-liner that previews the reward. E.g. "10 level"
     *
     * @param language Language - the language to translate in
     * @return String - the reward explanation
     */
    @Override
    public String getRewardPreview(Language language) {
        return language.translateMessage(TranslationKeys.QUESTS_REWARD_COINS_PREVIEW, "${amount}", getRewardObject());
    }

    /**
     * Returns an ItemStack that explains the reward.
     * E.g., new ItemStack(EXPERIENCE).setLore("10 exp")
     *
     * @param language Language - the language to translate in
     * @return ItemStack - the item explaining the reward
     */
    @Override
    public ItemStack getRewardDisplayItem(Language language) {
        return new ItemBuilder(Material.GOLD_INGOT)
                .setName(language.translateMessage(TranslationKeys.QUESTS_REWARD_COINS_NAME, "${amount}", getRewardObject()))
                .setLore(language.translateMessage(TranslationKeys.QUESTS_REWARD_COINS_LORE, "${amount}", getRewardObject()).split(";")).craft();
    }
}
