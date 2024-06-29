package net.playlegend.questsystem.commands.admin;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.commands.handler.APISubCommand;
import net.playlegend.questsystem.gui.QuestSpecificGUI;
import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.quest.Quest;
import net.playlegend.questsystem.translation.TranslationKeys;
import org.bukkit.ChatColor;

import java.util.Iterator;
import java.util.List;

public class QuestListSubCommand extends APISubCommand {

	public QuestListSubCommand() {
		super("list", "command.quest.admin.list", TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_LIST_USAGE);
	}

	@Override
	public void onCommand(QuestPlayer questPlayer, String[] arguments) {
		if (arguments.length == 0) {
			List<Quest> quests = QuestSystem.getInstance().getQuestManager().getQuests();
			if (quests.isEmpty()) {
				questPlayer.sendMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_LIST_NO_QUESTS);
			} else {
				Iterator<Quest> questIterator = quests.iterator();
				TextComponent textComponent = new TextComponent(questPlayer.getLanguage().translateMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_LIST));
				String hoverMsg = questPlayer.getLanguage().translateMessage(TranslationKeys.QUESTS_COMMAND_ADMIN_QUEST_LIST_HOVER);

				while (questIterator.hasNext()) {
					Quest quest = questIterator.next();
					TextComponent questComponent = new TextComponent(quest.name());
					questComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/questadmin list " + quest.name().replace("§", "&")));
					questComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverMsg.replace("${name}", quest.name()))));

					textComponent.addExtra(questComponent);
					if (questIterator.hasNext())
						textComponent.addExtra(", ");
				}

				questPlayer.getPlayer().spigot().sendMessage(textComponent);
			}
		} else {
			String name = ChatColor.translateAlternateColorCodes('&', arguments[0]);
			Quest quest = getQuestByNameOrMessageError(questPlayer, name);
			if (quest != null) {
				QuestSpecificGUI.openQuestGUI(questPlayer, quest, true, goBackPlayer -> goBackPlayer.getPlayer().closeInventory());
			}
		}
	}
}
