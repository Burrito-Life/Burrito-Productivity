---
description: Repository Information Overview
alwaysApply: true
---

# Burrito Productivity Information

## Summary

Burrito Productivity is a modern, **local-first productivity suite** built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**. It provides a unified interface for calendar, tasks, emails, notes (Notion style flexibility for "databases"), and an agentic web browser — all powered by on-device Edge AI inference and customizable agents, and all with privacy-first P2P synchronization. The architecture follows a **Hub & Brain** separation: the Hub (shared Kotlin) coordinates context and tools via MCP; the Brain consists of specialized models running locally on device NPUs or WebGPU.

What's here now is just the very early beginning of what we're building. We'll be adding more features over time, including support for additional platforms (like smart watches and other wearables), as well as expanding our capabilities beyond just productivity apps into areas such as personal finance, health tracking, and even entertainment and more. Our goal is to create a comprehensive suite that helps you manage your life from start to finish with the power of AI, while keeping your data secure and private.

## Structure

**Single-project repository** using a monolithic Kotlin Multiplatform structure:

- **composeApp/**: Main KMP application module
  - `src/commonMain/`: Shared Kotlin code (UI, data, services, utilities)
  - `src/androidMain/`: Android-specific implementations
  - `src/iosMain/`: iOS-specific implementations (Xcode framework generation)
  - `src/wasmJsMain/`: WebAssembly/JavaScript target
- **gradle/**: Gradle configuration and dependency versions
- **iosApp/**: iOS project workspace (Xcode integration)

**Main components** in `composeApp/src/commonMain/kotlin/com/burrito/productivity/`:

- **UI**: Compose Multiplatform UI layer with material design
- **Data**: Local storage layer with SQLite, `sqlite-vec`, settings, and RAG context
- **MCP**: Tool registry for Model Context Protocol integrations
- **Utilities**: A/B testing service, logging, helper functions

## Language & Runtime

**Language**: Kotlin  
**Version**: 2.1.0  
**JVM Target**: Java 11  
**Build System**: Gradle (with wrapper: `./gradlew`)  
**Package Manager**: Gradle with centralized version catalog

### Platform Targets

| Target | Min Version | Configuration |
|--------|-------------|---|
| Android | SDK 24 (API level) | Compile SDK 34, Target SDK 34 |
| iOS | N/A | x64, arm64, simulator architectures; generates `ComposeApp.framework` |
| Web | Chrome 121+ (WebGPU) | WASM/JS target; outputs `composeApp.js` |

## Dependencies

**Compose & UI** (commonMain):

- `compose.runtime`, `compose.foundation`, `compose.material`, `compose.ui`
- `compose.components.resources`, `compose.components.uiToolingPreview`

**AndroidX** (platform-specific):

- `androidx.activity:activity-compose` (1.9.3)
- `androidx.appcompat:appcompat` (1.7.0)
- `androidx.core:core-ktx` (1.15.0)
- `androidx.material:material` (1.12.0)
- `androidx.constraintlayout:constraintlayout` (2.2.0)
- `androidx.lifecycle:lifecycle-viewmodel`, `lifecycle-runtime-compose` (2.8.4)

**Testing** (commonMain):

- `org.jetbrains.kotlin:kotlin-test`, `kotlin-test-junit`
- `junit:junit` (4.13.2)
- `androidx.test.ext:junit` (1.2.1)
- `androidx.test.espresso:espresso-core` (3.6.1)

**Build Plugins**:

- Kotlin Multiplatform (2.1.0)
- Jetbrains Compose (1.7.1)
- Android Gradle Plugin (8.13.2)
- Kotlin Compose Compiler (2.1.0)

## Build & Installation

```bash
# Build all targets
./gradlew build

# Run unit tests
./gradlew test

# Assemble artifacts
./gradlew assemble

# Android: Assemble APK/AAB
./gradlew assembleDebug
./gradlew assembleRelease

# iOS: Generate Xcode framework
./gradlew iosArm64Binaries
./gradlew iosSimulatorArm64Binaries

# Web: Build WASM/JS bundle
./gradlew wasmJsBrowserDistribution
```

## Main Files & Entry Points

**Shared UI Entry**:

- `composeApp/src/commonMain/kotlin/com/burrito/productivity/App.kt` — Main Compose UI component

**Platform Entry Points**:

- Android: `composeApp/src/androidMain/kotlin/com/burrito/productivity/MainActivity.kt`
- iOS: `composeApp/src/iosMain/kotlin/MainViewController.kt`
- Web: `composeApp/src/wasmJsMain/kotlin/main.kt`

**Key Services & Data**:

- `com/burrito/productivity/data/local/DbService.kt` — SQLite/local storage layer
- `com/burrito/productivity/data/local/CoreSettings.kt`, `UserPersonaSettings.kt` — Settings management
- `com/burrito/productivity/data/mcp/McpToolRegistry.kt` — MCP tool registration
- `com/burrito/productivity/util/ABTestService.kt` — A/B testing framework (TODO placeholder)

**Configuration**:

- `gradle/libs.versions.toml` — Centralized dependency versions
- `gradle.properties` — Gradle JVM and build options
- `composeApp/build.gradle.kts` — Main module build configuration
- `settings.gradle.kts` — Project structure (`include(":composeApp")`)

## Testing

**Framework**: Kotlin Test + JUnit 4  
**Artifacts**: `kotlin-test`, `kotlin-test-junit`, `junit`, AndroidX Test extensions

**Run Tests**:

```bash
./gradlew test
```

Test dependencies are configured but no explicit test files were found; tests can be added under `composeApp/src/commonTest/kotlin/` or platform-specific `*Test/` directories.

## Gradle Configuration

**JVM Options** (gradle.properties):

- Max heap: 2048M
- Parallel builds enabled
- Build caching enabled
- Configuration cache enabled
- Code style: official Kotlin style

**Feature Previews**:

- `TYPESAFE_PROJECT_ACCESSORS` — Type-safe access to projects
