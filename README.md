# AutoIgnition

**AutoIgnition** is a **Hytale plugin** that automates benches. It automatically starts processing benches (like furnaces and campfires) when items are added. Also, it transfers items to and from nearby chests.

## Features

- **Auto-Start:** No longer need manually pressing the "Turn on" button. Processing starts immediately when fuel and input items are added.
- **Fuel Conservation:** Automatically extinguishes the bench when the recipe is complete or inputs run out, saving your precious resources.
- **Auto-Refuel:** Automatically takes fuel items from adjacent chests or output slot into the bench's fuel slot.
- **Auto-Output:** Automatically moves output items from the output slot into adjacent chests.

## 🛠 Installation

1. Navigate to your Hytale User Data folder:
    `C:\Users\YourUsername\AppData\Roaming\Hytale\UserData\`
2. Copy the plugin JAR into the `Mods/` folder.
3. Launch Hytale and enable the plugin in the world settings.

## ⚙️ Configuration

The plugin will generate a configuration file. You can modify this file.

**Location:** `C:\Users\YOUR_NAME\AppData\Roaming\Hytale\UserData\Saves\YOUR_WORLD\mods\Lutia_AutoIgnition\AutoIgnitionConfig.json`

### Configuration Options

| Option                  | Type    | Default                   | Description                                                                                 |
|:------------------------|:--------|:--------------------------|:--------------------------------------------------------------------------------------------|
| `UpdateIntervalMs`      | Long    | `1000`                    | The delay (in milliseconds) between items transfer. Higher values reduce server load.       |
| `ScannerIntervalMs`     | Long    | `10000`                   | The delay (in milliseconds) between containers scan. Higher values reduce server load.      |
| `EnableOutputTransfer`  | Boolean | `true`                    | If enabled, automatically moves finished products from benches to nearby chests.            |
| `EnableAutoRefuel`      | Boolean | `true`                    | If enabled, automatically pulls fuel from nearby chests into the bench.                     |
| `EnableInputTransfer`   | Boolean | `true`                    | When enabled, automatically pulls required ingredients from nearby containers.              |
| `EnableAutoFuelStart`   | Boolean | `true`                    | If enabled, automatically ignites the bench when both fuel and input materials are present. |
| `EnableAutoFuelStop`    | Boolean | `true`                    | If enabled, automatically extinguishes the bench when the recipe is finished to save fuel.  |
| `BlacklistedFuelItems`  | List    | `["Wood_Sallow_Trunk"]`   | List of Item IDs that should never be used as fuel by the automation.                       |
| `BlacklistedInputItems` | List    | `["Ingredient_Bar_Gold"]` | A list of item IDs that the mod is forbidden from pulling as ingredients.                   |

### Example Config

```json
{
  "UpdateIntervalMs": 1000,
  "ScannerIntervalMs": 10000,
  "EnableOutputTransfer": true,
  "EnableInputTransfer": true,
  "EnableAutoRefuel": true,
  "EnableAutoFuelStart": true,
  "EnableAutoFuelStop": true,
  "BlacklistedFuelItems": [
    "Wood_Sallow_Trunk"
  ],
  "BlacklistedInputItems": [
    "Ingredient_Bar_Gold"
  ]
}
```

# 💬 Feedback & Bug Reports

Your feedback is important for the growth of AutoIgnition! 
- **Found a bug?** Please open an issue on the [GitHub Repository](https://github.com/lukkoedm/AutoIgnition/issues) or tell us in the CurseForge comments.
- **Suggestions?** If you have ideas to improve the plugin, don't hesitate to reach out.

# 🚀 Future Roadmap

We are currently brainstorming and developing ideas to improve the plugin. Here is what we are considering for future updates:

- **Priority Storage:** Define which chests should be filled or emptied first.
- **Filter Logic:** Input/output filters (e.g., only move Iron Ore to this specific furnace).
- **More configuration options:** New config options to increase the scanning radius beyond the 6 immediate neighbors.
- **GUI Integration:** A simple in-game interface for more convenient configuration.

### **These features will not necessarily be implemented. They are potential ideas. It all depends on what users want!**
