# Changelog for Minecraft 1.21.1
All notable changes to this project will be documented in this file.

<a name="1.21.1-1.26.2"></a>
## [1.21.1-1.26.2](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.26.1...1.21.1-1.26.2) - 2025-08-08 20:10:42

### Changed
* Add translations through Crowdin (#208)
* Update PT_BR localization (#206)

<a name="1.21.1-1.26.1"></a>
## [1.21.1-1.26.1](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.26.0...1.21.1-1.26.1) - 2025-05-31 19:12:50 +0200

### Fixed
* Fix scrollbar not moving on screen resize
  Closes CyclopsMC/IntegratedDynamics#1515

<a name="1.21.1-1.26.0"></a>
## [1.21.1-1.26.0](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.25.9...1.21.1-1.26.0) - 2025-05-03 15:27:36 +0200

### Added
* Allow infobooks to be placed in lecterns
  Required for CyclopsMC/IntegratedDynamics#1496 and CyclopsMC/EvilCraft#1110.

<a name="1.21.1-1.25.9"></a>
## [1.21.1-1.25.9](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.25.8...1.21.1-1.25.9) - 2025-04-12 13:29:59 +0200

### Fixed
* Fix insertion into some slots failing
  Closes CyclopsMC/IntegratedDynamics#1502

<a name="1.21.1-1.25.8"></a>
## [1.21.1-1.25.8](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.25.7...1.21.1-1.25.8) - 2025-03-24 16:19:02 +0100

### Fixed
* Fix not all key combinations working for key bindings
  Closes CyclopsMC/IntegratedTerminals#166

<a name="1.21.1-1.25.7"></a>
## [1.21.1-1.25.7](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.25.6...1.21.1-1.25.7) - 2025-02-24 16:19:08 +0100

### Fixed
* Fix EvilCraft entity crash with some mobs, Closes CyclopsMC/EvilCraft#1102

<a name="1.21.1-1.25.6"></a>
## [1.21.1-1.25.6](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.25.5...1.21.1-1.25.6) - 2025-02-03 20:06:56 +0100

### Added
* Add ja_jp translations
* Add nl_nl translations

### Fixed
* Fix items with empty fluid containers being stored inconsistently
Items with empty fluid containers could be stored in two different ways.
This change ensures that there is only a single canonical way to store
them.
This could cause issues with items from EvilCraft when attempting to
autocraft them and getting them recognized by the system.
Closes CyclopsMC/EvilCraft#1089

<a name="1.21.1-1.25.5"></a>
## [1.21.1-1.25.5](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.25.4...1.21.1-1.25.5) - 2024-12-06 16:01:04 +0100

### Added
* Add IDynamicModelElementCommon

<a name="1.21.1-1.25.4"></a>
## [1.21.1-1.25.4](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.25.3...1.21.1-1.25.4) - 2024-11-25 16:00:51 +0100

### Fixed
* Improve client performance of large scrolling containers
  This fixes performance issues when having the gui open of a large Colossal Chest.

<a name="1.21.1-1.25.3"></a>
## [1.21.1-1.25.3](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.25.2...1.21.1-1.25.3) - 2024-11-19 10:09:18 +0100

### Changed
* Invoke Fabric block explode event before block is removed
  Required for CyclopsMC/ColossalChests#174

<a name="1.21.1-1.25.2"></a>
## [1.21.1-1.25.2](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.25.1...1.21.1-1.25.2) - 2024-10-23 18:53:06 +0200

### Fixed
* Fix recipe caching not considering different rotations, Closes CyclopsMC/IntegratedTerminals#129

<a name="1.21.1-1.25.1"></a>
## [1.21.1-1.25.1](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.25.0...1.21.1-1.25.1) - 2024-10-09 19:16:16 +0200

### Added
* Add fluid gui helpers to Forge and Fabric

<a name="1.21.1-1.25.0"></a>
## [1.21.1-1.25.0](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.24.0...1.21.1-1.25.0) - 2024-10-09 16:48:58 +0200

### Added
* Improve multiloader support
  * Add IBlockExplodedEvent
  * Add ConditionConfigFabric
  * Move ItemStackBlockEntityRendererBase to common
  * Add ConditionConfigForge
  * Port scrolling container to common
  * Port large and indexed inventory to common
  * Move multiblock helpers to common

<a name="1.21.1-1.24.0"></a>
## [1.21.1-1.24.0](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.23.0...1.21.1-1.24.0) - 2024-09-30 17:01:48 +0200

### Added
* Improve multiloader support
  * Support extended screen handlers in Fabric
  * Add Fabric events for entity join, entity tick, and player join
  * Add ILivingEntityRendererEvent for Fabric
  * Add ILootTableModifyEvent for Fabric
  * Add LootModifierConfigForge
  * Migrate particles to common
  * Add getCraftingRemainingItem to ItemStackHelpers
  * Abstract ContainerTypeData to common
  * Move NamedContainerProviderItem to common
  * Move ItemGui to common
  * Add gui helpers to common
  * Move gui components to common
  * Add ItemInventoryContainerCommon to common
  * Move item location to common
  * Move config command to common
  * Migrate ring of fire and debug command to common
  * Abstract packet handling across mod loaders
  * Allow commands to be registered in common base mod
  * Add world helpers to common

### Changed
* Make DeferredHolderCommon work with IForgeRegistry

### Fixed
* Fix loader-specific configurable types sometimes not working

<a name="1.21.1-1.23.0"></a>
## [1.21.1-1.23.0](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.22.0...1.21.1-1.23.0) - 2024-09-16 16:41:25 +0200

### Added
* Port advancement criteria to Forge and Fabric
* Add biome modifier config to Forge
* Add entity config to common

### Changed
* Move capacity and energy components to common

### Fixed
* Fix some config registrations for some registries in Forge failing

<a name="1.21.1-1.22.0"></a>
## [1.21.1-1.22.0](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.21.1...1.21.1-1.22.0) - 2024-09-04 17:21:43 +0200

### Added
* Additions for multiloader support
  * Add crafting helpers to common
  * Move registry manager to common

<a name="1.21.1-1.21.1"></a>
## [1.21.1-1.21.1](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.21.0...1.21.1-1.21.1) - 2024-09-01 17:31:39 +0200

### Fixed
* Fix config dictionary not being fully populated
  This could cause mods with infobooks still depending on the old configs
  API to miss many book entries.
  Closes CyclopsMC/EvilCraft#1055

<a name="1.21.1-1.21.0"></a>
## [1.21.1-1.21.0](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.20.5...1.21.1-1.21.0) - 2024-09-01 15:21:27 +0200

### Added
* Additions for multiloader support
  * Implement barebones openMenu helper for Fabric
  * Port base guis and registration to common
  * Add SimpleInventoryCommon
  * Move blocks with guis to common

<a name="1.21.1-1.20.5"></a>
## [1.21.1-1.20.5](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.20.4...1.21.1-1.20.5) - 2024-08-25 19:41:48 +0200

### Fixed
* Fix invalid config file entry names being generated

<a name="1.21.1-1.20.4"></a>
## [1.21.1-1.20.4](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.20.3...1.21.1-1.20.4) - 2024-08-24 07:04:11 +0200

### Fixed
* Fix other MinecraftHelpers.* causing crashes at startup

<a name="1.21.1-1.20.3"></a>
## [1.21.1-1.20.3](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.20.2...1.21.1-1.20.3) - 2024-08-23 11:09:55 +0200

### Fixed
* Fix Helpers.* causing crashes at startup

<a name="1.21.1-1.20.2"></a>
## [1.21.1-1.20.2](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.20.1...1.21.1-1.20.2) - 2024-08-22 17:48:15 +0200

### Fixed
* Fix MinecraftHelpers.isClientSide causing crashes at startup

<a name="1.21.1-1.20.1"></a>
## [1.21.1-1.20.1](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.20.0...1.21.1-1.20.1) - 2024-08-20 17:42:51 +0200

### Fixed
* Fix CyclopsCore mod helpers being invoked before mod is initialized, Closes #190

<a name="1.21.1-1.20.0"></a>
## [1.21.1-1.20.0](https://github.com/CyclopsMC/CyclopsCore/compare/1.21.1-1.19.11...1.21.1-1.20.0) - 2024-08-18 12:52:33 +0200

### Changed
* Refactor code to be compatible with NeoForge, Forge, and Fabric.

<a name="1.21.1-1.19.11"></a>
## [1.21.1-1.19.11] - 2024-08-09 21:00:37 +0200

### Fixed
* Update to updated CommonCapabilities API
  Required for CyclopsMC/IntegratedDynamics#1375
