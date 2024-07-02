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
        add(QUESTS_SIGN_ACTIVE_LINE_2, "Nächste Aufgabe:");
        add(QUESTS_SIGN_ACTIVE_LINE_3, "${task}");
        add(QUESTS_SIGN_ACTIVE_LINE_4, "Endet am ${date}");

        // builder
        add(QUESTS_BUILDER_NOT_VALID_NUMBER, "&cDu hast eine ungültige Zahl eingegeben.");
        add(QUESTS_BUILDER_NOT_VALID_UUID, "&cDu hast eine ungültige UUID eingegeben.");
        add(QUESTS_BUILDER_NOT_VALID_MATERIAL, "&cDu hast ein ungültiges Material eingegeben.");
        add(QUESTS_BUILDER_NOT_VALID_ENTITYTYPE, "&cDu hast einen ungültigen Entitätstyp eingegeben.");
        add(QUESTS_BUILDER_MODIFY_INTEGER, "&7Gib eine Zahl für: ${input} ein.");
        add(QUESTS_BUILDER_MODIFY_ITEM_INSERTION, "&7Lege ein Item neben dieses;&7und schließe das Inventar.");
        add(QUESTS_BUILDER_MODIFY_STEPS_REMOVE, ";&7Task-id ist: ${id};&7Klicken, um die Aufgabe zu &centfernen&7.");
        add(QUESTS_BUILDER_MODIFY_REWARD_REMOVE, ";&7Klicken, um die Belohnung zu &centfernen&7.");
        add(QUESTS_BUILDER_MODIFY_ADD, "&7Klicken, um einen Eintrag zu &ahinzufügen&7.");
        add(QUESTS_BUILDER_MODIFY_QUESTNAME, "&7Gib einen Namen ein, der der Questname sein wird.");
        add(QUESTS_BUILDER_NAME_LORE, "&7Das wird der Name der Quest;&7Klicken, um ihn zu ändern.");
        add(QUESTS_BUILDER_DESCRIPTION_NAME, "&eQuestbeschreibung");
        add(QUESTS_BUILDER_DESCRIPTION_LORE, "&7Klicken, um die Questbeschreibung zu ändern.");
        add(QUESTS_BUILDER_DESCRIPTION_CLICK_TEXT, "&e[KLICK] &7Und Questbeschreibung eingeben (';' ist ein line-break). Die Beschreibung wird in der Lore des Quest-Items angezeigt.");
        add(QUESTS_BUILDER_DESCRIPTION_CLICK_HOVER, "&7Klicken und Questbeschreibung nach dem Befehl eingeben.");
        add(QUESTS_BUILDER_REWARDS_LORE, ";&7Linksklick, um eine neue Questbelohnung hinzuzufügen; ;&7Shift-Klick, um die aktuellen Einträge zu sehen.");
        add(QUESTS_BUILDER_STEPS_LORE, ";&7Linksklick, um einen neuen Questschritt hinzuzufügen; ;&7Shift-Klick, um die aktuellen Einträge zu sehen.");
        add(QUESTS_BUILDER_TIMER_OFFLINE_NAME, "&eCountdown läuft offline: ${active}");
        add(QUESTS_BUILDER_TIMER_OFFLINE_LORE, "&7Klicken, um umzuschalten;&7ob der Countdown offline weiterläuft.");
        add(QUESTS_BUILDER_PUBLIC_NAME, "&eQuest ist öffentlich: ${active}");
        add(QUESTS_BUILDER_PUBLIC_LORE, "&7Klicken, um umzuschalten;&7ob die Quest öffentlich sichtbar ist;&7und nicht vom Spieler gefunden;&7werden muss.");
        add(QUESTS_BUILDER_TIMER_NAME, "&eQuest-Countdown");
        add(QUESTS_BUILDER_TIMER_LORE, "&7Die Sekunden, die der Spieler hat;&7um die Quest abzuschließen;&7Aktuelle Dauer: &e${duration}");
        add(QUESTS_BUILDER_CREATE_NAME, "&aQuest erstellen");
        add(QUESTS_BUILDER_CREATE_LORE, "&7Klicken, um die Quest zu erstellen.;&7Um die Quest zu erstellen,;&7musst du folgendes gesetzt habeN:;&7- Einen Namen;&7- Eine Beschreibung;&7- Mindestens eine Aufgabe");
        add(QUESTS_BUILDER_CREATE_NAME_ALREADY_EXISTS, "&cDieser Questname existent bereits.");

        add(QUESTS_BUILDER_SUCCESSFUL_CREATED, "&aQuest &e${name} &awurde erstellt.");
        add(QUESTS_BUILDER_SUCCESSFUL_CREATED_ERROR, "&e${name} &ckonnte nicht erstellt werden! &7Schaue in der Konsole nach.");
        add(QUESTS_BUILDER_SUCCESSFUL_UPDATED, "&aQuest &e${name} &awurde aktualisiert.");
        add(QUESTS_BUILDER_SUCCESSFUL_UPDATED_ERROR, "&e${name} &ckonnte nicht aktualisiert werden! &7Schaue in der Konsole nach.");

        add(QUESTS_BUILDER_STEPS_CREATION_ORDER_NAME, "&7Aktuelle Reihenfolge: &e${order}");
        add(QUESTS_BUILDER_STEPS_CREATION_ORDER_LORE, "&7Ändere die Reihenfolge dieser Aufgabe;&7Alle Aufgaben mit derselben Reihenfolge;&7können gleichzeitig erledigt werden;&7Klicken, um zu ändern.");
        add(QUESTS_BUILDER_STEPS_CREATION_AMOUNT_NAME, "&7Aktuelle Menge: &e${amount}");
        add(QUESTS_BUILDER_STEPS_CREATION_AMOUNT_LORE, "&7Die Menge zum Abschließen der Aufgabe;&7Das ist z.B.:;&7'Baue <anzahl> von Blöcken ab';&7Klicken, um zu ändern.");
        add(QUESTS_BUILDER_STEPS_CREATION_PARAMETER_NAME, "&eAktueller Parameter: &6${parameter}");
        add(QUESTS_BUILDER_STEPS_CREATION_PARAMETER_LORE, "&7Gib den Parameter des Tasks ein;&7Das kommt auf den Tasktyp an;&7Z.B. erfordert Mining einen Materialnamen;&7Dieser Schritt erfordert: &e${input};&7Klicken, um Parameter einzufügen.");
        add(QUESTS_BUILDER_STEPS_CREATION_ACCEPT_LORE, " ;&aKlicken, um hinzuzufügen.");
        add(QUESTS_BUILDER_STEPS_CREATION_ERROR, "&cEs gab einen Fehler beim Erstellen des Aufgaben-Items! &7Fehler ist: &e${message}");

        // events
        add(QUESTS_EVENT_TIMER_EXPIRED, "&e${name} &7ist &cabgelaufen&7! Starte sie neu über /quest");
        add(QUESTS_EVENT_FINISHED, "&aDu hast &e${name}&a abgeschlossen!");
        add(QUESTS_EVENT_STEP_FINISHED, "&7Du hast die Aufgabe '&e${task}&7' geschafft");
        add(QUESTS_EVENT_SWITCHED, "&7Du hast dein &aaktive Quest &7zu &e${name} &7gewechselt.");
        add(QUESTS_EVENT_STARTED, "&7Die Quest &e${name}&7 wurde gestartet!");
        add(QUESTS_EVENT_COUNTDOWN, "&7Du hast noch &e${duration} &7für die Quest &e${name}");
        add(QUESTS_EVENT_FOUND_NEW, "&7Du hast die Quest '&e${name}&7' gefunden! Starte sie über /quest");
        add(QUESTS_EVENT_CLICK_TO_OPEN_HOVER, "&7Klicken, um das Questmenü zu öffnen");
        add(QUESTS_EVENT_JOINED_HAS_ACTIVE, "&7Du hast eine &aaktive Quest '&e${name}' &7laufen! Verbleibende Zeit: &e${duration}");
        add(QUESTS_EVENT_JOINED_NO_ACTIVE_TEXT, "&7Du hast keine aktive Quest laufen. Beginne eine neue Quest mit &e/quest");
        add(QUESTS_EVENT_JOINED_NO_ACTIVE_HOVER, "&7Klicken, um dein Questmenü zu öffnen");

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
        add(QUESTS_COMMAND_ADMIN_USAGE, "questadmin create/list/remove/sign/npc");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST_USAGE, "questadmin list (<questname>) - Zeigt alle Quests oder öffnet einen Adminmodus-GUI für eine spezifische Quest");
        add(QUESTS_COMMAND_ADMIN_QUEST_CREATE_USAGE, "questadmin create (description <description>) - Start a creation of a quest. (Write a description for the current creation)");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_USAGE, "questadmin delete <name> - Delete a Quest");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM_TEXT, "&7Möchtest du diese Quest wirklich &clöschen&7? (Hier klicken, um zu bestätigen)");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_CONFIRM_HOVER, "&7Klicken, um Quest &cpermanent&7 zu löschen!");
        add(QUESTS_COMMAND_ADMIN_QUEST_DELETE_SUCCESSFUL, "&7Quest wurde erfolgreich gelöscht.");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST_NO_QUESTS, "&7Es sind keine Quests aufgelistet.");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST, "&7Liste aller Questnamen - Klicken, um GUI zu öffnen: &e${quest}");
        add(QUESTS_COMMAND_ADMIN_QUEST_LIST_HOVER, "&7Klicken, um Quest GUI zu öffnen");

        add(QUESTS_COMMAND_ADMIN_SIGN_USAGE, "questadmin sign - Füge ein Questschild hinzu");
        add(QUESTS_COMMAND_ADMIN_SIGN_NOT_SIGN, "&7Du schaust auf &ckein Schild");
        add(QUESTS_COMMAND_ADMIN_SIGN_ADDED, "&7Ein neues Schild wurde &ahinzugefügt");

        add(QUESTS_COMMAND_ADMIN_NPC_USAGE, "questadmin npc find/task/list - Erstelle/Lösche einen NPC, der zum finden einer Quest oder einer Questaufgabe zuständig ist");

        add(QUESTS_COMMAND_ADMIN_NPC_LIST_USAGE, "questadmin npc list - Listet alle aktuellen NPC");
        add(QUESTS_COMMAND_ADMIN_NPC_LIST_FIND, "&7Alle Finding-NPC:");
        add(QUESTS_COMMAND_ADMIN_NPC_LIST_FIND_ELEMENT, "&7- &c[&7Finde Quest &e${questname} &7durch ${name} &7bei ${location}&c]");
        add(QUESTS_COMMAND_ADMIN_NPC_LIST_TASK, "&7Alle Aufgaben-NPC:");
        add(QUESTS_COMMAND_ADMIN_NPC_LIST_TASK_ELEMENT, "&7- &c[&e${name} &7mit uuid '${uuid}' soll in &b(&7${queststeps}&b) &7angesprochen werden. Position ist ${location}&c]");
        add(QUESTS_COMMAND_ADMIN_NPC_LIST_TASK_ELEMENT_STEP, "&7Quest: ${questname} &7in Aufgabe-ID: &e${stepid}&7");

        add(QUESTS_COMMAND_ADMIN_NPC_FIND_SUCCESSFUL, "&aSetup was erfolgreich. Der NPC spawnt an deiner Position");
        add(QUESTS_COMMAND_ADMIN_NPC_FIND_DELETED, "&7Find-NPC wurde gelöscht.");
        add(QUESTS_COMMAND_ADMIN_NPC_FIND_USAGE, "questadmin npc find <questname> <npcname> - Erstelle einen NPC, der zum finden einer Quest da ist");
        add(QUESTS_COMMAND_ADMIN_NPC_TASK_USAGE, "questadmin npc task <npcname> <uuid> <language> <text> - Erstelle einen NPC, der zum abschließend einer Questaufgabe da ist");
        add(QUESTS_COMMAND_ADMIN_NPC_TASK_DELETED, "&7Task-NPC wurde gelöscht.");
        add(QUESTS_COMMAND_ADMIN_NPC_TASK_SUCCESSFUL, "&aTask-NPC wurde erstellt/gelöscht. &7Ein NPC wird an dieser Position spawnen. Spieler müssen mit diesem NPC interagieren, um die Aufgabe 'talk' abzuschließen!");

        add(QUESTS_COMMAND_LANGUAGE_USAGE, "language <language>");
        add(QUESTS_COMMAND_LANGUAGE_UPDATED, "&aDeine Sprache wurde geändert.");
        add(QUESTS_COMMAND_LANGUAGE_ILLEGAL, "&7Diese Sprache ist &cnicht &7verfügbar. Verfügbar ist: ${languages}");
        add(QUESTS_COMMAND_QUEST_USAGE, "quest");
        add(QUESTS_COMMAND_QUEST_CANCEL_USAGE, "quest cancel");
        add(QUESTS_COMMAND_QUEST_CANCEL_SUCCESS, "&7Deine aktive Quest wurde &aabgebrochen&7. Klicke hier, um sie zu öffnen");
        add(QUESTS_COMMAND_QUEST_CANCEL_NOACTIVE, "&7Es gibt &ckeine &7aktive Quest, die du abbrechen kannst.");
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
        add(QUESTS_GUI_ACTIVE_STEPS_PREVIEW_LORE, ";&7Klicken, um alle Aufgaben der Quest zu sehen");
        add(QUESTS_GUI_ACTIVE_STEPS_TITLE, "&6Aktive Quest Aufgaben");
        add(QUESTS_GUI_ACTIVE_STEPS_UNCOMPLETED, "&7Alle verbleibenden Aufgaben ->");
        add(QUESTS_GUI_ACTIVE_STEPS_COMPLETED, "&7Alle abgeschlossenen Aufgaben ->");

        add(QUESTS_GUI_NORMAL_STEPS_TITLE, "&6Quest Aufgaben");
        add(QUESTS_GUI_NORMAL_STEPS_INFO, "&7Alle Aufgaben zum Abschließen ->");

        add(QUESTS_GUI_FOUND_INFO, "&6Gefundene Quests");
        add(QUESTS_GUI_COMPLETED_INFO, "&6Abgeschlossene Quests");
        add(QUESTS_GUI_PUBLIC_INFO_NAME, "&eÖffentliche Quests");
        add(QUESTS_GUI_PUBLIC_INFO_LORE, "&7Hier ist jede öffentliche Quest;&7die du noch nicht absolviert hast.");
        add(QUESTS_GUI_PUBLIC_LORE, " ;&7Diese Quest ist öffentlich.;&7Sie kann ablaufen.;&eKlicken, um Details zu sehen.");
        add(QUESTS_GUI_QUEST_DETAILS_FOUND_LORE, " ;&7Du hast diese Quest am &e${time} &7gefunden;&eKlicken, um Details zu sehen.");
        add(QUESTS_GUI_QUEST_DETAILS_COMPLETED_LORE, " ;&7Du hast diese Quest am &e${time} &7absolviert;&eKlicken, um Details zu sehen.");
        add(QUESTS_GUI_ACCEPT_START_NAME, "&eQuest starten");
        add(QUESTS_GUI_ACCEPT_START_LORE, "&7Klicken, um die Quest zu starten;&7Eine aktive Quest wird abgebrochen;&7Du kannst sie später neustarten");
        add(QUESTS_GUI_ACCEPT_TITLE, "&6Quest starten");
        add(QUESTS_GUI_QUEST_TIME_NAME, "&eAbgeschlossen am");
        add(QUESTS_GUI_QUEST_TIME_LORE, "&7Du hast diese Quest;&7abgeschlossen am &e${time}");
        add(QUESTS_GUI_QUEST_STEPS_NAME, "&eAlle Questaufgaben");
        add(QUESTS_GUI_QUEST_REWARD_NAME_HAS, "&eAlle Questbelohnungen");
        add(QUESTS_GUI_QUEST_REWARD_NAME_NONE, "&eKeine Questbelohnungen");
        add(QUESTS_GUI_QUEST_DETAILS_ITEM_LORE, ";&7Beende diese Quest in &e${duration};&7Es gibt &e${rewards} Belohnung(en);&7und &e${tasks} Aufgab(en).");
        add(QUESTS_GUI_ACCEPT_REWARD, " ;&7Klicken, um ein;&7detailliertes Belohnungsmenü zu öffnen");
        add(QUESTS_GUI_QUEST_ADMIN_MODIFY_NAME, "&eQuest verändern");
        add(QUESTS_GUI_QUEST_ADMIN_MODIFY_LORE, "&7Klicken, um Quest;&7zu verändern");
        add(QUESTS_GUI_QUEST_ADMIN_DELETE_NAME, "&cQuest löschen");
        add(QUESTS_GUI_QUEST_ADMIN_DELETE_LORE, "&7Klicken, um Quest;&7zu &clöschen&7!");

        add(QUESTS_GUI_BACK, "&eZurück");
        add(QUESTS_GUI_REWARDS_PREVIEW_NAME, "&eQuest Belohnungen");
        add(QUESTS_GUI_REWARDS_PREVIEW_LORE, ";&7Klicken, um die Belohnungen;&7für diese Quest zu öffnen");
        add(QUESTS_GUI_REWARDS_TITLE, "&6Quest Belohnungen");

