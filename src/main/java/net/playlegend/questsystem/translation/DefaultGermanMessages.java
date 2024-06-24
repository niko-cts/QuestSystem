package net.playlegend.questsystem.translation;

import java.util.Locale;

import static net.playlegend.questsystem.translation.TranslationKeys.*;

/**
 * Holds all default german messages.
 *
 * @author Niko
 */
public class DefaultGermanMessages extends AbstractDefaultMessages {

    public DefaultGermanMessages() {
        super(Locale.GERMAN);

        add(QUESTS_NPC_FOUNDBOOK, "&6Du hast/n&l${name} &6gefunden!/n/n&0Questbeschreibung:/n${description}");
        add(QUESTS_NPC_FOUNDBOOK_ALREADY, "&6Du hast diese Quest bereits gefunden!");

        // Scoreboard
        add(QUESTS_SCOREBOARD_DISPLAYNAME, "&6QuestSystem");
        add(QUESTS_SCOREBOARD_ACTIVE_QUESTNAME, "&aAktive Quest");
        add(QUESTS_SCOREBOARD_ACTIVE_CURRENT_STEP, "&aAktuelle Aufgabe");
        add(QUESTS_SCOREBOARD_ACTIVE_FINISH_AT, "&eQuest endet am");
        add(QUESTS_SCOREBOARD_NO_ACTIVE_1, "&7Keine aktive Quest");
        add(QUESTS_SCOREBOARD_NO_ACTIVE_2, "&7/quest - Quest starten");
        add(QUESTS_SCOREBOARD_COINS, "&eCoins");


        // sign
        add(QUESTS_SIGN_NO_ACTIVE_LINE_1, "Keine aktive Quest");
        add(QUESTS_SIGN_NO_ACTIVE_LINE_2, "Schreibe /quest");
        add(QUESTS_SIGN_ACTIVE_LINE_1, "Quest &6${name}");
        add(QUESTS_SIGN_ACTIVE_LINE_2, "Nächste Aufgabe &e${task}");

        // builder
        add(QUESTS_BUILDER_NOT_VALID_NUMBER, "&cDu hast eine ungültige Zahl eingegeben.");
        add(QUESTS_BUILDER_NOT_VALID_UUID, "&cDu hast eine ungültige UUID eingegeben.");
        add(QUESTS_BUILDER_NOT_VALID_MATERIAL, "&cDu hast ein ungültiges Material eingegeben.");
        add(QUESTS_BUILDER_NOT_VALID_ENTITYTYPE, "&cDu hast einen ungültigen Entitätstyp eingegeben.");
        add(QUESTS_BUILDER_MODIFY_INTEGER, "&7Gib eine Zahl für: ${input} ein.");
        add(QUESTS_BUILDER_MODIFY_ITEM_INSERTION, "&7Lege ein Item neben dieses;&7und schließe das Inventar.");
        add(QUESTS_BUILDER_MODIFY_REMOVE, "&7Step-id ist: ${id};&7Klicken, um den Eintrag zu &centfernen&7.");
        add(QUESTS_BUILDER_MODIFY_ADD, "&7Klicken, um einen Eintrag zu &ahinzufügen&7.");
        add(QUESTS_BUILDER_MODIFY_QUESTNAME, "&7Gib einen Namen ein, der der Questname sein wird.");
        add(QUESTS_BUILDER_NAME_LORE, "&7Das wird der Name der Quest;&7Klicken, um ihn zu ändern.");
        add(QUESTS_BUILDER_DESCRIPTION_NAME, "&eQuestbeschreibung");
        add(QUESTS_BUILDER_DESCRIPTION_LORE, "&7Klicken, um die Questbeschreibung zu ändern.");
        add(QUESTS_BUILDER_DESCRIPTION_CLICK, "&7Klicken und Questbeschreibung eingeben. Ein ';' ist ein line-break.");
        add(QUESTS_BUILDER_REWARDS_LORE, "&7Linksklick, um eine neue Questbelohnung hinzuzufügen; ;&7Shift-Klick, um die aktuellen Einträge zu sehen.");
        add(QUESTS_BUILDER_STEPS_LORE, "&7Linksklick, um einen neuen Questschritt hinzuzufügen; ;&7Shift-Klick, um die aktuellen Einträge zu sehen.");
        add(QUESTS_BUILDER_TIMER_OFFLINE_NAME, "&eCountdown läuft offline: ${active}");
        add(QUESTS_BUILDER_TIMER_OFFLINE_LORE, "&7Klicken, um umzuschalten;&7ob der Countdown offline weiterläuft.");
        add(QUESTS_BUILDER_PUBLIC_NAME, "&eQuest ist öffentlich: ${active}");
        add(QUESTS_BUILDER_PUBLIC_LORE, "&7Klicken, um umzuschalten;&7ob die Quest öffentlich sichtbar ist;&7und nicht vom Spieler gefunden;&7werden muss.");
        add(QUESTS_BUILDER_TIMER_NAME, "&eQuest-Countdown");
        add(QUESTS_BUILDER_TIMER_LORE, "&7Die Sekunden, die der Spieler hat;&7um die Quest abzuschließen;&7Aktuelle Dauer: &e${duration}");
        add(QUESTS_BUILDER_CREATE_NAME, "&aQuest erstellen");
        add(QUESTS_BUILDER_CREATE_LORE, "&7Klicken, um die Quest zu erstellen.;&7Um die Quest zu erstellen,;&7musst du folgendes gesetzt habeN:;&7- Einen Namen;&7- Eine Beschreibung;&7- Mindestens eine Aufgabe");
        add(QUESTS_BUILDER_CREATE_NAME_ALREADY_EXISTS, "&cDieser Questname existent bereits.");

        add(QUESTS_BUILDER_SUCCESSFUL_CREATED, "&aQuest ${name} wurde erstellt.");
        add(QUESTS_BUILDER_SUCCESSFUL_UPDATED, "&aQuest ${name} wurde aktualisiert.");

        add(QUESTS_BUILDER_STEPS_CREATION_ORDER_NAME, "&7Aktuelle Reihenfolge: &e${order}");
        add(QUESTS_BUILDER_STEPS_CREATION_ORDER_LORE, "&7Ändere die Reihenfolge dieser Aufgabe;&7Alle Aufgaben mit derselben Reihenfolge;&7können gleichzeitig erledigt werden;&7Klicken, um zu ändern.");
        add(QUESTS_BUILDER_STEPS_CREATION_AMOUNT_NAME, "&7Aktuelle Menge: &e${amount}");
        add(QUESTS_BUILDER_STEPS_CREATION_AMOUNT_LORE, "&7Die Menge zum Abschließen der Aufgabe;&7Das ist z.B.:;&7'Baue <anzahl> von Blöcken ab';&7Klicken, um zu ändern.");
        add(QUESTS_BUILDER_STEPS_CREATION_PARAMETER_NAME, "&eAktueller Parameter: &6${parameter}");
        add(QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, "&7Gib den Parameter des Tasks ein;&7Das kommt auf den Tasktyp an;&7Z.B. erfordert Mining einen Materialnamen;&7Dieser Schritt erfordert: &e${input};&7Klicken, um Parameter einzufügen.");
        add(QUESTS_BUILDER_STEPS_CREATION_ACCEPT, "&aKlicken, um hinzuzufügen.");

        // events
        add(QUESTS_EVENT_TIMER_EXPIRED, "&e${name} &7ist &cabgelaufen&7!");
        add(QUESTS_EVENT_FINISHED, "&aDu hast &e${name}&a abgeschlossen!");
        add(QUESTS_EVENT_SWITCHED, "&7Du hast dein &aaktive Quest &7zu &e${name} &7gewechselt.");
        add(QUESTS_EVENT_STARTED, "&7Die Quest &e${name}&7 wurde gestartet!");
        add(QUESTS_EVENT_COUNTDOWN, "&7Du hast noch &e${duration} &7für die Quest &e${name}");
        add(QUESTS_EVENT_FOUND_NEW, "&7Du hast eine &eneue Quest &7gefunden: &e${name}");
        add(QUESTS_EVENT_JOINED_HAS_ACTIVE, "&7Du hast eine &aaktive Quest '&e${name}' &7laufen! Verbleibende Zeit: &e${duration}");
        add(QUESTS_EVENT_JOINED_NO_ACTIVE, "&7Du hast keine aktive Quest laufen. Beginne eine neue Quest mit &e/quest");

        add(QUESTS_DISPLAY_DAYS, "Tage");
        add(QUESTS_DISPLAY_HOURS, "Stunden");
        add(QUESTS_DISPLAY_MINUTES, "Minuten");
        add(QUESTS_DISPLAY_SECONDS, "Sekunden");

// System
        add(SYSTEM_DISABLED_COMMAND, "&cDieser Befehl wurde deaktiviert.");
        add(SYSTEM_NO_PERMISSION, "&cDu hast keine ausreichende Berechtigung für diesen Befehl");

// Commands
        add(SYSTEM_COMMAND_USAGE, "&7Führe den Befehl mit &e/${command} aus");
        add(QUESTS_COMMAND_NOT_FOUND, "&7Diese Quest konnte &cnicht &7gefunden werden.");
        add(QUESTS_COMMAND_ADMIN_USAGE, "questadmin create/list/remove/sign");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST_USAGE, "questadmin quest create/delete/list");
        add(QUESTS_COMMAND_ADMIN_QUEST_CREATE_USAGE, "questadmin create (description <description>)");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_USAGE, "questadmin delete <name>");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM, "&7Möchtest du diese Quest wirklich &clöschen&7? (Hier klicken, um zu bestätigen)");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_SUCCESSFUL, "&7Quest wurde erfolgreich gelöscht.");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST_NO_QUESTS, "&7Es sind keine Quests aufgelistet.");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST, "&7Alle Quests: &e${quest}");

        add(QUESTS_COMMAND_ADMIN_SIGN_USAGE, "/questadmin sign - Füge ein Quetschild hinzu");
        add(QUESTS_COMMAND_ADMIN_SIGN_NOT_SIGN, "&7Du schaust auf &ckein Schild");
        add(QUESTS_COMMAND_ADMIN_SIGN_ADDED, "&7Ein neues Schild wurde &ahinzugefügt");

        add(QUESTS_COMMAND_LANGUAGE_USAGE, "language <language>");
        add(QUESTS_COMMAND_LANGUAGE_UPDATED, "&aDeine Sprache wurde geändert.");
        add(QUESTS_COMMAND_LANGUAGE_ILLEGAL, "&7Diese Sprache ist &cnicht &7verfügbar");
        add(QUESTS_COMMAND_QUEST_USAGE, "quest");
        add(QUESTS_COMMAND_QUEST_CANCEL_USAGE, "quest cancel");
        add(QUESTS_COMMAND_QUEST_CANCEL_SUCCESS, "&7Deine aktive Quest wurde &aabgebrochen&7. Du kannst sie über die /quest GUI neu starten");
        add(QUESTS_COMMAND_QUEST_CANCEL_NOACTIVE, "&7Es gibt keine aktive Quest, die du abbrechen kannst.");
        add(QUESTS_COMMAND_QUEST_INFO_USAGE, "quest info - Informationen über deine aktive Quest");
        add(QUESTS_COMMAND_QUEST_INFO_DISPLAY, "&7Die Quest &e${name} &7läuft noch &e${duration}&7. Deine aktuelle Aufgabe ist: &e${todo}");
        add(QUESTS_COMMAND_QUEST_INFO_NOACTIVE, "&7Es gibt &ckeine aktive Quest&7. &aStarte &7eine mit der GUI über /quest");

        add(QUESTS_COMMAND_QUEST_FIND_USAGE, "quest find <name> - Entdecke die Quest");
        add(QUESTS_COMMAND_QUEST_FIND_ALREADY_FOUND, "&cDu hast die Quest bereits gefunden");
        add(QUESTS_COMMAND_QUEST_FIND_ISPUBLIC, "&cDiese Quest ist öffentlich und kann nicht gefunden werden");

