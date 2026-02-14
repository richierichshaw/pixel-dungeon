#!/bin/bash
# Download gdx-teavm 1.5.1 and transitive dependencies into local-repo
set -euo pipefail

LOCAL_REPO="/home/user/pixel-dungeon/local-repo"
MAVEN_CENTRAL="https://repo.maven.apache.org/maven2"
TEAVM_REPO="https://teavm.org/maven/repository"

SUCCESS=0
FAILED=0
SKIPPED=0
FAIL_LIST=""

# Download a single file: download_file <group> <artifact> <version> <ext>
download_file() {
    local group="$1"
    local artifact="$2"
    local version="$3"
    local ext="$4"

    local group_path="${group//\.//}"
    local filename="${artifact}-${version}.${ext}"
    local local_dir="${LOCAL_REPO}/${group_path}/${artifact}/${version}"
    local local_file="${local_dir}/${filename}"
    local url_central="${MAVEN_CENTRAL}/${group_path}/${artifact}/${version}/${filename}"
    local url_teavm="${TEAVM_REPO}/${group_path}/${artifact}/${version}/${filename}"

    if [[ -f "$local_file" ]]; then
        echo "  SKIP ${ext}: ${group}:${artifact}:${version} (exists)"
        ((SKIPPED++)) || true
        return 0
    fi

    mkdir -p "$local_dir"

    # Try Maven Central first
    if curl -f -s -S --connect-timeout 10 --max-time 60 -o "$local_file" "$url_central" 2>/dev/null; then
        local size=$(stat -c%s "$local_file" 2>/dev/null || echo "?")
        echo "  OK   ${ext}: ${group}:${artifact}:${version} (${size} bytes) [maven-central]"
        ((SUCCESS++)) || true
        return 0
    fi

    # Try TeaVM repo as fallback
    if curl -f -s -S --connect-timeout 10 --max-time 60 -o "$local_file" "$url_teavm" 2>/dev/null; then
        local size=$(stat -c%s "$local_file" 2>/dev/null || echo "?")
        echo "  OK   ${ext}: ${group}:${artifact}:${version} (${size} bytes) [teavm-repo]"
        ((SUCCESS++)) || true
        return 0
    fi

    # Cleanup empty file on failure
    rm -f "$local_file"
    echo "  FAIL ${ext}: ${group}:${artifact}:${version}"
    ((FAILED++)) || true
    FAIL_LIST="${FAIL_LIST}  ${group}:${artifact}:${version}:${ext}\n"
    return 1
}

# Download artifact: download_artifact <group> <artifact> <version> [pom-only]
download_artifact() {
    local group="$1"
    local artifact="$2"
    local version="$3"
    local pom_only="${4:-no}"

    # Always download POM
    download_file "$group" "$artifact" "$version" "pom" || true

    # Download JAR unless pom-only
    if [[ "$pom_only" != "pom-only" ]]; then
        download_file "$group" "$artifact" "$version" "jar" || true
    fi
}

echo "=========================================="
echo "Downloading gdx-teavm 1.5.1 dependencies"
echo "to: ${LOCAL_REPO}"
echo "=========================================="

echo ""
echo "--- gdx-teavm parent POM ---"
download_artifact "com.github.niclasko" "gdx-teavm" "1.5.1" "pom-only"

echo ""
echo "--- gdx-teavm modules ---"
download_artifact "com.github.niclasko" "backend-web" "1.5.1"
download_artifact "com.github.niclasko" "gdx-freetype-teavm" "1.5.1"
download_artifact "com.github.niclasko" "backend-shared" "1.5.1"
download_artifact "com.github.niclasko" "asset-loader" "1.5.1"

echo ""
echo "--- TeaVM 0.13.0 ---"
download_artifact "org.teavm" "teavm" "0.13.0" "pom-only"
download_artifact "org.teavm" "teavm-tooling" "0.13.0"
download_artifact "org.teavm" "teavm-core" "0.13.0"
download_artifact "org.teavm" "teavm-classlib" "0.13.0"
download_artifact "org.teavm" "teavm-jso" "0.13.0"
download_artifact "org.teavm" "teavm-jso-apis" "0.13.0"
download_artifact "org.teavm" "teavm-jso-impl" "0.13.0"
download_artifact "org.teavm" "teavm-interop" "0.13.0"
download_artifact "org.teavm" "teavm-metaprogramming-api" "0.13.0"
download_artifact "org.teavm" "teavm-platform" "0.13.0"

echo ""
echo "--- Reflections + Javassist ---"
download_artifact "org.reflections" "reflections" "0.10.2"
download_artifact "org.javassist" "javassist" "3.28.0-GA"

echo ""
echo "--- Jetty 11.0.13 ---"
download_artifact "org.eclipse.jetty" "jetty-server" "11.0.13"
download_artifact "org.eclipse.jetty" "jetty-webapp" "11.0.13"
download_artifact "org.eclipse.jetty" "jetty-servlet" "11.0.13"
download_artifact "org.eclipse.jetty" "jetty-http" "11.0.13"
download_artifact "org.eclipse.jetty" "jetty-io" "11.0.13"
download_artifact "org.eclipse.jetty" "jetty-util" "11.0.13"
download_artifact "org.eclipse.jetty" "jetty-security" "11.0.13"
download_artifact "org.eclipse.jetty" "jetty-xml" "11.0.13"
download_artifact "org.eclipse.jetty.toolchain" "jetty-servlet-api" "5.0.2"

echo ""
echo "--- Jakarta Servlet API ---"
download_artifact "jakarta.servlet" "jakarta.servlet-api" "5.0.0"

echo ""
echo "--- SLF4J ---"
download_artifact "org.slf4j" "slf4j-api" "2.0.5"

echo ""
echo "=========================================="
echo "SUMMARY"
echo "=========================================="
echo "  Downloaded: ${SUCCESS}"
echo "  Skipped:    ${SKIPPED}"
echo "  Failed:     ${FAILED}"

if [[ -n "$FAIL_LIST" ]]; then
    echo ""
    echo "  Failed artifacts:"
    echo -e "$FAIL_LIST"
fi

echo ""
echo "Done."