// STEPS
        add(QUESTS_STEP_CRAFT_NORMAL_LINE, "&e${order}&7. &7Stelle &e${item} &a${maxamount} &7Mal her");
        add(QUESTS_STEP_CRAFT_ACTIVE_LINE, "&e${order}&7. &7Stelle &e${item} &7her - &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_CRAFT_NORMAL_LORE, "&7Du musst;&7dieses Item &e${maxamount} &7Mal herstellen.");
        add(QUESTS_STEP_CRAFT_ACTIVE_LORE, "&7Du musst;&7dieses Item noch &e${amount}&7/&e${maxamount} &7mal herstellen");
        add(QUESTS_STEP_MINE_NORMAL_LINE, "&e${order}&7. &7Baue &e${item} &a${maxamount} &7Mal ab");
        add(QUESTS_STEP_MINE_ACTIVE_LINE, "&e${order}&7. &7Baue &e${item} &7ab - &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_MINE_ALREADY_MINED, "&cDu hast an dieser Stelle bereits den Block abgebaut!");
        add(QUESTS_STEP_MINE_NORMAL_LORE, "&7Du musst;&7diesen Block &e${maxamount} &7Mal abbauen.");
        add(QUESTS_STEP_MINE_ACTIVE_LORE, "&7Du musst;&7diesen Block noch &e${amount}&7/&e${maxamount} &7mal abbauen");
        add(QUESTS_STEP_KILL_NORMAL_LINE, "&e${order}&7. &7Töte &e${entity} &a${maxamount} &7Mal");
        add(QUESTS_STEP_KILL_ACTIVE_LINE, "&e${order}&7. &7Töte &e${entity} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_KILL_NORMAL_LORE, "&7Töte &e${entity} &a${maxamount} &7Mal");
        add(QUESTS_STEP_KILL_ACTIVE_LORE, "&7Du musst;&7diese Entität noch &e${amount}&7/&e${maxamount} &7mal töten");
        add(QUESTS_STEP_NPC_NORMAL_LINE, "&e${order}&7. &7Sprich mit &e${name} &a${maxamount} &7Mal");
        add(QUESTS_STEP_NPC_ACTIVE_LINE, "&e${order}&7. &7Sprich mit &e${name} &7- &a${amount}&7/&a${maxamount}");
        add(QUESTS_STEP_NPC_NORMAL_NAME, "&eSprich mit ${name}");
        add(QUESTS_STEP_NPC_NORMAL_LORE, "&7Sprich mit &e${name} &a${maxamount} &7Mal;&7${name} &7ist bei &e${location}");
        add(QUESTS_STEP_NPC_ACTIVE_LORE, "&7Du musst;&7mit ${name} noch &e${amount}&7/&e${maxamount} &7mal sprechen;&7${name} &7ist bei &e${location}");

// REWARDS
        add(QUESTS_REWARD_COINS_PREVIEW, "&e${amount} Münzen");
        add(QUESTS_REWARD_COINS_MESSAGE, "&7Du erhälst &e${amount} Münzen");
        add(QUESTS_REWARD_COINS_NAME, "&eMünzen");
        add(QUESTS_REWARD_COINS_LORE, "&7Du erhältst &e${amount} Münzen;&7nach Abschluss der Quest");
        add(QUESTS_REWARD_LVL_PREVIEW, "&e${amount} Level");
        add(QUESTS_REWARD_LVL_NAME, "&eLevel");
        add(QUESTS_REWARD_LVL_LORE, "&7Du erhältst &e${amount} Level;&7nach Abschluss der Quest");
        add(QUESTS_REWARD_LVL_MESSAGE, "&7Du erhälst &e${amount} Level");
        add(QUESTS_REWARD_ITEM_PREVIEW, "1 Item");
        add(QUESTS_REWARD_ITEM_LORE, "&7Du erhältst diesen Item;&7nach Abschluss der Quest");
        add(QUESTS_REWARD_ITEM_MESSAGE, "&7Du erhälst das Item: &e${name}");

        save();
    }
}