// GUI overview
        add(QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_NAME, "&7Keine aktive Quest");
        add(QUESTS_GUI_OVERVIEW_ACTIVE_NO_ACTIVE_LORE, "&7Starte eine Quest über;&7das Menü '&egefundene&7' oder '&eöffentliche Quests&7'");
        add(QUESTS_GUI_OVERVIEW_ACTIVE_NAME, "&a${name} &eläuft");
        add(QUESTS_GUI_OVERVIEW_ACTIVE_LORE, "&7${name} ist derzeit deine aktive Quest.;&7Klicken, um Details zu sehen");
        add(QUESTS_GUI_OVERVIEW_PUBLIC_NAME, "&eÖffentliche Quests");
        add(QUESTS_GUI_OVERVIEW_PUBLIC_LORE, "&7Klicken, um alle öffentlichen Quests zu sehen.;&7Diese sind temporäre Quests;&7die ablaufen können");
        add(QUESTS_GUI_OVERVIEW_FOUND_NAME, "&eGefundene Quests");
        add(QUESTS_GUI_OVERVIEW_FOUND_LORE, "&7Klicken, um alle gefundenen Quests zu sehen.;&7Du kannst Quests in der Welt finden");
        add(QUESTS_GUI_OVERVIEW_COMPLETED_NAME, "&eAbgeschlossene Quests");
        add(QUESTS_GUI_OVERVIEW_COMPLETED_LORE, "&7Klicken, um alle Quests zu sehen;&7die du bereits abgeschlossen hast.;&7Diese Quests können nicht erneut gemacht werden.");

