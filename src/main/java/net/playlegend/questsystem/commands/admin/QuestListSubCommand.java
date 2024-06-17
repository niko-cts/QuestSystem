package net.playlegend.questsystem.commands.admin;

import chatzis.nikolas.mc.nikoapi.NikoAPI;
import chatzis.nikolas.mc.nikoapi.inventory.CustomInventory;
import chatzis.nikolas.mc.nikoapi.item.UsefulItems;
import chatzis.nikolas.mc.nikoapi.player.APIPlayer;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;

public class QuestListSubCommand extends APISubCommand {
	public QuestListSubCommand() {
		super("list", "command.quest.admin.list", TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_USAGE, TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_DESCRIPTION);
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		if (arguments.length == 0) {
			StringBuilder quests = new StringBuilder();
			Iterator<Quest> questIterator = QuestSystem.getInstance().getQuestManager().getQuests().iterator();
			if (!questIterator.hasNext()) {
				questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_LIST_NO_QUESTS);
			} else {
				while (questIterator.hasNext()) {
					quests.append(questIterator.next().name());
					if (questIterator.hasNext())
						quests.append(", ");
				}
				questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_LIST, "${quests}", quests.toString());
			}
		} else {
			String name = arguments[0];
			Quest quest = getQuestByNameOrMessageError(questPlayer, name);
			if (quest != null) {
				List<ItemStack> questItem = quest.getQuestItem(questPlayer.getCurrentLanguage());
				CustomInventory menu = new CustomInventory(27);
				menu.fill(UsefulItems.BACKGROUND_BLACK);
				for (int i = 0, j = 10; i < questItem.size(); i++, j++) {
					menu.setItem(j, questItem.get(i));
				}

				APIPlayer apiPlayer = NikoAPI.getInstance().getPlayerHandler().getPlayer(questPlayer.getUniqueId());
				if (apiPlayer != null)
					menu.open(apiPlayer);
			}
		}
	}
}
