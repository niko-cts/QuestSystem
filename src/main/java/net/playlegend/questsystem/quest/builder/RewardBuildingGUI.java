package net.playlegend.questsystem.quest.builder;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.quest.reward.IQuestReward;
import net.playlegend.questsystem.quest.reward.RewardType;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public class RewardBuildingGUI {

    private RewardBuildingGUI() {
        throw new UnsupportedOperationException();
    }

    protected static void openAllSetRewards(QuestBuilder builder) {
        for (IQuestReward reward : builder.rewards) {
            menu.addItem(new ItemBuilder(reward.getRewardDisplayItem(builder.language))
                    .adLore(builder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_REMOVE).split(";"))
                    .craft(),
                    new ClickAction() {

            builder.rewards.remove(reward);
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

    private static void openItemRewardMenu(QuestBuilder builder) {
        builder.questPlayer.getPlayer().closeInventory();
        builder.addingItemMode = itemStack -> {
            addNewRewardFromType(builder, RewardType.ITEM, itemStack);
            builder.openMenu();
        };
    }

    private static void addNewRewardFromType(QuestBuilder questBuilder, RewardType type, Object parameter) {
        try {
            questBuilder.rewards.add(type.getQuestRewardInstance(parameter));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not add quest reward type {0} to QuestBuilder: {1}",
                    new Object[]{type, e.getMessage()});
        }
    }


}
