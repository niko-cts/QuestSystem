package net.playlegend.questsystem.quest.builder;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.quest.reward.RewardType;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class QuestStepBuildingGUI {

    private QuestStepBuildingGUI() {
        throw new UnsupportedOperationException();
    }

    protected static void openAllSetSteps(QuestBuilder builder) {
        for (QuestStep step : builder.steps) {
            menu.addItem(new ItemBuilder(step.getTaskItem(builder.language))
                            .adLore(builder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_REMOVE).split(";"))
                            .craft(),
                    new ClickAction() {

            builder.rewards.remove(step);
                        openAllSetRewards(builder);
                    });
        }
    }


    protected static void addNewRewardSelection(QuestBuilder questBuilder) {
        for (RewardType type : RewardType.values()) {
            // on click:
            menu.addItem(new ItemBuilder(Material.GOLD_INGOT)
                    .setName(ChatColor.YELLOW + type.name())
                    .setLore(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_ADD).split(";")))

            openAddNewQuestStepForType(questBuilder, type, 1, 1, null);
        }
    }

    private static void openAddNewQuestStepForType(QuestBuilder questBuilder, RewardType type,
                                                   int order, int amount, Object parameter) {
        if (type.getConstructorParameter() == Integer.class || type.getConstructorParameter() == int.class) {
            AnvilInsertionHelper.acceptNumberInAnvilMenu(questBuilder.language,
                    TranslationKeys.QUESTS_BUILDER_MODIFY_INTEGER, type.name(),
                    amount -> addNewRewardFromType(questBuilder, type, amount));
        } else if (type.getConstructorParameter() == ItemStack.class) {
            openItemRewardMenu(questBuilder);
        } else {
            QuestSystem.getInstance().getLogger().log(Level.WARNING, "Type not implemented in builder: ", type);
        }
    }

}
