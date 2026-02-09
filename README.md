# Pixel Dungeon (Cross-Platform)

Personal cross-platform build of Pixel Dungeon, based on [Shattered Pixel Dungeon](https://github.com/00-Evan/shattered-pixel-dungeon) by [00-Evan](https://shatteredpixel.com/), which is itself based on the [original Pixel Dungeon](https://github.com/watabou/pixel-dungeon) by [Watabou](https://watabou.itch.io/).

## Targets

| Platform | Status | Module |
|----------|--------|--------|
| Desktop (Windows/Mac/Linux) | Working | `desktop/` |
| Android | Available (needs SDK) | `android/` |
| iOS | Planned | `ios/` |
| Browser (HTML5) | Planned | `html/` |

## Quick Start (Desktop)

```bash
# Build and run
./gradlew desktop:debug

# Build release JAR
./gradlew desktop:release
```

## Project Structure

```
pixel-dungeon/
├── SPD-classes/     # Noosa engine (libGDX-based)
├── core/            # Game logic (platform-independent)
├── android/         # Android launcher
├── desktop/         # Desktop launcher (LWJGL3)
├── ios/             # iOS launcher (RoboVM)
├── services/        # Update/news services
└── docs/            # Build guides
```

## Prerequisites

- **JDK 11+** (JDK 17 recommended)
- **Android SDK** (only for Android builds)
- **RoboVM** (only for iOS builds)

## Credits

- Original game: [Watabou](https://watabou.itch.io/) (GPLv3)
- Cross-platform port & content: [Shattered Pixel](https://shatteredpixel.com/)

## License

GNU General Public License v3.0 - see [LICENSE.txt](LICENSE.txt)
