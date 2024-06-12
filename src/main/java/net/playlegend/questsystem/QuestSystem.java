package net.playlegend.questsystem;

import lombok.Getter;
import net.playlegend.questsystem.database.DatabaseHandler;
import net.playlegend.questsystem.quest.QuestManager;
import net.playlegend.questsystem.translation.DefaultGermanMessages;
import net.playlegend.questsystem.translation.LanguageHandler;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class QuestSystem extends JavaPlugin {

    @Getter
    private static QuestSystem instance;
    private LanguageHandler languageHandler;
    private QuestManager questManager;


    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.languageHandler = new LanguageHandler();
        this.questManager = new QuestManager();

        new DefaultGermanMessages();
    }

    @Override
    public void onDisable() {
        DatabaseHandler.getInstance().closeConnection();
    }

}
