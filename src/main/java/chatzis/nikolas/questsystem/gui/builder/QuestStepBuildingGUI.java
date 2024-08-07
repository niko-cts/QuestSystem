package chatzis.nikolas.questsystem.gui.builder;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import chatzis.nikolas.mc.nikoapi.util.Utils;
import chatzis.nikolas.questsystem.QuestSystem;
import chatzis.nikolas.questsystem.gui.GUIHelper;
import chatzis.nikolas.questsystem.quest.reward.RewardType;
import chatzis.nikolas.questsystem.quest.steps.QuestStep;
import chatzis.nikolas.questsystem.quest.steps.QuestStepType;
import chatzis.nikolas.questsystem.translation.TranslationKeys;
import chatzis.nikolas.questsystem.util.ItemToBase64ConverterUtil;
import chatzis.nikolas.questsystem.util.QuestObjectConverterUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
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
		for (QuestStep<?> step : builder.steps) {
			menu.addItem(new ItemBuilder(step.getTaskItem(builder.language))
							.addLore(builder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_STEPS_REMOVE, "${id}", step.getId()).split(";"))
							.craft(),
					new ClickAction() {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							builder.steps.remove(step);
							openAllSetSteps(builder);
						}
					});
		}

		GUIHelper.fillInventoryWithBackAndOpen(builder.questPlayer, menu, (questPlayer) -> builder.openMenu());
	}


	protected static void openStepCreationSelection(QuestBuilder builder) {
		CustomInventory menu = new CustomInventory(Utils.getPerfectInventorySize(RewardType.values().length + 1));
		for (QuestStepType type : QuestStepType.values()) {
			menu.addItem(new ItemBuilder(Material.GOLD_INGOT)
							.setName(ChatColor.YELLOW + type.name())
							.setLore(builder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_ADD).split(";")).craft(),
					new ClickAction() {
						@Override
						public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
							openStepCreationMenu(builder, type, 1, 1, null);
						}
					});
		}

		GUIHelper.fillInventoryWithBackAndOpen(builder.questPlayer, menu, (questPlayer) -> builder.openMenu());
	}

	private static void openStepCreationMenu(QuestBuilder questBuilder, QuestStepType type,
	                                         int order, int amount, Object parameter) {
		CustomInventory menu = new CustomInventory(9 * 3);
		menu.setItem(10, new ItemBuilder(Material.WHITE_BANNER)
						.setName(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_ORDER_NAME, "${order}", order))
						.setLore(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_ORDER_LORE).split(";"))
						.setAmount(Math.min(64, Math.max(1, order)))
						.craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						AnvilInsertionHelper.acceptNumberInAnvilMenu(questBuilder.questPlayer, TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_ORDER_LORE, order + "",
								newOrder -> openStepCreationMenu(questBuilder, type, Math.max(1, newOrder), amount, parameter));
					}
				});

		menu.setItem(12, new ItemBuilder(Material.SPLASH_POTION)
						.setName(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_AMOUNT_NAME, "${amount}", amount))
						.setLore(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_AMOUNT_LORE).split(";"))
						.setAmount(Math.min(64, Math.max(1, amount)))
						.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
						.craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						AnvilInsertionHelper.acceptNumberInAnvilMenu(questBuilder.questPlayer, TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_ORDER_LORE, amount + "",
								newAmount -> openStepCreationMenu(questBuilder, type, order, Math.max(1, newAmount), parameter));
					}
				});


		menu.setItem(14, new ItemBuilder(Material.ITEM_FRAME)
						.setName(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_PARAMETER_NAME, "${parameter}",
								getParameterInCreation(parameter)))
						.setLore(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, "${input}", type.getConstructorParameter().getSimpleName()).split(";"))
						.craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						Class<?> constructorParameter = type.getConstructorParameter();
						if (constructorParameter == int.class || constructorParameter == Integer.class) {
							AnvilInsertionHelper.acceptNumberInAnvilMenu(questBuilder.questPlayer, TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, constructorParameter.getSimpleName(),
									newParameter -> openStepCreationMenu(questBuilder, type, order, amount, newParameter));
						} else if (constructorParameter == Material.class) {
							AnvilInsertionHelper.acceptMaterialInAnvilMenu(questBuilder.questPlayer, TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, constructorParameter.getSimpleName(),
									newParameter -> openStepCreationMenu(questBuilder, type, order, amount, newParameter));
						} else if (constructorParameter == ItemStack.class) {
							questBuilder.openItemInsertion(
									newItem -> openStepCreationMenu(questBuilder, type, order, amount, newItem));
						} else if (constructorParameter == UUID.class) {
							AnvilInsertionHelper.acceptUUIDInAnvilMenu(questBuilder.questPlayer, TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, constructorParameter.getSimpleName(),
									newParameter -> openStepCreationMenu(questBuilder, type, order, amount, newParameter));
						} else if (constructorParameter == EntityType.class) {
							AnvilInsertionHelper.acceptEntityType(questBuilder.questPlayer, TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, constructorParameter.getSimpleName(),
									newParameter -> openStepCreationMenu(questBuilder, type, order, amount, newParameter));
						} else {
							QuestSystem.getInstance().getLogger().log(Level.WARNING, "QuestStepType is not implemented in creation: ", type);
						}
					}
				});


		if (parameter != null) {
			String newParameter = parameter.toString();
			try {
				if (type.getConstructorParameter() == ItemStack.class) {
					newParameter = ItemToBase64ConverterUtil.toBase64((ItemStack) parameter);
				}
				int newStepId = questBuilder.steps.stream().max(Comparator.comparingInt(QuestStep::getId))
						                .map(QuestStep::getId).orElse(0) + 1;
				QuestStep<?> stepToCreate = QuestObjectConverterUtil.instantiateQuestStepFromTypeAndParameter(type, newStepId, order, amount, newParameter);

				menu.setItem(16, new ItemBuilder(stepToCreate.getTaskItem(questBuilder.language))
								.addLore(questBuilder.language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_ACCEPT_LORE)).craft(),
						new ClickAction() {
							@Override
							public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
								questBuilder.steps.add(stepToCreate);
								questBuilder.openMenu();
							}
						});
			} catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
				QuestSystem.getInstance().getLogger().log(Level.SEVERE, "Could not create QuestTask in QuestBuilder", e);
				questBuilder.questPlayer.sendMessage(TranslationKeys.QUESTS_BUILDER_STEPS_CREATION_ERROR, "${message}", e.getMessage());
			}
		}

		GUIHelper.fillInventoryWithBackAndOpen(questBuilder.questPlayer, menu, null,
				(questPlayer, o) -> questBuilder.openMenu());
	}

	private static String getParameterInCreation(Object parameter) {
		if (parameter == null)
			return "unset";
		return parameter.toString();
	}

}