// GUI active
        add(QUESTS_GUI_ACTIVE_OVERVIEW, "&7Die Quest läuft noch: &e${duration};&7Du hast &e${done} &7Aufgaben abgeschlossen;&7Du musst noch &e${todo} &7weitere Aufgaben erledigen");
        add(QUESTS_GUI_ACTIVE_LORE_STEP_COMPLETED, "&7&m${order}. ${task}");
        add(QUESTS_GUI_ACTIVE_LORE_STEP_TODO, "&e${order}. ${task}");
        add(QUESTS_GUI_ACTIVE_CANCEL_NAME, "&cQuest abbrechen");
        add(QUESTS_GUI_ACTIVE_CANCEL_LORE, "&7Klicken, um die Quest abzubrechen.;&7Alle Fortschritte gehen verloren!;&7Du wirst die Quest neu starten können.;&cDies kann nicht rückgängig gemacht werden");
        add(QUESTS_GUI_ACTIVE_TIME_LEFT_NAME, "&eVerbleibende Zeit: ${duration}");
        add(QUESTS_GUI_ACTIVE_STEPS_PREVIEW_NAME, "&eQuest Aufgaben");
        add(QUESTS_GUI_ACTIVE_STEPS_PREVIEW_LORE, "&7Klicken, um alle Aufgaben der Quest zu sehen");
        add(QUESTS_GUI_ACTIVE_STEPS_TITLE, "&6Aktive Quest Aufgaben");
        add(QUESTS_GUI_ACTIVE_STEPS_UNCOMPLETED, "&7Alle verbleibenden Aufgaben ->");
        add(QUESTS_GUI_ACTIVE_STEPS_COMPLETED, "&7Alle abgeschlossenen Aufgaben ->");

        add(QUESTS_GUI_NORMAL_STEPS_TITLE, "&6Quest Aufgaben");
        add(QUESTS_GUI_NORMAL_STEPS_INFO, "&7Alle Aufgaben zum Abschließen ->");

        add(QUESTS_GUI_FOUND_INFO, "&6Gefundene Quests");
        add(QUESTS_GUI_COMPLETED_INFO, "&6Abgeschlossene Quests");
        add(QUESTS_GUI_PUBLIC_INFO, "&eÖffentliche Quests");
        add(QUESTS_GUI_PUBLIC_LORE, " ;&7Diese Quest ist öffentlich.;&7Sie kann ablaufen.;&eKlicken, um Details zu sehen.");
        add(QUESTS_GUI_QUEST_DETAILS_LORE, " ;&7Um &e${time};&eKlicken, um Details zu sehen.");
        add(QUESTS_GUI_ACCEPT_START_NAME, "&eQuest starten");
        add(QUESTS_GUI_ACCEPT_START_LORE, "&7Klicken, um die Quest zu starten;&7Eine aktive Quest wird abgebrochen;&7Du kannst sie später neustarten");
        add(QUESTS_GUI_ACCEPT_TITLE, "&6Quest starten");
        add(QUESTS_GUI_QUEST_TIME_NAME, "&eAbgeschlossen am");
        add(QUESTS_GUI_QUEST_TIME_LORE, "&7Du hast diese Quest;&7abgeschlossen am &e${time}");
        add(QUESTS_GUI_QUEST_STEPS_NAME, "&eAlle Questaufgaben");
        add(QUESTS_GUI_QUEST_REWARD_NAME, "&eAlle Questbelohnungen");
        add(QUESTS_GUI_QUEST_DETAILS_ITEM_LORE, "&7Beende diese Quest in &e${duration};&7Es gibt &e${rewards} Belohnungen;&7und &e${tasks} Aufgaben.");


        add(QUESTS_GUI_ACCEPT_REWARD, " ;&7Klicken, um ein;&7detailliertes Belohnungsmenü zu öffnen");
        add(QUESTS_GUI_BACK, "&eZurück");
        add(QUESTS_GUI_REWARDS_PREVIEW_NAME, "&eQuest Belohnungen");
        add(QUESTS_GUI_REWARDS_PREVIEW_LORE, "&7Klicken, um die Belohnungen;&7für diese Quest zu öffnen");
        add(QUESTS_GUI_REWARDS_TITLE, "&6Quest Belohnungen");

