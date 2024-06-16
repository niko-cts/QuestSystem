package net.playlegend.questsystem;

import lombok.Getter;
import net.playlegend.questsystem.database.DatabaseHandler;
import net.playlegend.questsystem.listener.QuestPlayerConnectionListener;
import net.playlegend.questsystem.listener.QuestStepListener;
import net.playlegend.questsystem.player.PlayerHandler;
import net.playlegend.questsystem.quest.QuestManager;
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


    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.languageHandler = new LanguageHandler();
        this.questManager = new QuestManager();
        this.playerHandler = new PlayerHandler();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new QuestPlayerConnectionListener(this, playerHandler), this);
        pm.registerEvents(new QuestStepListener(playerHandler), this);

        new DefaultGermanMessages();
    }

    @Override
    public void onDisable() {
        this.playerHandler.saveAllPlayers();
        DatabaseHandler.getInstance().closeConnection();
    }

}
