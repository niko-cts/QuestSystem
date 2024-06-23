package net.playlegend.questsystem.util;

import net.playlegend.questsystem.player.QuestPlayer;
import net.playlegend.questsystem.translation.Language;
import net.playlegend.questsystem.translation.TranslationMessageHolder;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A helper class used to create translatable sideboard-scoreboard easier.
 * @author Niko
 * @since 1.0.2
 */
public class APIScoreboard {

    public static final String START_COL = "&8» ";

    private final String objectiveName;
    private TranslationMessageHolder displayName;
    private final List<Object> scores;
    private final StringBuilder blankHolder;


    /**
     * Instantiates the scoreboard.
     * @param objectiveName String - the objective name which should be used.
     * @since 1.0.2
     */
    public APIScoreboard(String objectiveName) {
        this.objectiveName = objectiveName;
        this.scores = new ArrayList<>();
        this.blankHolder = new StringBuilder();
    }

    /**
     * Sets the display name of the scoreboard
     * @param displayName TranslationMessageHolder - new display name of the scoreboard.
     * @return APIScoreboard - instance of this scoreboard.
     * @since 1.0.2
     */
    public APIScoreboard setDisplayName(TranslationMessageHolder displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Adds multiple empty lines to the scoreboard.
     * @param amount int - the amount of empty lines to add.
     * @return APIScoreboard - instance of this scoreboard.
     * @since 1.0.2
     */
    public APIScoreboard addEmptyLines(int amount) {
        for (int i = 0; i < amount; i++) {
            addEmptyLine();
        }
        return this;
    }

    /**
     * Adds an empty line to the scoreboard.
     * @return APIScoreboard - instance of this scoreboard.
     * @since 1.0.2
     */
    public APIScoreboard addEmptyLine() {
        this.blankHolder.append(ChatColor.WHITE);
        return addLine(this.blankHolder.toString());
    }

    /**
     * Adds a new line to the scoreboard.
     * @param lines String[] - the lines to add (can include '&' as color codes).
     * @return APIScoreboard - instance of this scoreboard.
     * @since 1.0.2
     */
    public APIScoreboard addLine(String... lines) {
        for (String line : lines) {
            scores.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return this;
    }

    /**
     * Adds a new translatable line to the scoreboard.
     * @param lineKeys String[] - the lines to add (can include '&' as color codes).
     * @return APIScoreboard - instance of this scoreboard.
     * @since 1.0.2
     */
    public APIScoreboard addTranslatableLine(String... lineKeys) {
        for (String line : lineKeys) {
            scores.add(new TranslationMessageHolder(line));
        }
        return this;
    }

    /**
     * Adds a new translatable line to the scoreboard.
     * @param lineKey String - the translation key for the line.
     * @param placeholder String - the placeholder for the line.
     * @param replacement String - the replacement for the line.
     * @return APIScoreboard - instance of this scoreboard.
     * @since 1.0.2
     */
    public APIScoreboard addTranslatableLine(String lineKey, String placeholder, String replacement) {
        scores.add(new TranslationMessageHolder(lineKey, placeholder, replacement));
        return this;
    }

    /**
     * Adds a new translatable line to the scoreboard.
     * @param lineKey String - the translation key for the line.
     * @param placeholder List<String> - the placeholders for the line.
     * @param replacement List<String> - the replacements for the line.
     * @return APIScoreboard - instance of this scoreboard.
     * @since 1.0.2
     */
    public APIScoreboard addTranslatableLine(String lineKey, List<String> placeholder, List<Object> replacement) {
        scores.add(new TranslationMessageHolder(lineKey, placeholder, replacement));
        return this;
    }

    /**
     * Shows the player the current scoreboard and unregisters their old one.
     * @param player Player - player to show the scoreboard.
     * @since 1.0.2
     */
    public void showPlayer(QuestPlayer player) {
        Scoreboard scoreboard = player.getPlayer().getScoreboard();

        if (scoreboard.getObjective(DisplaySlot.SIDEBAR) != null) {
            Objects.requireNonNull(scoreboard.getObjective(DisplaySlot.SIDEBAR)).unregister();
        }

        Language language = player.getLanguage();
        Objective objective = scoreboard.registerNewObjective(objectiveName, Criteria.AIR, this.displayName.translate(language), RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 0; i < scores.size(); i++) {
            Object o = scores.get(i);
            objective.getScore(o instanceof TranslationMessageHolder holder ? holder.translate(language) : (String) o).setScore(scores.size() - i - 1);
        }
    }
}
