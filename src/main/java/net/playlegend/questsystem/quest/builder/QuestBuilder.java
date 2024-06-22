package net.playlegend.questsystem.quest.builder;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import lombok.NonNull;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.reward.QuestReward;
import net.playlegend.questsystem.quest.steps.QuestStep;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.QuestTimingsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.Consumer;

import java.util.*;

/**
 * This class is used to create whole new Quests.
 * It is instantiated once through a /questadmin create command.
 *
 * @author Niko
 */
public class QuestBuilder implements Listener {

	private static final Map<UUID, QuestBuilder> EDITING_PLAYERS = new HashMap<>();
	private static final int ITEM_SLOT_INSERT_INDEX = 1;

	public static void addPlayer(QuestPlayer questPlayer) {
		EDITING_PLAYERS.compute(questPlayer.getUniqueId(), (uuid, questBuilder) -> {
			if (questBuilder == null)
				questBuilder = new QuestBuilder(questPlayer);
			questBuilder.openMenu();
			return questBuilder;
		});
	}

	private static void removePlayer(UUID uuid) {
		EDITING_PLAYERS.computeIfPresent(uuid, (uuid1, questBuilder) -> {
			HandlerList.unregisterAll(questBuilder);
			return null;
		});
	}

	@NonNull
	final QuestPlayer questPlayer;
	@NonNull
	final Language language;
	private String name;
	private String description;
	final List<QuestReward<?>> rewards;
	final List<QuestStep<?>> steps;
	private long finishTimeInSeconds;
	private boolean timerRunsOffline;
	private boolean isPublic;

	private Consumer<ItemStack> addingItemMode;

	private QuestBuilder(@NonNull QuestPlayer questPlayer) {
		this.questPlayer = questPlayer;
		this.language = questPlayer.getLanguage();
		this.name = null;
		this.addingItemMode = null;
		this.description = null;
		this.rewards = new ArrayList<>();
		this.steps = new ArrayList<>();
		this.finishTimeInSeconds = 3600;
		this.timerRunsOffline = false;
		this.isPublic = true;
		QuestSystem.getInstance().getServer().getPluginManager().registerEvents(this, QuestSystem.getInstance());
		EDITING_PLAYERS.put(questPlayer.getUniqueId(), this);
	}

