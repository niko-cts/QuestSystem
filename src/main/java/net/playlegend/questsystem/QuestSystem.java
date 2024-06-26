package net.playlegend.questsystem;

import lombok.Getter;
import net.playlegend.questsystem.commands.LanguageCommand;
import net.playlegend.questsystem.commands.QuestCommand;
import net.playlegend.questsystem.commands.admin.QuestAdminCommand;
import net.playlegend.questsystem.commands.handler.CommandHandler;
import net.playlegend.questsystem.database.*;
import net.playlegend.questsystem.listener.QuestPlayerConnectionListener;
import net.playlegend.questsystem.listener.QuestStepListener;
import net.playlegend.questsystem.npc.NPCManager;
import net.playlegend.questsystem.player.PlayerHandler;
import net.playlegend.questsystem.quest.QuestManager;
import net.playlegend.questsystem.quest.QuestSignManager;
import net.playlegend.questsystem.translation.DefaultEnglishMessages;
import net.playlegend.questsystem.translation.DefaultGermanMessages;
import net.playlegend.questsystem.translation.LanguageHandler;
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
    private QuestSignManager questSign;
    private NPCManager npcManager;


    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.languageHandler = new LanguageHandler();
        this.playerHandler = new PlayerHandler();
        this.commandHandler = new CommandHandler();
        this.questManager = new QuestManager();
        this.questSign = new QuestSignManager(this);
        if (getServer().getPluginManager().isPluginEnabled("NPCSystem")) {
            this.npcManager = new NPCManager(this);
        }
        QuestDatabase.getInstance();
        PlayerQuestDatabase.getInstance();
        PlayerInfoDatabase.getInstance();
        NPCDatabase.getInstance();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new QuestPlayerConnectionListener(this, playerHandler), this);
        pm.registerEvents(new QuestStepListener(playerHandler), this);
        pm.registerEvents(questSign, this);
        this.commandHandler.addCommands(new QuestAdminCommand(), new LanguageCommand(), new QuestCommand());

        new DefaultGermanMessages();
        new DefaultEnglishMessages();
    }

    @Override
    public void onDisable() {
        this.playerHandler.saveAllPlayers();
        DatabaseHandler.getInstance().closeConnection();
    }

}
