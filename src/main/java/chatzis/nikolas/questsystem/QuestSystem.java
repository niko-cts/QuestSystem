package chatzis.nikolas.questsystem;

import chatzis.nikolas.questsystem.commands.LanguageCommand;
import chatzis.nikolas.questsystem.commands.QuestCommand;
import chatzis.nikolas.questsystem.commands.admin.QuestAdminCommand;
import chatzis.nikolas.questsystem.commands.handler.CommandHandler;
import chatzis.nikolas.questsystem.database.DatabaseHandler;
import chatzis.nikolas.questsystem.database.PlayerInfoDatabase;
import chatzis.nikolas.questsystem.listener.QuestPlayerConnectionListener;
import chatzis.nikolas.questsystem.listener.QuestStepListener;
import chatzis.nikolas.questsystem.npc.NPCManager;
import chatzis.nikolas.questsystem.player.PlayerHandler;
import chatzis.nikolas.questsystem.quest.QuestManager;
import chatzis.nikolas.questsystem.quest.QuestSignManager;
import chatzis.nikolas.questsystem.translation.DefaultEnglishMessages;
import chatzis.nikolas.questsystem.translation.DefaultGermanMessages;
import chatzis.nikolas.questsystem.translation.LanguageHandler;
import chatzis.nikolas.questsystem.util.PlayerQuestDataSaveTimer;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class QuestSystem extends JavaPlugin {

	@Getter
	private static QuestSystem instance;
	private LanguageHandler languageHandler;
	private QuestManager questManager;
	private PlayerHandler playerHandler;
	private CommandHandler commandHandler;
	private QuestSignManager questSignManager;
	private NPCManager npcManager;


	@Override
	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();
		this.languageHandler = new LanguageHandler();
		this.playerHandler = new PlayerHandler();
		this.commandHandler = new CommandHandler();
		this.questManager = new QuestManager();
		this.questSignManager = new QuestSignManager(this);
		if (getServer().getPluginManager().isPluginEnabled("NPCSystem")) {
			this.npcManager = new NPCManager(this);
		}

		PlayerInfoDatabase.getInstance();

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new QuestPlayerConnectionListener(this, playerHandler), this);
		pm.registerEvents(new QuestStepListener(playerHandler), this);
		pm.registerEvents(questSignManager, this);
		if (npcManager != null) {
			pm.registerEvents(npcManager, this);
		}

		this.commandHandler.addCommands(new QuestAdminCommand(), new LanguageCommand(), new QuestCommand());

		new DefaultGermanMessages();
		new DefaultEnglishMessages();

		PlayerQuestDataSaveTimer.startsSaveTimer(this);
	}

	@Override
	public void onDisable() {
		this.playerHandler.saveAllPlayers();
		DatabaseHandler.getInstance().closeConnection();
	}

}
