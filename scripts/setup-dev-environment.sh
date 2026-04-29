#!/usr/bin/env bash
set -e

echo "Checking development environment for android-Gravity-Path..."

fail() { echo "ERROR: $1" >&2; exit 1; }

# Java
echo -n "Java: "; if command -v java >/dev/null 2>&1; then java -version 2>&1 | head -n1; else echo "missing"; fi

# Gradle wrapper
if [ -x ./gradlew ]; then
  echo "Gradle wrapper: OK"
else
  echo "Gradle wrapper missing or not executable (./gradlew)"
fi

# Android SDK
if [ -n "$ANDROID_SDK_ROOT" ] || [ -n "$ANDROID_HOME" ]; then
  echo "ANDROID_SDK_ROOT = ${ANDROID_SDK_ROOT:-$ANDROID_HOME}"
else
  echo "ANDROID_SDK_ROOT / ANDROID_HOME not set"
fi

if command -v sdkmanager >/dev/null 2>&1; then
  echo "sdkmanager: $(sdkmanager --version 2>/dev/null || echo 'unknown')"
else
  echo "sdkmanager: not found"
fi

# Git
echo -n "git: "; git --version 2>/dev/null || echo "missing"

echo "\nQuick checks finished. Follow README.md for next steps to fully install dependencies."

echo "You can run: ./gradlew clean assembleDebug to verify the project builds once prerequisites are satisfied."

exit 0

