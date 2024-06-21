package net.playlegend.questsystem.quest.builder;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import chatzis.nikolas.mc.nikoapi.util.Utils;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.gui.GUIHelper;
import net.playlegend.questsystem.quest.reward.RewardType;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.quest.steps.QuestStepType;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.ItemToBase64ConverterUtil;
import net.playlegend.questsystem.util.QuestObjectConverterUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Comparator;
import java.util.UUID;
import java.util.logging.Level;

public class QuestStepBuildingGUI {

	private QuestStepBuildingGUI() {
		throw new UnsupportedOperationException();
	}

	protected static void openAllSetSteps(QuestBuilder builder) {
		CustomInventory menu = new CustomInventory(Utils.getPerfectInventorySize(builder.steps.size() + 1));
		for (QuestStep step : builder.steps) {
			menu.addItem(new ItemBuilder(step.getTaskItem(builder.language))
							.addLore(builder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_REMOVE).split(";"))
							.craft(),
					new ClickAction() {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							builder.rewards.remove(step);
							openAllSetSteps(builder);
						}
					});
		}

		GUIHelper.fillInventoryWithBackAndOpen(builder.questPlayer, menu, null, (questPlayer, o) -> builder.openMenu());
	}


	protected static void addNewRewardSelection(QuestBuilder builder) {
		CustomInventory menu = new CustomInventory(Utils.getPerfectInventorySize(RewardType.values().length + 1));
		for (QuestStepType type : QuestStepType.values()) {
			menu.addItem(new ItemBuilder(Material.GOLD_INGOT)
							.setName(ChatColor.YELLOW + type.name())
							.setLore(builder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_ADD).split(";")).craft(),
					new ClickAction() {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							openAddNewQuestStepForType(builder, type, 1, 1, null);
						}
					});
		}

		GUIHelper.fillInventoryWithBackAndOpen(builder.questPlayer, menu, null, (questPlayer, o) -> builder.openMenu());
	}

	private static void openAddNewQuestStepForType(QuestBuilder questBuilder, QuestStepType type,
	                                               int order, int amount, Object parameter) {
		CustomInventory menu = new CustomInventory(9 * 3);
		menu.setItem(12, new ItemBuilder(Material.WHITE_BANNER)
						.setName(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_ORDER_NAME, "${order}", order))
						.setLore(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_ORDER_LORE).split(";"))
						.setAmount(Math.min(64, Math.max(1, order)))
						.craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						AnvilInsertionHelper.acceptNumberInAnvilMenu(questBuilder.language, TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_ORDER_LORE, "",
								newOrder -> openAddNewQuestStepForType(questBuilder, type, Math.max(1, newOrder), amount, parameter));
					}
				});

		menu.setItem(14, new ItemBuilder(Material.SPLASH_POTION)
						.setName(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_AMOUNT_NAME, "${amount}", amount))
						.setLore(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_AMOUNT_LORE).split(";"))
						.setAmount(Math.min(64, Math.max(1, amount)))
						.craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						AnvilInsertionHelper.acceptNumberInAnvilMenu(questBuilder.language, TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_ORDER_LORE, "",
								newOrder -> openAddNewQuestStepForType(questBuilder, type, order, Math.max(1, amount), parameter));
					}
				});

		menu.setItem(16, new ItemBuilder(Material.ITEM_FRAME)
						.setName(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_PARAMETER_NAME, "${parameter}", parameter))
						.setLore(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, "${class}", type.getConstructorParameter()).split(";"))
						.setAmount(Math.min(64, Math.max(1, amount)))
						.craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						Class<?> constructorParameter = type.getConstructorParameter();
						if (constructorParameter == int.class || constructorParameter == Integer.class) {
							AnvilInsertionHelper.acceptNumberInAnvilMenu(questBuilder.language, TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, constructorParameter.toString(),
									newParameter -> openAddNewQuestStepForType(questBuilder, type, order, amount, newParameter));
						} else if (constructorParameter == Material.class) {
							questBuilder.openItemInsertion(newItem -> openAddNewQuestStepForType(questBuilder, type, order, amount, newItem.getType()));
						} else if(constructorParameter == ItemStack.class) {
							questBuilder.openItemInsertion(newItem -> openAddNewQuestStepForType(questBuilder, type, order, amount, newItem));
						} else if (constructorParameter == UUID.class) {
							AnvilInsertionHelper.acceptUUIDInAnvilMenu(questBuilder.language, TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, constructorParameter.toString(),
									newParameter -> openAddNewQuestStepForType(questBuilder, type, order, amount, newParameter));
						} else if (constructorParameter == EntityType.class) {
							AnvilInsertionHelper.acceptEntityType(questBuilder.language, TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, constructorParameter.toString(),
									newParameter -> openAddNewQuestStepForType(questBuilder, type, order, amount, newParameter));
						} else {
							QuestSystem.getInstance().getLogger().log(Level.WARNING, "QuestStepType is not implemented in creation: ", type);
						}
					}
				});

		menu.setItem(17, UsefulItems.HEAD_A()
						.setName(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_ACCEPT)).craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						if (parameter == null) return;

						String newParameter = parameter.toString();
						try {
							if (type == QuestStepType.CRAFT) {
								newParameter = ItemToBase64ConverterUtil.toBase64((ItemStack) parameter);
							}
							questBuilder.steps.add(QuestObjectConverterUtil.instantiateQuestStepFromTypeAndParameter(type,
									questBuilder.steps.stream().map(QuestStep::getId).max(Comparator.comparingInt(id -> id)).orElse(1),
									order, amount, newParameter));
						} catch (IOException | ClassNotFoundException e) {
							QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not add QuestStep in QuestBuilder", e);
						}

						questBuilder.openMenu();
					}
				});

		GUIHelper.fillInventoryWithBackAndOpen(questBuilder.questPlayer, menu, null,
				(questPlayer, o) -> questBuilder.openMenu());
	}

}