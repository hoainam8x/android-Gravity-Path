# android-Gravity-Path — Setup & Development

This repository contains the Android project "Gravity Path". This README documents a reproducible developer setup so you (or another machine) can get a working build environment without guessing configuration.

Checklist (to run on a fresh macOS machine)

- [ ] Install a JDK (recommended Temurin 17 or 17+)
- [ ] Install Android SDK / Android Studio or command-line SDK tools
- [ ] Set `JAVA_HOME` and `ANDROID_SDK_ROOT` / `ANDROID_HOME`
- [ ] Add `platform-tools` to `PATH`
- [ ] Install Git and set up SSH or use `gh auth login --web`
- [ ] Build with the included Gradle wrapper

Quick start (copy/paste commands)

1) Install Homebrew (if you use it)
```bash
# Install Homebrew (if you don't have it)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

2) Install a JDK (example using brew and Temurin)
```bash
# Example: Temurin 17 (adjust if your project requires another Java major version)
brew install --cask temurin17
export JAVA_HOME=$(/usr/libexec/java_home -v17)
export PATH="$JAVA_HOME/bin:$PATH"
java -version
```

3) Install Android SDK / command-line tools
 - Best: install Android Studio and open SDK Manager to install SDK Platforms and `Android SDK Command-line Tools`.
 - CLI alternative (if `sdkmanager` is available):
```bash
# Example: install platform-tools and a platform (adjust API level as needed)
# sdkmanager is part of Android command-line tools
sdkmanager --install "platform-tools" "platforms;android-33" --sdk_root="$ANDROID_SDK_ROOT"
```

4) Environment variables (add to `~/.zshrc`)
```bash
export ANDROID_SDK_ROOT="$HOME/Library/Android/sdk"
export PATH="$ANDROID_SDK_ROOT/platform-tools:$PATH"
export JAVA_HOME=$(/usr/libexec/java_home -v17)
```

5) Authenticate with GitHub (recommended workflows)

- SSH key (recommended for dev machines):
  - `ssh-keygen -t ed25519 -C "you@example.com"` then add `~/.ssh/id_ed25519.pub` to GitHub SSH keys
- Or GitHub CLI (browser auth):
  - `brew install gh` then `gh auth login --web`

6) Build with Gradle wrapper (no system Gradle required)
```bash
./gradlew clean assembleDebug
```

Notes
- The repository includes a `.gitignore` to avoid committing local build artifacts and IDE files.
- If you moved machines, copy your `local.properties` (Android SDK path) or set `ANDROID_SDK_ROOT`.
- If you see an aapt2 parsing error referencing a `ParsedResource` instance, ensure resource XML/JSON encoding is UTF-8 without BOM and avoid hidden control characters.

Helpful scripts
- `scripts/setup-dev-environment.sh` — a small check helper you can run to verify prerequisites.

If you'd like, I can also add a GitHub Actions workflow that runs `./gradlew assembleDebug` on push to `main` to verify CI builds automatically. Ask and I will add it.

