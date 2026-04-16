# Changelog for Minecraft 26.1.1
All notable changes to this project will be documented in this file.

<a name="26.1.1-1.29.4"></a>
## [26.1.1-1.29.4](https://github.com/CyclopsMC/CyclopsCore/compare/26.1.1-1.29.3...26.1.1-1.29.4) - 2026-04-16 07:29:39

### Fixed
* Fix Mixin breaking other DeferredHolders

<a name="26.1.1-1.29.3"></a>
## [26.1.1-1.29.3](https://github.com/CyclopsMC/CyclopsCore/compare/26.1.1-1.29.2...26.1.1-1.29.3) - 2026-04-14 20:08:17 +0200

### Fixed
* Fix DeferredHolderCommon not handling delegates correctly
  This could sometimes lead to crashes in rare conditions during itemstack serialization.

<a name="26.1.1-1.29.2"></a>
## [26.1.1-1.29.2](https://github.com/CyclopsMC/CyclopsCore/compare/26.1.1-1.29.1...26.1.1-1.29.2) - 2026-04-13 16:46:41 +0200

### Fixed
* Fix exception when ItemStackFromIngredient has empty tag
  Closes CyclopsMC/IntegratedDynamics#1650

<a name="26.1.1-1.29.1"></a>
## [26.1.1-1.29.1](https://github.com/CyclopsMC/CyclopsCore/compare/26.1.1-1.29.0...26.1.1-1.29.1) - 2026-04-12 09:13:02 +0200

### Fixed
* Restore removed drawSlotText method, Closes CyclopsMC/IntegratedTerminals#197
* Make MethodGameTestInstance safe to null error messages

<a name="26.1.1-1.29.0"></a>
## [26.1.1-1.29.0] - 2026-04-10 14:37:43 +0200

Initial 26.1.1 release
