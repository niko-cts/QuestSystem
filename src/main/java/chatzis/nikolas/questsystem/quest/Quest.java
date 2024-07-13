package chatzis.nikolas.questsystem.quest;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.questsystem.quest.reward.QuestReward;
import chatzis.nikolas.questsystem.quest.steps.QuestStep;
import chatzis.nikolas.questsystem.translation.Language;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import chatzis.nikolas.questsystem.util.QuestTimingsUtil;
import lombok.NonNull;
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
                .setLore(description.split(";"))
                .addLore(language.translateMessage(TranslationKeys.QUESTS_GUI_QUEST_DETAILS_ITEM_LORE,
                        List.of("${duration}", "${rewards}", "${tasks}"),
                        List.of(QuestTimingsUtil.convertSecondsToDHMS(language, finishTimeInSeconds), rewards.size(), completionSteps.size())).split(";"))
                .craft();
    }
}
