# Quest System Plugin for Minecraft

Welcome to the Quest System plugin for Minecraft! This plugin provides an engaging quest and reward system designed to enhance the player experience. Below you will find a comprehensive guide to the features and usage of this plugin.

## Features

### Core Features
1. **Quest Creation and Management**
   - Create and manage quests directly in-game.
   - Each quest includes properties such as:
     - Name
     - Description
     - Rewards (e.g., quest coins, items)

2. **Quest Status**
   - Players can accept, complete, and cancel quests.
   - Quest progress is visible to the player.

3. **Rewards**
   - Players receive customizable rewards upon completing quests, including:
     - Quest coins
     - Items
     - Experience points

4. **Quest Information in Chat and Upon Server Entry**
   - Quest progress and status are displayed in the chat and upon server entry.

5. **Dynamic Quest Changes**
   - Immediate status updates when a quest is accepted or canceled.

6. **Configuration File**
   - Customizable messages and settings via a configuration file.

7. **Querying Current Quest and Remaining Time**
   - Command available to check the current quest and remaining time for completion.

8. **Display of Player Information on Signs**
   - Signs can display individual player information, such as current quest and progress.

9. **Database Integration**
   - All quest data, progress, and rewards are stored in a relational database.
     
10. **Multilingual Support**
   - Support for multiple languages for quests and messages.

11. **Scoreboard**
   - Display quest information on a scoreboard.

12. **Multiple Server Instances**
   - Compatible with multiple server instances using the same database.

13. **Multiple Tasks per Quest**
   - Different tasks to complete a quest.
   - Current task-types are: Speak to NPC, Mine a block, Kill an entity, Craft an item

14. **NPC in Quests**
   - NPC will spawn to find a certain Quests
   - NPC will spawn to complete the task 'Speak to NPC'
   

## Commands

- `/quest`: Quest GUI
- `/quest cancel`: Cancel a quest.
- `/quest info`: View the current quest status and remaining time.
- `/questadmin create/remove/list/sign/find/npc ...`: Admin commands to manage the quests, signs and NPC.


## Permissions

- `command.quest.admin.<subcommand>`: Access to all admin commands.

## Configuration

Customize the plugin settings and messages in the `config.yml` file. Example configuration:

```yaml
npc:
  vanish-delay: 3

database:
  host: "localhost"
  port: 3306
  username: "root"
  password: "password"
  database: "minecraft_quests"
```

Messages are under the plugin folder "messages".
`de` and `en` represent the Locale.getLanguage key.

## Development Notes

- **Requirements**: This plugin requires NikoAPI and for NPC support my NPCSystem plugin. 
- **Code Style**: Adheres to Java conventions and Google Codestyle.
- **Performance**: Designed to handle a large number of players efficiently.
- **Asynchronous I/O**: Ensures smooth performance and user experience.
- **Exception Handling**: Robust handling of exceptions, especially for database operations.
- **Testing**: Includes unit tests with at least 15% code coverage. Mockito and MockBukkit were used.
