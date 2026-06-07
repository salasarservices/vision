#!/bin/bash
set -e

ANDROID_JAR="/usr/lib/android-sdk/platforms/android-23/android.jar"
DX="/usr/lib/android-sdk/build-tools/debian/dx"
AAPT2="/usr/bin/aapt2"
APKSIGNER="/usr/bin/apksigner"
ZIPALIGN="/usr/bin/zipalign"

SRC_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$SRC_DIR/build"
OUT_APK="$SRC_DIR/DepthWallpaper.apk"

echo "=== Cleaning build dir ==="
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR/compiled_res" "$BUILD_DIR/classes" "$BUILD_DIR/gen/com/salasar/depthwallpaper"

echo "=== Compiling resources ==="
"$AAPT2" compile \
    --dir "$SRC_DIR/src/main/res" \
    -o "$BUILD_DIR/compiled_res/"

echo "=== Linking resources ==="
"$AAPT2" link \
    -o "$BUILD_DIR/linked.apk" \
    -I "$ANDROID_JAR" \
    --manifest "$SRC_DIR/src/main/AndroidManifest.xml" \
    --java "$BUILD_DIR/gen" \
    --min-sdk-version 23 \
    --target-sdk-version 28 \
    --version-code 1 \
    --version-name "1.0" \
    "$BUILD_DIR/compiled_res/"*.flat

echo "=== Compiling Java sources ==="
find "$SRC_DIR/src/main/java" -name "*.java" > "$BUILD_DIR/sources.txt"
find "$BUILD_DIR/gen" -name "*.java" >> "$BUILD_DIR/sources.txt"

javac -source 8 -target 8 \
    -bootclasspath "$ANDROID_JAR" \
    -cp "$ANDROID_JAR" \
    -d "$BUILD_DIR/classes" \
    @"$BUILD_DIR/sources.txt"

echo "=== Converting to DEX ==="
"$DX" --dex \
    --output="$BUILD_DIR/classes.dex" \
    "$BUILD_DIR/classes"

echo "=== Assembling APK ==="
cp "$BUILD_DIR/linked.apk" "$BUILD_DIR/unsigned.apk"
# Inject classes.dex into the APK zip
cd "$BUILD_DIR"
zip -j unsigned.apk classes.dex
cd "$SRC_DIR"

echo "=== Aligning APK ==="
"$ZIPALIGN" -f -v 4 "$BUILD_DIR/unsigned.apk" "$BUILD_DIR/aligned.apk"

echo "=== Generating debug keystore ==="
# Keystore lives next to build.sh (not inside build/) so signature stays
# consistent across builds — Android won't allow installing an update signed
# with a different key than the originally installed APK.
KEYSTORE="$SRC_DIR/debug.keystore"
if [ ! -f "$KEYSTORE" ]; then
    keytool -genkey -noprompt \
        -keyalg RSA -keysize 2048 \
        -validity 10000 \
        -keystore "$KEYSTORE" \
        -storepass android \
        -keypass android \
        -alias androiddebugkey \
        -dname "CN=Depth Wallpaper,O=Salasar,C=IN" 2>/dev/null
fi

echo "=== Signing APK ==="
"$APKSIGNER" sign \
    --ks "$KEYSTORE" \
    --ks-pass pass:android \
    --key-pass pass:android \
    --ks-key-alias androiddebugkey \
    --out "$OUT_APK" \
    "$BUILD_DIR/aligned.apk"

echo ""
echo "=========================================="
echo "  BUILD SUCCESS!"
echo "  APK: $OUT_APK"
echo "  Size: $(du -sh "$OUT_APK" | cut -f1)"
echo "=========================================="
