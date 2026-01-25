# AutoIgnition

AutoIgnition is a Hytale plugin that automates benches. It automatically starts processing benches (like furnaces and campfires) when items are added. Also, it transfers items to and from nearby chests.

## Features

- **Auto-Start:** No longer need manually pressing the "Turn on" button. Processing starts immediately when fuel and input items are added.
- **Auto-Refuel:** Automatically takes fuel items from adjacent chests or output slot into the bench's fuel slot.
- **Auto-Output:** Automatically moves output items from the output slot into adjacent chests.

## Installation

1. Navigate to your Hytale User Data folder:
    `C:\Users\YourUsername\AppData\Roaming\Hytale\UserData\`
2. Copy the plugin JAR into the `Mods/` folder.
3. Launch Hytale and enable the plugin in the world settings.

## Configuration

The plugin will generate a configuration file. You can modify this file.

**Location:** `C:\Users\YOUR_NAME\AppData\Roaming\Hytale\UserData\Saves\YOUR_WORLD\mods\Lutia_AutoIgnition\AutoIgnitionConfig.json`

### Configuration Options

| Option | Type | Default | Description                                                                                |
| :--- | :--- | :--- |:-------------------------------------------------------------------------------------------|
| `UpdateIntervalMs` | Long | `500` | The delay (in milliseconds) between items transfer. Higher values reduce server load.      |
| `EnableNearbyChestsTransfer` | Boolean | `true` | Set to `false` to disable chest interactions and only keep the auto ignite feature.        |
| `FuelItems` | List | `["Ingredient_Charcoal"]` | A list of item IDs that the plugin is allowed to pull from chests as fuel.                 |

### Example Config

```json
{
  "UpdateIntervalMs": 500,
  "EnableNearbyChestsTransfer": true,
  "FuelItems": [
    "Ingredient_Charcoal",
    "Wood_Sallow_Trunk"
  ]
}