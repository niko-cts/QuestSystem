package net.playlegend.questsystem.quest.builder;

import chatzis.nikolas.mc.nikoapi.inventory.ClickAction;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import lombok.NonNull;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.gui.GUIHelper;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;
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
		addPlayer(questPlayer, null);
	}

	public static void addPlayer(QuestPlayer questPlayer, Quest quest) {
		EDITING_PLAYERS.compute(questPlayer.getUniqueId(), (uuid, questBuilder) -> {
			if (questBuilder == null)
				questBuilder = quest == null ? new QuestBuilder(questPlayer) : new QuestBuilder(questPlayer, quest);
			questBuilder.openMenu();
			return questBuilder;
		});
	}

	public static QuestBuilder getBuilder(UUID uuid) {
		return EDITING_PLAYERS.get(uuid);
	}

	private static void removePlayer(UUID uuid) {
		EDITING_PLAYERS.computeIfPresent(uuid, (uuid1, questBuilder) -> {
			HandlerList.unregisterAll(questBuilder);
			return null;
		});
	}

	private Integer questId;
	@NonNull
	final QuestPlayer questPlayer;
	@NonNull
	final Language language;
	private String name;
	@Setter
	private String description;
	final List<QuestReward<?>> rewards;
	final List<QuestStep<?>> steps;
	private long finishTimeInSeconds;
	private boolean timerRunsOffline;
	private boolean isPublic;

	private Consumer<ItemStack> addingItemMode;

	private QuestBuilder(@NonNull QuestPlayer questPlayer, Quest quest) {
		this(questPlayer);
		this.name = quest.name();
		this.description = quest.description();
		this.steps.addAll(quest.completionSteps());
		this.rewards.addAll(quest.rewards());
		this.finishTimeInSeconds = quest.finishTimeInSeconds();
		this.timerRunsOffline = quest.timerRunsOffline();
		this.isPublic = quest.isPublic();
		this.questId = quest.id();
	}

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
	}

	public void openMenu() {
		CustomInventory menu = new CustomInventory(27);

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
						.setLore(description != null ? description.split(";") : new String[0])
						.addLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_DESCRIPTION_LORE).split(";")).craft(),
				new ClickAction(true) {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						TextComponent textComponent = new TextComponent(language.translateMessage(TranslationKeys.QUESTS_BUILDER_DESCRIPTION_CLICK_TEXT));
						textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/questadmin create description " + (description != null ? description.replace("§", "&") : "")));
						textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(language.translateMessage(TranslationKeys.QUESTS_BUILDER_DESCRIPTION_CLICK_HOVER))));
						apiPlayer.getPlayer().spigot().sendMessage(textComponent);
					}
				});

		menu.setItem(12, new ItemBuilder(GUIHelper.getRewardItem(language, rewards))
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
				.setLore(steps.stream().sorted(Comparator.comparingInt(QuestStep::getOrder)).map(s -> ChatColor.YELLOW + "" + s.getOrder() + ChatColor.GRAY + ". " + s.getTaskLine(language)).toList())
				.addLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_STEPS_LORE).split(";"))
				.craft(), new ClickAction() {
			@Override
			public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
				QuestStepBuildingGUI.openStepCreationSelection(QuestBuilder.this);
			}

			@Override
			public void onShiftClick(APIPlayer apiPlayer, ItemStack itemStack, int slot) {
				QuestStepBuildingGUI.openAllSetSteps(QuestBuilder.this);
			}
		});

		menu.setItem(14, new ItemBuilder(Material.REDSTONE)
						.setName(language.translateMessage(TranslationKeys.QUESTS_BUILDER_TIMER_OFFLINE_NAME, "${active}", timerRunsOffline))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_TIMER_OFFLINE_LORE).split(";"))
						.addEnchantment(timerRunsOffline ? Enchantment.ARROW_INFINITE : null, 1, true, false).craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						timerRunsOffline = !timerRunsOffline;
						openMenu();
					}
				});

		menu.setItem(15, new ItemBuilder(Material.NETHER_STAR)
						.setName(language.translateMessage(TranslationKeys.QUESTS_BUILDER_PUBLIC_NAME, "${active}", isPublic))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_PUBLIC_LORE).split(";"))
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
						AnvilInsertionHelper.acceptNumberInAnvilMenu(questPlayer, TranslationKeys.QUESTS_BUILDER_MODIFY_INTEGER, finishTimeInSeconds + "", seconds -> {
							finishTimeInSeconds = Math.max(1, seconds);
							openMenu();
						});
					}
				});

		menu.setItem(26, UsefulItems.HEAD_A()
						.setName(language.translateMessage(TranslationKeys.QUESTS_BUILDER_CREATE_NAME))
						.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_CREATE_LORE).split(";")).craft(),
				new ClickAction() {
					@Override
					public void onClick(APIPlayer apiPlayer, ItemStack itemStack, int i) {
						if (name != null && description != null && !steps.isEmpty()) {
							if (questId == null && QuestSystem.getInstance().getQuestManager().getQuestByName(name).isPresent()) {
								questPlayer.sendMessage(TranslationKeys.QUESTS_BUILDER_CREATE_NAME_ALREADY_EXISTS);
								return;
							}
							new BukkitRunnable() {
								@Override
								public void run() {
									if (questId == null) {
										if (QuestSystem.getInstance().getQuestManager().addQuest(
												name, description, rewards, steps, finishTimeInSeconds, isPublic, timerRunsOffline))
											questPlayer.sendMessage(TranslationKeys.QUESTS_BUILDER_SUCCESSFUL_CREATED, "${name}", name);
										else
											questPlayer.sendMessage(TranslationKeys.QUESTS_BUILDER_SUCCESSFUL_CREATED_ERROR, "${name}", name);
									} else {
										if (QuestSystem.getInstance().getQuestManager().updateQuest(
												questId, name, description, rewards, steps, finishTimeInSeconds, isPublic, timerRunsOffline))
											questPlayer.sendMessage(TranslationKeys.QUESTS_BUILDER_SUCCESSFUL_UPDATED, "${name}", name);
										else
											questPlayer.sendMessage(TranslationKeys.QUESTS_BUILDER_SUCCESSFUL_UPDATED_ERROR, "${name}", name);
									}
								}
							}.runTaskAsynchronously(QuestSystem.getInstance());

							setCloseInventory(true);
							removePlayer(apiPlayer.getUniqueId());
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
	public void onInteraction(InventoryClickEvent event) {
		if (!event.getWhoClicked().getUniqueId().equals(questPlayer.getUniqueId())) return;
		if (addingItemMode == null) return;
		if (event.getRawSlot() != event.getSlot()) return;
		if (event.getRawSlot() < event.getInventory().getSize() && event.getRawSlot() != ITEM_SLOT_INSERT_INDEX)
			event.setCancelled(true);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (!event.getPlayer().getUniqueId().equals(questPlayer.getUniqueId())) return;
		if (addingItemMode != null) {
			ItemStack item = event.getInventory().getItem(ITEM_SLOT_INSERT_INDEX);
			addingItemMode.accept(item);
			if (item != null)
				event.getPlayer().getInventory().addItem(item);
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
		menu.setItem(ITEM_SLOT_INSERT_INDEX - 1, UsefulItems.ARROW_RIGHT().setName(" ")
				.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_ITEM_INSERTION).split(";")).craft());
		menu.setItem(ITEM_SLOT_INSERT_INDEX + 1, UsefulItems.ARROW_LEFT().setName(" ")
				.setLore(language.translateMessage(TranslationKeys.QUESTS_BUILDER_MODIFY_ITEM_INSERTION).split(";")).craft());
		questPlayer.getPlayer().openInventory(menu);
	}
}
