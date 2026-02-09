Pixel Dungeon
=============

Traditional roguelike game with pixel-art graphics and simple interface.

Based on the original by Oleg Dolya (watabou).

## Project Structure

```
pixel-dungeon/
├── android/          # Android application module
│   └── src/main/
│       ├── java/     # Game source code
│       ├── assets/   # Sprites, tilesets, audio
│       └── res/      # Android resources (icons, strings)
├── pd-classes/       # Noosa engine library module
│   └── src/main/
│       └── java/     # Engine source (rendering, audio, input, utils)
├── build.gradle      # Root build configuration
├── settings.gradle   # Module includes
└── gradle/           # Gradle wrapper
```

## Building

### Prerequisites

- Java 11+ (JDK)
- Android SDK with:
  - Build Tools 34.0.0+
  - Platform SDK 34

### Setup

1. Install the Android SDK and set `ANDROID_HOME` environment variable, or create
   a `local.properties` file in the project root:

```properties
sdk.dir=/path/to/your/android/sdk
```

2. Build the debug APK:

```bash
./gradlew android:assembleDebug
```

3. The APK will be at `android/build/outputs/apk/debug/android-debug.apk`

## Future Targets

This project is structured for cross-platform expansion:

- **Browser** (HTML5 via GWT/TeaVM)
- **iOS** (via RoboVM or Multi-OS Engine)
- **Desktop** (via LWJGL)

## Original Links

- Original source: https://github.com/watabou/pixel-dungeon
- Engine library: https://github.com/watabou/PD-classes

## License

GNU General Public License v3.0 - see [LICENSE.txt](LICENSE.txt)
