package chatzis.nikolas.questsystem.quest.reward;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.util.MaterialConverterUtil;
import chatzis.nikolas.questsystem.player.QuestPlayer;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemReward extends QuestReward<ItemStack> {

    public ItemReward(ItemStack item) {
        super(RewardType.ITEM, item);
    }

    /**
     * Will be called, when a player finished the quest.
     * The player should receive the reward.
     *
     * @param player {@link QuestPlayer} - the quest player who finished the quest.
     */
    @Override
    public void rewardPlayer(QuestPlayer player) {
        player.addItem(getRewardObject().clone());
        player.sendMessage(TranslationKeys.QUESTS_REWARD_ITEM_MESSAGE, "${name}",
		        getRewardObject().hasItemMeta() && Objects.requireNonNull(getRewardObject().getItemMeta()).hasDisplayName() ?
                getRewardObject().getItemMeta().getDisplayName() :
                        ChatColor.BLUE + MaterialConverterUtil.convertMaterialToName(getRewardObject().getType()));
    }

    /**
     * Returns a one-liner that previews the reward. E.g. "10 level"
     *
     * @param language Language - the language to translate in
     * @return String - the reward explanation
     */
    @Override
    public String getRewardPreview(Language language) {
        return language.translateMessage(TranslationKeys.QUESTS_REWARD_ITEM_PREVIEW);
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
        return new ItemBuilder(getRewardObject())
                .addLore(language.translateMessage(TranslationKeys.QUESTS_REWARD_ITEM_LORE).split(";")).craft();
    }
}
