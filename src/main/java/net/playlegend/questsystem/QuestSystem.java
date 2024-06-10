package net.playlegend.questsystem;

import lombok.Getter;
import net.playlegend.questsystem.translation.LanguageHandler;
import net.playlegend.questsystem.translation.defaults.DefaultGermanMessages;
import org.bukkit.plugin.java.JavaPlugin;

public final class QuestSystem extends JavaPlugin {

    @Getter
    private static QuestSystem instance;

    @Getter
    private LanguageHandler languageHandler;


    @Override
    public void onEnable() {
        instance = this;
        this.languageHandler = new LanguageHandler();

        new DefaultGermanMessages();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
