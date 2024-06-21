package net.playlegend.questsystem.quest;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import lombok.NonNull;
import net.playlegend.questsystem.quest.reward.QuestReward;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.QuestTimingsUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record Quest(int id,
                    @NonNull String name,
                    @NonNull String description,
                    boolean isPublic,
                    @NonNull List<QuestReward<?>> rewards,
                    @NonNull List<QuestStep<?>> completionSteps,
                    long finishTimeInSeconds,
                    boolean timerRunsOffline
                    ) {

    public ItemStack getQuestItem(Language language) {
        return new ItemBuilder(Material.WRITABLE_BOOK)
                .setName(name)
                .setLore(language.translateMessage(TranslationKeys.QUESTS_GUI_QUEST_DETAILS_ITEM_LORE,
                        List.of("${duration}", "${rewards}", "${tasks}"),
                        List.of(QuestTimingsUtil.calculateNextDuration(finishTimeInSeconds), rewards.size(), completionSteps.size())))
                .craft();
    }

    public ItemStack getRewardItem(Language language) {
        return new ItemBuilder(Material.GOLD_INGOT)
                .setName(language.translateMessage(TranslationKeys.QUESTS_GUI_QUEST_REWARD_NAME))
                .setLore(rewards.stream().map(r -> ChatColor.GRAY + "- " + r.getRewardPreview(language)).toList())
                .craft();
    }

    public ItemStack getStepItem(Language language) {
        return new ItemBuilder(Material.IRON_DOOR)
                .setName(language.translateMessage(TranslationKeys.QUESTS_GUI_QUEST_STEPS_NAME))
                .setLore(completionSteps.stream().map(s -> ChatColor.GRAY + "- " + s.getTaskLine(language)).toList())
                .craft();
    }
}
