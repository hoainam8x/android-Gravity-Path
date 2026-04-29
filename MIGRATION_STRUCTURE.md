# Gravity Path iOS -> Android Structure Mapping

## iOS source analysis

- Main app code: `GravityPath/*.swift`
- Local JSON config: `GravityPath/Config/levels.json`
- Localization: `en.lproj/Localizable.strings`, `vi.lproj/Localizable.strings`
- Legal: `GravityPath/Legal/*`
- Core game files:
  - UI/Flow: `ContentView.swift`, `HomeView.swift`, `WorldMapView.swift`, `GameRootView.swift`
  - Game loop/state: `GameScene.swift`, `GameState.swift`
  - Levels: `LevelModels.swift`, `LevelJSONLoader.swift`, `LevelsData.swift`, `LevelTheme.swift`
  - Ads: `AdConfig.swift`, `AdBannerView.swift`
  - Haptics/Audio: `HapticsManager.swift`, `AudioFX.swift`, `VocalFeedback.swift`
  - Storage: `RetentionStore.swift`
  - Localization helper: `L10n.swift`

## Android package structure created

Base package: `app/src/main/java/com/duna/gravitypath`

- `core/`
  - `ads/` (AdMob config, banner/interstitial wrappers)
  - `audio/` (sound/vocal abstraction)
  - `haptics/` (VibratorManager/API fallback)
  - `physics/` (gravity engine and collision utilities)
  - `localization/` (localization helpers)
  - `common/` (shared constants/util)
- `data/`
  - `local/json/` (level JSON loader in Android)
  - `local/preferences/` (retention/progress persistence)
  - `model/` (DTO/entities from data layer)
  - `mapper/` (data <-> domain mappers)
  - `repository/` (repository implementations)
- `domain/`
  - `model/` (domain models)
  - `repository/` (repository contracts)
  - `usecase/` (game/business use cases)
- `presentation/`
  - `navigation/` (Compose navigation routes)
  - `components/` (shared Compose components)
  - `home/` (Home screen + ViewModel)
  - `worldmap/` (WorldMap screen + ViewModel)
  - `game/` (Game screen + ViewModel)
- `di/` (dependency injection modules)

## Resources structure created

- `app/src/main/res/values/` (default `strings.xml` - English)
- `app/src/main/res/values-vi/` (Vietnamese `strings.xml`)
- `app/src/main/res/raw/` (store `levels.json`)
- `app/src/main/res/xml/` (AdMob and app XML configs)
