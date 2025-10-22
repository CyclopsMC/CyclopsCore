# Changelog for Minecraft 1.20.1
All notable changes to this project will be documented in this file.

<a name="1.20.1-1.21.2"></a>
## [1.20.1-1.21.2](/compare/1.20.1-1.21.1...1.20.1-1.21.2) - 2025-10-22 16:25:44

### Fixed
* Fix empty item entities being spawned
  Closes CyclopsMC/IntegratedTunnels#348

<a name="1.20.1-1.21.1"></a>
## [1.20.1-1.21.1](/compare/1.20.1-1.21.0...1.20.1-1.21.1) - 2025-10-11 10:52:11 +0200

### Fixed
* Add client-side null player safety check to PacketHandler, Closes #211

<a name="1.20.1-1.21.0"></a>
## [1.20.1-1.21.0](/compare/1.20.1-1.20.1...1.20.1-1.21.0) - 2025-10-07 07:34:00 +0200

### Added
* Add IngredientComponentStorageComposite (required for Integrated Mekanism)

<a name="1.20.1-1.20.1"></a>
## [1.20.1-1.20.1](/compare/1.20.1-1.20.0...1.20.1-1.20.1) - 2025-05-31 19:12:38 +0200

### Fixed
* Fix scrollbar not moving on screen resize
  Closes CyclopsMC/IntegratedDynamics#1515

<a name="1.20.1-1.20.0"></a>
## [1.20.1-1.20.0](/compare/1.20.1-1.19.10...1.20.1-1.20.0) - 2025-05-03 15:08:33 +0200

### Added
* Allow infobooks to be placed in lecterns
  Required for CyclopsMC/IntegratedDynamics#1496 and CyclopsMC/EvilCraft#1110.

<a name="1.20.1-1.19.10"></a>
## [1.20.1-1.19.10](/compare/1.20.1-1.19.9...1.20.1-1.19.10) - 2025-04-12 13:03:35 +0200

### Fixed
* Improve client performance of large scrolling containers
  This fixes performance issues when having the gui open of a large Colossal Chest.

<a name="1.20.1-1.19.9"></a>
## [1.20.1-1.19.9](/compare/1.20.1-1.19.8...1.20.1-1.19.9) - 2025-03-24 16:09:38 +0100

### Fixed
* Fix not all key combinations working for key bindings
  Closes CyclopsMC/IntegratedTerminals#166

<a name="1.20.1-1.19.8"></a>
## [1.20.1-1.19.8](/compare/1.20.1-1.19.7...1.20.1-1.19.8) - 2025-03-11 07:01:52 +0100

### Fixed
* Fix crash when loading empty fluid containers from NBT

<a name="1.20.1-1.19.7"></a>
## [1.20.1-1.19.7](/compare/1.20.1-1.19.6...1.20.1-1.19.7) - 2025-02-16 14:43:50 +0100

### Fixed
* Fix broken EvilCraft tanks upgrades, Closes CyclopsMC/EvilCraft#1099

<a name="1.20.1-1.19.6"></a>
## [1.20.1-1.19.6](/compare/1.20.1-1.19.5...1.20.1-1.19.6) - 2025-02-03 19:47:06 +0100

### Fixed
* Fix items with empty fluid containers being stored inconsistently
Items with empty fluid containers could be stored in two different ways.
This change ensures that there is only a single canonical way to store
them.
This could cause issues with items from EvilCraft when attempting to
autocraft them and getting them recognized by the system.
Closes CyclopsMC/EvilCraft#1089

<a name="1.20.1-1.19.5"></a>
## [1.20.1-1.19.5](/compare/1.20.1-1.19.4...1.20.1-1.19.5) - 2024-07-31 13:13:24 +0200

### Fixed
* Fix getGuiTexture() not being used in ContainerScreenExtended

<a name="1.20.1-1.19.4"></a>
## [1.20.1-1.19.4](/compare/1.20.1-1.19.3...1.20.1-1.19.4) - 2024-07-23 13:42:11 +0200

### Added
* Add tr_tr.json Turkish localization

### Changed
* Expose cache in IngredientCollectionDiffManager
  Required for CyclopsMC/IntegratedDynamics#1359

### Fixed
* Make RegistryExportableRegistry thread-safe
  This fixes rare crashes at startup
  Closes CyclopsMC/IntegratedDynamics#1360

<a name="1.20.1-1.19.3"></a>
## [1.20.1-1.19.3](/compare/1.20.1-1.19.2...1.20.1-1.19.3) - 2024-07-21 11:35:40 +0200

### Fixed
* Make RegistryExportableRegistry thread-safe
  This fixes rare crashes at startup
  Closes CyclopsMC/IntegratedDynamics#1360

<a name="1.20.1-1.19.2"></a>
## [1.20.1-1.19.2](/compare/1.20.1-1.19.1...1.20.1-1.19.2) - 2024-06-24 10:28:49 +0200

