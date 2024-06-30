package net.playlegend.questsystem.npc;

import chatzis.nikolas.mc.nikoapi.item.ItemBuilder;
import chatzis.nikolas.mc.nikoapi.util.LocationUtil;
import chatzis.nikolas.mc.npcsystem.NPC;
import chatzis.nikolas.mc.npcsystem.NPCSkin;
import lombok.Getter;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationKeys;
import net.playlegend.questsystem.util.ColorConverterUtil;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
public class FindNPC extends NPC {

	private final Quest quest;

	public FindNPC(UUID uuid, String wholeName, Location location, NPCSkin skin, Set<UUID> visibleTo, boolean withAi,
	               Quest quest) {
		super(uuid, wholeName, location, skin, visibleTo, withAi);
		this.quest = quest;

		click(event -> {
			QuestPlayer player = QuestSystem.getInstance().getPlayerHandler().getPlayer(event.getPlayer().getPlayer());
			if (player == null) return;
			String foundBook = player.getLanguage().translateMessage(TranslationKeys.QUESTS_NPC_FOUNDBOOK,
							List.of("${name}", "${description}"),
							List.of(ColorConverterUtil.convertToBlackColors(quest.name()), ColorConverterUtil.convertToBlackColors(quest.description())))
					.replace("/n", "\n");
			if (player.getFoundQuests().containsKey(quest)) {
				foundBook = player.getLanguage().translateMessage(TranslationKeys.QUESTS_NPC_FOUNDBOOK_ALREADY)
				            + ";" + foundBook;
			} else {
				player.foundQuest(quest);
			}
			player.openBook(new ItemBuilder(Material.WRITTEN_BOOK).addPage(foundBook.split(";")).craft());
		});
	}

	public String toString(Language language) {
		return language.translateMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_NPC_LIST_FIND_ELEMENT,
				List.of("${name}", "${questname}", "${location}"),
				List.of(getName(), quest.name(), LocationUtil.locationToString(getLocation())));
	}
}
