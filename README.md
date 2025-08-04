# MapXYZ
A plugin that shows XYZ coordinates on maps, like the Legacy Console edition of Minecraft.

![A mostly blank map with the players XYZ coordinates printed on the top of the map.](https://cdn.modrinth.com/data/BFBWahSw/images/b832452d2fad693af75220e9d754182b8c9ce30a.png)

## Features (all are configurable)
- Enables starting map so that when a player joins for the first time, they will be granted a empty map
- Disables the coordinates in the F3 menu, making players rely on the map more in gameplay
![The plugin can forcefully disable the XYZ coordinates in the F3 menu](https://cdn.modrinth.com/data/BFBWahSw/images/6833fe3097a5d1ec38ae7c1b72cc2ee3989bc0ea.png)
- Scales the default map zoom to be zoomed out at 1:8 (Level 3/4) to match Legacy Console edition
- Locator maps for Java!
  - Shows other player markers on map with usernames underneath
![A map with another players marker on it.](https://cdn.modrinth.com/data/cached_images/c83cf0b832e5435340a9afaa2b7ac2e50147217d.png)

## Config
```yaml
# If true, show XYZ on map
enable-xyz-display: true
# If true, players will get an empty map when they first join the server.
enable-starting-map: true
# If true, maps will scale at 1:8 (Level 3/4) by default when a map is created.
use-legacy-console-default-zoom: true
# Enables the reducedDebugInfo game rule on all worlds
# This option hides position info in the Debug menu (F3)
enable-reduced-debug-info: true
# Enable locator maps, similar idea to the one on Bedrock Edition
# Shows other player's markers on maps
enable-locator-maps: true
# Make the XYZ display position show the eye level position instead of
# the players feet level. This matches Legacy Console Edition but may cause confusion
use-eye-level-position: false
# show players on maps inside of item frames
# barelly works lol
# requires enable-locator-maps to also be true
players-on-item-frames: false
```

## Commands
`/mapxyz - Reloads the config of the MapXYZ plugin.`

## Permissions
```yaml
mapxyz.command:
  default: op
  description: Allow players to execute the /mapxyz command
```
## Links

[![discord-singular](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/discord-singular_vector.svg)](https://discord.gg/2FfuShKQgy)

[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/modrinth_vector.svg)](https://modrinth.com/plugin/mapxyz) 
[![github](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/github_vector.svg)](https://github.com/letsgoawaydev/MapXYZ)

![spigot](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/spigot_vector.svg)
![paper](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact-minimal/supported/paper_vector.svg)
![purpur](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact-minimal/supported/purpur_vector.svg)

## Plans
[![trello](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/trello_vector.svg)](https://trello.com/b/aPtO93wK)

## Credits
[BananaTypeFont by BananaPuncher714](https://github.com/BananaPuncher714/BananaTypeFont)