### Changed
* Only enable dumpregistries command for OPs, Closes #180

<a name="1.20.1-1.19.1"></a>
## [1.20.1-1.19.1](/compare/1.20.1-1.19.0...1.20.1-1.19.1) - 2024-04-14 13:55:33 +0200

### Fixed
* Fix infobook sections from other mods linking to wrong URL
  Closes CyclopsMC/IntegratedDynamics#1338

<a name="1.20.1-1.19.0"></a>
## [1.20.1-1.19.0](/compare/1.20.1-1.18.14...1.20.1-1.19.0) - 2024-02-04 16:04:08 +0100

### Added
* Add textfield appendix type (required for Integrated Scripting)

### Changed
* Allow first row to be set in scrollbar widget

<a name="1.20.1-1.18.14"></a>
## [1.20.1-1.18.14](/compare/1.20.1-1.18.13...1.20.1-1.18.14) - 2023-11-27 15:49:21 +0100

### Fixed
* Fix simple ingredient types returning wrong collapsed collections
  This fixes an Integrated Terminals issue where energy interactions
  would be visualized incorrectly.
  Closes CyclopsMC/IntegratedTerminals#111

<a name="1.20.1-1.18.13"></a>
## [1.20.1-1.18.13](/compare/1.20.1-1.18.12...1.20.1-1.18.13) - 2023-10-21 08:28:53 +0200

### Fixed
* Actually fix lack of background render events in infobooks
  Closes CyclopsMC/EvilCraft#1008

<a name="1.20.1-1.18.12"></a>
## [1.20.1-1.18.12](/compare/1.20.1-1.18.11...1.20.1-1.18.12) - 2023-10-19 16:51:55 +0200

### Fixed
* Fix lack of background render events in infobooks, Closes CyclopsMC/EvilCraft#1008
* Fix network errors not being caught by PacketHandler
  Required for debugging CyclopsMC/IntegratedTerminals#102
* Fix crash when interacting with empty arrowed list field

<a name="1.20.1-1.18.11"></a>
## [1.20.1-1.18.11](/compare/1.20.1-1.18.10...1.20.1-1.18.11) - 2023-10-13 17:21:01 +0200

### Fixed
* Fix duping exploit in phantom slots
  Closes CyclopsMC/IntegratedDynamics#1310

<a name="1.20.1-1.18.10"></a>
## [1.20.1-1.18.10](/compare/1.20.1-1.18.9...1.20.1-1.18.10) - 2023-08-27 11:41:47 +0200

### Added
* Improve error logging for packet codec failures
  Required for debugging CyclopsMC/IntegratedTerminals#102

### Fixed
* Fix text fields not losing focus when needed
  Closes CyclopsMC/IntegratedDynamics#1297
* Fix EMI overlapping with infobook GUI
  This caused issues in EvilCraft and Integrated Dynamics.
  Closes CyclopsMC/IntegratedDynamics#1296

<a name="1.20.1-1.18.9"></a>
## [1.20.1-1.18.9](/compare/1.20.1-1.18.8...1.20.1-1.18.9) - 2023-08-04 07:47:09 +0200

### Fixed
* Fix capabilities baking multiple times, #177

<a name="1.20.1-1.18.8"></a>
## [1.20.1-1.18.8](/compare/1.20.1-1.18.7...1.20.1-1.18.8) - 2023-07-31 14:57:30 +0200

### Fixed
* Fix creative tabs being registered incorrectly
  Closes CyclopsMC/IntegratedTerminals#107
  Closes CyclopsMC/IntegratedDynamics#1288

<a name="1.20.1-1.18.7"></a>
## [1.20.1-1.18.7](/compare/1.20.1-1.18.6...1.20.1-1.18.7) - 2023-07-16 11:15:08 +0200

### Fixed
* Fix slot text not correctly scoping posestack
  This fixes various issues in Integrated Terminals related to the gui shifting.
  Closes CyclopsMC/IntegratedTerminals#105

<a name="1.20.1-1.18.6"></a>
## [1.20.1-1.18.6](/compare/1.20.1-1.18.5...1.20.1-1.18.6) - 2023-07-08 14:42:06 +0200

### Fixed
* Fix focus not being set on WidgetTextFieldExtended

<a name="1.20.1-1.18.5"></a>
## [1.20.1-1.18.5](/compare/1.20.1-1.18.4...1.20.1-1.18.5) - 2023-07-04 17:09:53 +0200

### Fixed
* Fix creative tab not being stored in mod class
  This fixes a bug in Everlasting Abilities where totems do not show up in the creative tab.
  Closes CyclopsMC/EverlastingAbilities#232

<a name="1.20.1-1.18.4"></a>
## [1.20.1-1.18.4] - 2023-07-02 08:09:40 +0200

Initial 1.20.1 release