// STEPS
        add(QUESTS_STEP_CRAFT_NORMAL_LINE, "&7Stelle &e${item} &a${maxamount} &7Mal her");
        add(QUESTS_STEP_CRAFT_ACTIVE_LINE, "&7Stelle &e${item} &7her - &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_CRAFT_NORMAL_LORE, "&7Du musst;&7dieses Item &e${maxamount} &7Mal herstellen.");
        add(QUESTS_STEP_CRAFT_ACTIVE_LORE, "&7Du musst;&7dieses Item noch &e${amount}&7/&e${maxamount} &7mal herstellen");
        add(QUESTS_STEP_MINE_NORMAL_LINE, "&7Baue &e${item} &a${maxamount} &7Mal ab");
        add(QUESTS_STEP_MINE_ACTIVE_LINE, "&7Baue &e${item} &7ab - &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_MINE_NORMAL_LORE, "&7Du musst;&7diesen Block &e${maxamount} &7Mal abbauen.");
        add(QUESTS_STEP_MINE_ACTIVE_LORE, "&7Du musst;&7diesen Block noch &e${amount}&7/&e${maxamount} &7mal abbauen");
        add(QUESTS_STEP_KILL_NORMAL_LINE, "&7Töte &e${entity} &a${maxamount} &7Mal");
        add(QUESTS_STEP_KILL_ACTIVE_LINE, "&7Töte &e${entity} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_KILL_NORMAL_LORE, "&7Töte &e${entity} &a${maxamount} &7Mal");
        add(QUESTS_STEP_KILL_ACTIVE_LORE, "&7Du musst;&7diese Entität noch &e${amount}&7/&e${maxamount} &7mal töten");
        add(QUESTS_STEP_NPC_NORMAL_LINE, "&7Sprich mit &e${name} &a${maxamount} &7Mal");
        add(QUESTS_STEP_NPC_ACTIVE_LINE, "&7Sprich mit &e${name} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_NPC_NORMAL_LORE, "&7Sprich mit &e${name} &a${maxamount} &7Mal;&7${name} ist bei &e${location}");
        add(QUESTS_STEP_NPC_ACTIVE_LORE, "&7Du musst;&7mit ${name} noch &e${amount}&7/&e${maxamount} &7mal sprechen;&7${name} ist bei &e${location}");

// REWARDS
        add(QUESTS_REWARD_COINS_PREVIEW, "&e${amount} Münzen");
        add(QUESTS_REWARD_COINS_NAME, "&eMünzen");
        add(QUESTS_REWARD_COINS_LORE, "&7Du erhältst &e${amount} Münzen;&7nach Abschluss der Quest");
        add(QUESTS_REWARD_LVL_PREVIEW, "&e${amount} Level");
        add(QUESTS_REWARD_LVL_NAME, "&eLevel");
        add(QUESTS_REWARD_LVL_LORE, "&7Du erhältst &e${amount} Level;&7nach Abschluss der Quest");
        add(QUESTS_REWARD_ITEM_PREVIEW, "1 Item");
        add(QUESTS_REWARD_ITEM_LORE, "&7Du erhältst diesen Item;&7nach Abschluss der Quest");

        save();
    }
}
