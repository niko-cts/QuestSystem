package net.playlegend.questsystem.gui.builder;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import chatzis.nikolas.mc.nikoapi.util.Utils;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.gui.GUIHelper;
import net.playlegend.questsystem.quest.reward.QuestReward;
import net.playlegend.questsystem.quest.reward.RewardType;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

/**
 * Stores methods to open and modify the rewards of a new quest.
 *
 * @author Niko
 * @see QuestBuilder
 */
public class RewardBuildingGUI {

	private RewardBuildingGUI() {
		throw new UnsupportedOperationException();
	}

	protected static void openAllSetRewards(QuestBuilder builder) {
		CustomInventory menu = new CustomInventory(Utils.getPerfectInventorySize(builder.rewards.size() + 1));
		for (QuestReward<?> reward : builder.rewards) {
			menu.addItem(new ItemBuilder(reward.getRewardDisplayItem(builder.language))
							.addLore(builder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_REWARD_REMOVE).split(";"))
							.craft(),
					new ClickAction() {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							builder.rewards.remove(reward);
							openAllSetRewards(builder);
						}
					});
		}

		GUIHelper.fillInventoryWithBackAndOpen(builder.questPlayer, menu, (questPlayer) -> builder.openMenu());
	}

	protected static void addNewRewardSelection(QuestBuilder builder) {
		CustomInventory menu = new CustomInventory(Utils.getPerfectInventorySize(RewardType.values().length + 1));
		for (RewardType type : RewardType.values()) {
			menu.addItem(new ItemBuilder(Material.GOLD_INGOT)
							.setName(ChatColor.YELLOW + type.name())
							.setLore(builder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_ADD).split(";")).craft(),
					new ClickAction() {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							if (type.getConstructorParameter() == Integer.class || type.getConstructorParameter() == int.class) {
								AnvilInsertionHelper.acceptNumberInAnvilMenu(builder.questPlayer,
										TranslationKeys.QUESTS_BUILDER_MODIFY_INTEGER, type.name(), amount -> {
											addNewRewardFromType(builder, type, amount);
											openAllSetRewards(builder);
										});
							} else if (type.getConstructorParameter() == ItemStack.class) {
								openItemRewardMenu(builder);
							} else {
								QuestSystem.getInstance().getLogger().log(Level.WARNING, "Type not implemented in builder: ", type);
							}
						}
					});
		}

		GUIHelper.fillInventoryWithBackAndOpen(builder.questPlayer, menu, (questPlayer) -> builder.openMenu());
	}

	private static void openItemRewardMenu(QuestBuilder builder) {
		builder.openItemInsertion(itemStack -> {
			if (itemStack != null && itemStack.getType() != Material.AIR)
				addNewRewardFromType(builder, RewardType.ITEM, itemStack);
			openAllSetRewards(builder);
		});
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
