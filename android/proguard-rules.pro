# Pixel Dungeon ProGuard Rules

# Keep the Bundle serialization system (uses reflection for class loading)
-keep class com.watabou.utils.Bundlable
-keep class * implements com.watabou.utils.Bundlable {
    *;
}

# Keep all game classes that are loaded by name
-keep class com.watabou.pixeldungeon.** { *; }
-keep class com.watabou.noosa.** { *; }
-keep class com.watabou.utils.** { *; }
-keep class com.watabou.gltextures.** { *; }
-keep class com.watabou.glwrap.** { *; }
-keep class com.watabou.glscripts.** { *; }
-keep class com.watabou.input.** { *; }
