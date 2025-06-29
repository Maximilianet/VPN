# VPN Project

This project uses the Gradle wrapper to manage builds.

## Setup

Before running any Gradle tasks, ensure the wrapper script is executable:

```bash
chmod +x gradlew
```

Then you can build or test the project with:

```bash
./gradlew build
```

## Using a local build of ICS OpenVPN

If access to JitPack is unavailable, you can compile the OpenVPN library from a local clone. Clone the upstream project next to `app` and `gradle`:

```bash
git clone --branch v0.7.61 https://github.com/schwabe/ics-openvpn.git openvpn
```

Edit `openvpn/main/build.gradle.kts` and replace the `com.android.application` plugin with `com.android.library`. Optionally apply the `maven-publish` plugin and add a `publishing` block as described in the project guide.

Build the archive:

```bash
cd openvpn
./gradlew :main:assembleRelease
```

Copy the generated `main-release.aar` to `app/libs/openvpn-v0.7.61.aar` and add the dependency in `app/build.gradle.kts`:

```kotlin
implementation(files("libs/openvpn-v0.7.61.aar"))
```

Gradle will then resolve classes from the locally built AAR.