	public void openMenu() {
		CustomInventory menu = new CustomInventory(9 * 3);

		menu.setItem(10, new ItemBuilder(Material.PAPER)
						.setName(name == null ? ChatColor.YELLOW + "?" : name)
						.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_NAME_LORE).split(";")).craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						AnvilInsertionHelper.acceptStringInAnvilMenu(questPlayer, TranslationKeys.QUESTS_BUILDER_MODIFY_QUESTNAME, "", s -> {
							name = s;
							openMenu();
						});
					}
				});
		menu.setItem(11, new ItemBuilder(Material.WRITABLE_BOOK)
						.setName(language.translateMessage(TranslationKeys.QUESTS_BUILDER_DESCRIPTION_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_DESCRIPTION_LORE).split(";")).craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						apiPlayer.openBook(new ItemBuilder(Material.WRITABLE_BOOK)
								.addPage(description != null ? description.replace("§", "&").split(";") : new String[0]).craft());
					}
				});

		menu.setItem(12, new ItemBuilder(Material.GOLD_INGOT)
				.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_QUEST_REWARD_NAME))
				.setLore(rewards.stream().map(r -> ChatColor.GRAY + "- " + r.getRewardPreview(language)).toList())
				.addLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_REWARDS_LORE).split(";"))
				.craft(), new ClickAction() {
			@Override
			public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
				RewardBuildingGUI.addNewRewardSelection(QuestBuilder.this);
			}

			@Override
			public void onShiftClick(APIPlayer apiPlayer, ItemStack itemStack, int slot) {
				RewardBuildingGUI.openAllSetRewards(QuestBuilder.this);
			}
		});

		menu.setItem(13, new ItemBuilder(Material.IRON_DOOR)
				.setName(language.translateMessage(TranslationKeys.QUESTS_GUI_QUEST_STEPS_NAME))
				.setLore(steps.stream().map(s -> ChatColor.GRAY + "- " + s.getTaskLine(language)).toList())
				.addLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_LORE).split(";"))
				.craft(), new ClickAction() {
			@Override
			public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
				QuestStepBuildingGUI.addNewRewardSelection(QuestBuilder.this);
			}

			@Override
			public void onShiftClick(APIPlayer apiPlayer, ItemStack itemStack, int slot) {
				QuestStepBuildingGUI.openAllSetSteps(QuestBuilder.this);
			}
		});

		menu.setItem(14, new ItemBuilder(Material.REDSTONE)
						.setName(language.translateMessage(TranslationKeys.QUESTS_BUILDER_TIMER_OFFLINE_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_TIMER_OFFLINE_LORE, "${active}", timerRunsOffline).split(";"))
						.addEnchantment(timerRunsOffline ? Enchantment.ARROW_INFINITE : null, 1, true, false).craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						timerRunsOffline = !timerRunsOffline;
						openMenu();
					}
				});

		menu.setItem(15, new ItemBuilder(Material.NETHER_STAR)
						.setName(language.translateMessage(TranslationKeys.QUESTS_BUILDER_PUBLIC_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_PUBLIC_LORE, "${active}", isPublic).split(";"))
						.addEnchantment(isPublic ? Enchantment.ARROW_INFINITE : null, 1, true, false).craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						isPublic = !isPublic;
						openMenu();
					}
				});

		menu.setItem(16, new ItemBuilder(Material.CLOCK)
						.setName(language.translateMessage(TranslationKeys.QUESTS_BUILDER_TIMER_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_TIMER_LORE, "${duration}",
								QuestTimingsUtil.convertSecondsToDHMS(language, finishTimeInSeconds)).split(";")).craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						AnvilInsertionHelper.acceptNumberInAnvilMenu(questPlayer, TranslationKeys.QUESTS_BUILDER_MODIFY_INTEGER, "Countdown in seconds", seconds -> {
							finishTimeInSeconds = seconds;
							openMenu();
						});
					}
				});

		menu.setItem(26, UsefulItems.HEAD_A()
						.setName(language.translateMessage(TranslationKeys.QUESTS_BUILDER_CREATE_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_CREATE_LORE).split(";")).craft(),
				new ClickAction(true) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						if (name != null && description != null && !steps.isEmpty()) {
							QuestSystem.getInstance().getQuestManager().addQuest(
									name, description, rewards, steps, finishTimeInSeconds, isPublic, timerRunsOffline);
							questPlayer.sendMessage(TranslationKeys.QUESTS_BUILDER_SUCCESSFUL);
						}
					}
				});
		menu.fill(UsefulItems.BACKGROUND_BLACK);
		questPlayer.openCustomInv(menu);
	}

	@EventHandler
	public void onFinishBook(PlayerEditBookEvent event) {
		if (!event.getPlayer().getUniqueId().equals(questPlayer.getUniqueId())) return;
		BookMeta newBookMeta = event.getNewBookMeta();
		description = ChatColor.translateAlternateColorCodes('&',
				String.join(";", newBookMeta.getPages()));
		openMenu();
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (!event.getPlayer().getUniqueId().equals(questPlayer.getUniqueId())) return;
		if (addingItemMode != null) {
			addingItemMode.accept(event.getInventory().getItem(ITEM_SLOT_INSERT_INDEX));
			addingItemMode = null;
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		removePlayer(event.getPlayer().getUniqueId());
	}

	public void openItemInsertion(Consumer<ItemStack> itemStackConsumer) {
		questPlayer.getPlayer().closeInventory();
		addingItemMode = itemStackConsumer;
		Inventory menu = Bukkit.createInventory(questPlayer.getPlayer(), 9);
		menu.setItem(ITEM_SLOT_INSERT_INDEX - 1, UsefulItems.ARROW_RIGHT()
				.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_ITEM_INSERTION).split(";")).craft());
		menu.setItem(ITEM_SLOT_INSERT_INDEX + 1, UsefulItems.ARROW_LEFT()
				.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_ITEM_INSERTION).split(";")).craft());
		questPlayer.getPlayer().openInventory(menu);
	}
}
