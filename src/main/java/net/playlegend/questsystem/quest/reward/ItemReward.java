package net.playlegend.questsystem.quest.reward;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public record ItemReward(ItemStack item) implements IQuestReward {

    /**
     * Will be called, when a player finished the quest.
     * The player should receive the reward.
     *
     * @param player {@link QuestPlayer} - the quest player who finished the quest.
     */
    @Override
    public void rewardPlayer(QuestPlayer player) {
        player.addItem(item.clone());
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
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta != null && itemMeta.hasLore() && itemMeta.getLore() != null ?
                itemMeta.getLore() : List.of());
        lore.addAll(List.of(language.translateMessage(TranslationKeys.QUESTS_REWARD_ITEM_LORE).split(";")));

        return new ItemBuilder(item).setLore(lore).craft();
    }
}
