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

### Bonus Features
1. **Multilingual Support**
   - Support for multiple languages for quests and messages.

3. **Scoreboard**
   - Display quest information on a scoreboard.

4. **Multiple Server Instances**
   - Compatible with multiple server instances using the same database.

## Installation

1. **Download the Plugin**
   - Download the latest version of the plugin from the [GitHub repository](#).

2. **Install the Plugin**
   - Place the plugin JAR file into the `plugins` folder of your Minecraft server.

3. **Configure the Plugin**
   - Edit the configuration file (`config.yml`) to customize messages and settings.

4. **Database Setup**
   - Ensure your server has access to a relational database and configure the database settings in the configuration file.

5. **Start the Server**
   - Start or restart your Minecraft server to load the plugin.

## Commands

- `/quest find <quest_name>`: Accept a quest.
- `/quest`: Quest GUI
- `/quest cancel`: Cancel a quest.
- `/quest info`: View the current quest status.
- `/questadmin create/remove/list`: Check remaining time for the current quest.

## Permissions

- `command.quest.admin`: Access to all admin commands.

## Configuration

Customize the plugin settings and messages in the `config.yml` file. Example configuration:

```yaml
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

- **Code Style**: Adheres to Java conventions and Google Codestyle.
- **Performance**: Designed to handle a large number of players efficiently.
- **Asynchronous I/O**: Ensures smooth performance and user experience.
- **Exception Handling**: Robust handling of exceptions, especially for database operations.
- **Testing**: Includes unit tests with at least 15% code coverage. Mockito and MockBukkit were used.
