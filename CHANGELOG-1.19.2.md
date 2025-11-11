# Changelog for Minecraft 1.19.2
All notable changes to this project will be documented in this file.

<a name="1.19.2-1.20.3"></a>
## [1.19.2-1.20.3](/compare/1.19.2-1.20.2...1.19.2-1.20.3) - 2025-11-11 13:48:56

### Fixed
* Fix commands not being available after reload command
  Closes CyclopsMC/IntegratedDynamics#1556

<a name="1.19.2-1.20.2"></a>
## [1.19.2-1.20.2](/compare/1.19.2-1.20.1...1.19.2-1.20.2) - 2025-10-22 16:24:44 +0200

### Fixed
* Fix empty item entities being spawned
  Closes CyclopsMC/IntegratedTunnels#348

<a name="1.19.2-1.20.1"></a>
## [1.19.2-1.20.1](/compare/1.19.2-1.20.0...1.19.2-1.20.1) - 2025-05-31 19:12:27 +0200

### Fixed
* Fix scrollbar not moving on screen resize
  Closes CyclopsMC/IntegratedDynamics#1515

<a name="1.19.2-1.20.0"></a>
## [1.19.2-1.20.0](/compare/1.19.2-1.19.9...1.19.2-1.20.0) - 2025-05-03 15:05:29 +0200

### Added
* Allow infobooks to be placed in lecterns
  Required for CyclopsMC/IntegratedDynamics#1496 and CyclopsMC/EvilCraft#1110.

<a name="1.19.2-1.19.9"></a>
## [1.19.2-1.19.9](/compare/1.19.2-1.19.8...1.19.2-1.19.9) - 2025-03-24 16:08:09 +0100

### Fixed
* Fix not all key combinations working for key bindings
  Closes CyclopsMC/IntegratedTerminals#166

<a name="1.19.2-1.19.8"></a>
## [1.19.2-1.19.8](/compare/1.19.2-1.19.7...1.19.2-1.19.8) - 2025-03-11 07:01:08 +0100

### Fixed
* Fix crash when loading empty fluid containers from NBT

<a name="1.19.2-1.19.7"></a>
## [1.19.2-1.19.7](/compare/1.19.2-1.19.6...1.19.2-1.19.7) - 2025-02-16 14:43:04 +0100

### Fixed
* Fix broken EvilCraft tanks upgrades, Closes CyclopsMC/EvilCraft#1099

<a name="1.19.2-1.19.6"></a>
## [1.19.2-1.19.6](/compare/1.19.2-1.19.5...1.19.2-1.19.6) - 2025-02-03 19:45:42 +0100

### Fixed
* Fix items with empty fluid containers being stored inconsistently
Items with empty fluid containers could be stored in two different ways.
This change ensures that there is only a single canonical way to store
them.
This could cause issues with items from EvilCraft when attempting to
autocraft them and getting them recognized by the system.
Closes CyclopsMC/EvilCraft#1089

<a name="1.19.2-1.19.5"></a>
## [1.19.2-1.19.5](/compare/1.19.2-1.19.4...1.19.2-1.19.5) - 2024-07-31 12:56:15 +0200

### Fixed
* Fix getGuiTexture() not being used in ContainerScreenExtended

<a name="1.19.2-1.19.4"></a>
## [1.19.2-1.19.4](/compare/1.19.2-1.19.3...1.19.2-1.19.4) - 2024-07-23 13:37:53 +0200

### Changed
* Expose cache in IngredientCollectionDiffManager
  Required for CyclopsMC/IntegratedDynamics#1359

<a name="1.19.2-1.19.3"></a>
## [1.19.2-1.19.3](/compare/1.19.2-1.19.2...1.19.2-1.19.3) - 2024-07-21 11:34:04 +0200

### Fixed
* Make RegistryExportableRegistry thread-safe
  This fixes rare crashes at startup
  Closes CyclopsMC/IntegratedDynamics#1360

<a name="1.19.2-1.19.2"></a>
## [1.19.2-1.19.2](/compare/1.19.2-1.19.1...1.19.2-1.19.2) - 2024-06-24 10:27:26 +0200

### Fixed
* Only enable dumpregistries command for OPs, Closes #180

<a name="1.19.2-1.19.1"></a>
## [1.19.2-1.19.1](/compare/1.19.2-1.19.0...1.19.2-1.19.1) - 2024-04-14 13:50:49 +0200

### Fixed
* Fix infobook sections from other mods linking to wrong URL
  Closes CyclopsMC/IntegratedDynamics#1338

<a name="1.19.2-1.19.0"></a>
## [1.19.2-1.19.0](/compare/1.19.2-1.18.11...1.19.2-1.19.0) - 2024-02-04 14:49:24 +0100

### Added
* Add textfield appendix type for infobook (required for Integrated Scripting)

### Changed
* Allow first row to be set in scrollbar widget

<a name="1.19.2-1.18.11"></a>
## [1.19.2-1.18.11](/compare/1.19.2-1.18.10...1.19.2-1.18.11) - 2023-11-27 15:46:14 +0100

### Fixed
* Fix simple ingredient types returning wrong collapsed collections
  This fixes an Integrated Terminals issue where energy interactions
  would be visualized incorrectly.
  Closes CyclopsMC/IntegratedTerminals#111

<a name="1.19.2-1.18.10"></a>
## [1.19.2-1.18.10](/compare/1.19.2-1.18.9...1.19.2-1.18.10) - 2023-10-21 08:25:50 +0200

