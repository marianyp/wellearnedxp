# Well-Earned XP

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

### Learn More

<div class="spoiler">

<p>Each JSON file defines:</p>

<ul>
    <li>Which stat to listen for.</li>
    <li>How often that stat should grant XP.</li>
    <li>How much XP to spawn.</li>
</ul>

<p>The file path selects the stat target:</p>

<pre><code>data/&lt;namespace&gt;/wellearnedxp/engagement_type/&lt;category&gt;/&lt;namespace&gt;/&lt;path&gt;.json</code></pre>

<p>The category decides how the stat is matched:</p>

<ul>
    <li>
        <code>stat_type</code> targets every stat under a stat type, for example, <code>minecraft:crafted</code>.
        <ul>
            <li><a href="https://minecraft.wiki/w/Statistics#Statistic_types_and_names">For an exhaustive list of stat types, visit the wiki.</a></li>
        </ul>
    </li>
    <li>
        <code>custom_stat</code> targets one specific custom stat, for example, <code>minecraft:fish_caught</code>.
        <ul>
            <li><a href="https://minecraft.wiki/w/Statistics#List_of_custom_statistic_names">For an exhaustive list of custom stats, visit the wiki.</a></li>
        </ul>
    </li>
</ul>

<h4>Stat Type Example</h4>

<div class="spoiler">

<p>The following example grants 5 - 10 XP for every item crafted:</p>

<pre><code>// data/mycoolexample/wellearnedxp/engagement_type/stat_type/minecraft/crafted.json

{
  "experience": {
    "min": 5,
    "max": 10
  }
}</code></pre>

</div>

<h4>Custom Stat Example</h4>

<div class="spoiler">

<p>The following example grants 5 XP for every 10 fish caught:</p>

<pre><code>// data/mycoolexample/wellearnedxp/engagement_type/custom_stat/minecraft/fish_caught.json

{
  "experience": {
    "min": 5,
    "max": 5
  },
  "interval": 10
}</code></pre>

</div>

<h3>Overriding Engagement Types</h3>

<div class="spoiler">

<p>If you want to change any of the default values, like how much experience a stat gives, you can overwrite engagement types using a file of the same path. You can even disable built-in engagement types by setting the file's contents to an empty object, <code>{}</code>.</p>

<h4>Disabled Engagement Type Example</h4>

<div class="spoiler">

<p>By default, <strong>Well-Earned XP</strong> rewards players experience for mining. The following example disables that behavior:</p>

<pre><code>// data/wellearnedxp/wellearnedxp/engagement_type/stat_type/minecraft/mined.json

{}</code></pre>

</div>

</div>

</div>

## 🚦 Permissions

✅ **You may use this mod in modpacks.**

✅ **You may create add-ons, extensions, or companion mods.**

✅ **You may submit pull requests to contribute to the code.**

❌ **You may not publish this mod or modified versions anywhere without explicit permission.**
