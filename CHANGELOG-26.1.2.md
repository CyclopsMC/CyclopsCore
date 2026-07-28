# Changelog for Minecraft 26.1.2
All notable changes to this project will be documented in this file.

<a name="26.1.2-1.30.2"></a>
## [26.1.2-1.30.2](https://github.com/CyclopsMC/CyclopsCore/compare/26.1.2-1.30.1...26.1.2-1.30.2) - 2026-07-28 16:55:07

### Fixed
* Fix EntityHelpers not spawning any entities
  Closes CyclopsMC/EvilCraft#1239
  Regression due to CyclopsMC/EvilCraft#1102

<a name="26.1.2-1.30.1"></a>
## [26.1.2-1.30.1](https://github.com/CyclopsMC/CyclopsCore/compare/26.1.2-1.30.0...26.1.2-1.30.1) - 2026-06-28 14:08:16 +0200

### Fixed
* Fix capacity-based fluid capability not taking into account stack size

<a name="26.1.2-1.30.0"></a>
## [26.1.2-1.30.0](https://github.com/CyclopsMC/CyclopsCore/compare/26.1.2-1.29.9...26.1.2-1.30.0) - 2026-06-14 13:02:01 +0200

### Added
* Add SlotSingleIngredient

<a name="26.1.2-1.29.9"></a>
## [26.1.2-1.29.9](https://github.com/CyclopsMC/CyclopsCore/compare/26.1.2-1.29.8...26.1.2-1.29.9) - 2026-05-22 11:20:34 +0200

### Added
* Add translations through Crowdin (#230)

### Changed
* Migrate from simulation to transaction logic in IngredientStorageHelpers

### Fixed
* Do class instance check before casting capabilities (#231)
  This ensures that a ClassCastException won't be generated to improve performance

<a name="26.1.2-1.29.8"></a>
## [26.1.2-1.29.8](https://github.com/CyclopsMC/CyclopsCore/compare/26.1.2-1.29.7...26.1.2-1.29.8) - 2026-05-03 06:50:28 +0200

### Fixed
* Fix fluid helper crashes on empty fluid handler resources

<a name="26.1.2-1.29.7"></a>
## [26.1.2-1.29.7](https://github.com/CyclopsMC/CyclopsCore/compare/26.1.2-1.29.6...26.1.2-1.29.7) - 2026-04-29 19:45:35 +0200

### Fixed
* Fix crash when registering GUIs on Fabric, Closes CyclopsMC/ColossalChests#200

<a name="26.1.2-1.29.6"></a>
## [26.1.2-1.29.6](https://github.com/CyclopsMC/CyclopsCore/compare/26.1.2-1.29.5...26.1.2-1.29.6) - 2026-04-28 19:44:31 +0200

### Fixed
* Fix active arrows on disabled WidgetNumberFields

<a name="26.1.2-1.29.5"></a>
## [26.1.2-1.29.5] - 2026-04-20 19:57:09 +0200

Initial 26.1.2 release