### Fixed
* Actually fix lack of background render events in infobooks
  Closes CyclopsMC/EvilCraft#1008

<a name="1.19.2-1.18.9"></a>
## [1.19.2-1.18.9](/compare/1.19.2-1.18.8...1.19.2-1.18.9) - 2023-10-19 16:48:10 +0200

### Fixed
* Fix lack of background render events in infobooks, Closes CyclopsMC/EvilCraft#1008
* Fix network errors not being caught by PacketHandler
  Required for debugging CyclopsMC/IntegratedTerminals#102
* Fix crash when interacting with empty arrowed list field

<a name="1.19.2-1.18.8"></a>
## [1.19.2-1.18.8](/compare/1.19.2-1.18.7...1.19.2-1.18.8) - 2023-08-27 11:31:35 +0200

### Changed
* Improve error logging for packet codec failures
  Required for debugging CyclopsMC/IntegratedTerminals#102

<a name="1.19.2-1.18.7"></a>
## [1.19.2-1.18.7](/compare/1.19.2-1.18.6...1.19.2-1.18.7) - 2023-08-04 07:45:45 +0200

### Fixed
* Fix capabilities baking multiple times, #177

<a name="1.19.2-1.18.6"></a>
## [1.19.2-1.18.6](/compare/1.19.2-1.18.5...1.19.2-1.18.6) - 2023-06-10 15:34:59 +0200

### Changed
* Allow scrolling through infobooks
  Closes CyclopsMC/IntegratedDynamics#1076

<a name="1.19.2-1.18.5"></a>
## [1.19.2-1.18.5](/compare/1.19.2-1.18.4...1.19.2-1.18.5) - 2023-06-04 08:46:14 +0200

### Fixed
* Fix Colossal Chest errors showing incorrect size
  Closes CyclopsMC/EvilCraft#986

<a name="1.19.2-1.18.4"></a>
## [1.19.2-1.18.4](/compare/1.19.2-1.18.3...1.19.2-1.18.4) - 2023-05-01 16:27:29 +0200

### Fixed
* Optimize containment check in single-classified ingred collection
  This improves performance for large Integrated Terminals terminals with JEI.
  Related to CyclopsMC/IntegratedDynamics#1247

<a name="1.19.2-1.18.3"></a>
## [1.19.2-1.18.3](/compare/1.19.2-1.18.2...1.19.2-1.18.3) - 2023-04-16 15:54:21 +0200

### Fixed
* Fix disconnect when sending invalid container values
  Closes CyclopsMC/IntegratedDynamics#1255

<a name="1.19.2-1.18.2"></a>
## [1.19.2-1.18.2](/compare/1.19.2-1.18.1...1.19.2-1.18.2) - 2023-03-15 07:23:43 +0100

### Added
* Support Vec3i in PacketCodec
* Add DimPos.withPosition

<a name="1.19.2-1.18.1"></a>
## [1.19.2-1.18.1](/compare/1.19.2-1.18.0...1.19.2-1.18.1) - 2023-03-05 13:17:41 +0100

### Fixed
* Fix incorrect optimization in IngredientCollectionPrototypeMap

<a name="1.19.2-1.18.0"></a>
## [1.19.2-1.18.0](/compare/1.19.2-1.17.5...1.19.2-1.18.0) - 2023-03-05 11:32:47 +0100

### Added
* Add single-classified collapsed ingredient collection
  This allows for more efficient match-based containment checking and
  iteration for mods such as Integrated Dynamics, Tunnels, and Terminals..

<a name="1.19.2-1.17.5"></a>
## [1.19.2-1.17.5](/compare/1.19.2-1.17.4...1.19.2-1.17.5) - 2023-02-12 06:28:47 +0100

### Fixed
* Fix fluid properties not being applied
  Related to CyclopsMC/EvilCraft#967

<a name="1.19.2-1.17.4"></a>
## [1.19.2-1.17.4](/compare/1.19.2-1.17.3...1.19.2-1.17.4) - 2023-01-21 07:05:18 +0100

### Fixed
* Fix incorrect containment check in IngredientCollectionPrototypeMap
  This is a regression since 61267920638e48331a513b9a446907910122078d
  Closes CyclopsMC/IntegratedTerminals#98

<a name="1.19.2-1.17.3"></a>
## [1.19.2-1.17.3](/compare/1.19.2-1.17.2...1.19.2-1.17.3) - 2022-12-30 10:00:31 +0100

### Fixed
* Fix config changes not always taking effect, Closes CyclopsMC/EverlastingAbilities#211

<a name="1.19.2-1.17.2"></a>
## [1.19.2-1.17.2](/compare/1.19.2-1.17.1...1.19.2-1.17.2) - 2022-12-11 13:50:43 +0100

### Changed
* Optimize containment checks in IngredientCollectionPrototypeMap
  This fixes performance issues such as CyclopsMC/IntegratedTerminals#94

<a name="1.19.2-1.17.1"></a>
## [1.19.2-1.17.1](/compare/1.19.2-1.17.0...1.19.2-1.17.1) - 2022-12-10 08:51:34 +0100

### Added
* Add pt_br.json

### Fixed
* Fix fluids having no localized names
  Closes CyclopsMC/EvilCraft#950

<a name="1.19.2-1.17.0"></a>
## [1.19.2-1.17.0] - 2022-08-11 19:47:20 +0200

Update to MC 1.19.2
