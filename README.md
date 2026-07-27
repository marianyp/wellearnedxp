**Well-Earned XP** introduces new ways to earn experience, allowing players to earn levels for enchanting and repairing.
Builders, miners, farmers, explorers, and mixed-style players can all earn XP by doing things they already spend time
doing, without feeling
pushed toward XP farms or mob grinders.

![earn xp from... building, mining, looting, and more](https://i.imgur.com/BPn8uzY.png)

## ✨ New Ways to Earn XP

* Mining
* Building
* Farming
* Crafting New Items
* Looting Chests
* Breaking Tools
* Completing Raids

## 📁 Data Packs

**Well-Earned XP** is completely data-driven and uses in-game statistics to detect rewardable player engagement. You can
easily add or remove support for different stats using a simple data
pack.

<details>

<summary>Learn More</summary>

Each JSON file defines:

- Which stat to listen for.
- How often that stat should grant XP.
- How much XP to spawn.

The file path selects the stat target:

```text
data/<namespace>/wellearnedxp/engagement_type/<category>/<namespace>/<path>.json
```

The category decides how the stat is matched:

- `stat_type` targets every stat under a stat type, for example, `minecraft:crafted`.
    - [For an exhaustive list of stat types, visit the wiki.](https://minecraft.wiki/w/Statistics#Statistic_types_and_names)
- `custom_stat` targets one specific custom stat, for example, `minecraft:fish_caught`.
    - [For an exhaustive list of custom stats, visit the wiki.](https://minecraft.wiki/w/Statistics#List_of_custom_statistic_names)

<details>

<summary>Stat Type Example</summary>

The following example grants 5 - 10 XP for every item crafted:

```json5
// data/mycoolexample/wellearnedxp/engagement_type/stat_type/minecraft/crafted.json

{
  "experience": {
    "min": 5,
    "max": 10
  }
}
```

</details>

<details>

<summary>Custom Stat Example</summary>

The following example grants 5 XP for every 10 fish caught:

```json5
// data/mycoolexample/wellearnedxp/engagement_type/custom_stat/minecraft/fish_caught.json

{
  "experience": {
    "min": 5,
    "max": 5
  },
  "interval": 10
}
```

</details>

<details>

<summary>Overriding Engagement Types</summary>

If you want to change any of the default values, like how much experience a stat gives, you can overwrite engagement types using a file of the same path. You can even disable built-in engagement types by setting the file's contents to an empty object, `{}`.

<details>

<summary>Disabled Engagement Type Example</summary>

By default, **Well-Earned XP** rewards players experience for mining. The following example disables that behavior:

```json5
// data/wellearnedxp/wellearnedxp/engagement_type/stat_type/minecraft/mined.json

{}
```

</details>

</details>

</details>

## 🚦 Permissions

✅ **You may use this mod in modpacks.**

✅ **You may create add-ons, extensions, or companion mods.**

✅ **You may submit pull requests to contribute to the code.**

❌ **You may not publish this mod or modified versions anywhere without explicit permission.**
