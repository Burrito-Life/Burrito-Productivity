<!-- Copilot instructions for Burrito-Productivity -->
# Burrito Productivity — Copilot Instructions

Purpose: give AI coding agents immediate, actionable knowledge to be productive in this repository.

- Big picture:
  - Kotlin Multiplatform app (KMP) using Compose Multiplatform. Shared code lives under `composeApp/src/commonMain/kotlin` and is consumed by platform sources in `androidMain`, `iosMain`, and `wasmJsMain`.
  - UI samples and entry points: `composeApp/src/commonMain/kotlin/com/burrito/productivity/App.kt`, `composeApp/src/androidMain/kotlin/com/burrito/productivity/MainActivity.kt`, `composeApp/src/iosMain/kotlin/MainViewController.kt`, and `wasmJsMain/kotlin/main.kt`.
  - Build system: Gradle (wrapper included). Dependency versions are centralized in `gradle/libs.versions.toml`.

- What to change (patterns & conventions):
  - Add shared logic in `composeApp/src/commonMain/kotlin/com/burrito/productivity/*` and platform-specific implementations under `*Main` folders.
  - Use package `com.burrito.productivity` for public/shared symbols.
  - Avoid editing generated resources under `composeApp/build/generated/` — they are produced by resource generators.

- Build / test / run (discoverable commands):
  - Build all targets: `./gradlew build`
  - Run unit tests: `./gradlew test`
  - Use `./gradlew assemble` for assembling artifacts.
  - For Android run/debug, open the project in Android Studio (use the Gradle wrapper). For iOS open the generated Xcode project when needed by the Kotlin/Native plugin.

- Project-specific signals to watch for:
  - `composeApp/src/commonMain/kotlin/com/burrito/productivity/App.kt` shows Compose UI patterns used throughout the app.
  - `composeApp/src/commonMain/kotlin/com/burrito/productivity/util/ABTestService.kt` is a TODO placeholder for feature-flag / experiment infra — follow its TODO comments when implementing A/B or config-as-data features.
  - README describes the higher-level Hub vs Brain architecture. Respect the separation: the Hub (shared Kotlin) coordinates context and tools; models and inference are handled as separate Brain components.

- Integration & dependencies:
  - Native model runtimes and platform-specific AI integrations are external; inspect `gradle/libs.versions.toml` and `build.gradle.kts` for references to ExecuTorch, sqlite-vec, and MCP-related modules.
  - Cross-target communication happens via the common API surface in `commonMain` and by platform adapters placed in `*Main` directories.

- Examples (quick pointers):
  - To add a new shared service: create `composeApp/src/commonMain/kotlin/com/burrito/productivity/service/MyService.kt`.
  - To add Android wiring: edit `composeApp/src/androidMain/kotlin/com/burrito/productivity/MainActivity.kt`.
  - To respect resource generation: add assets in `composeApp/src/commonMain/resources` (or platform resources) and do not hand-edit `build/generated`.

- What NOT to do:
  - Do not change generated files under `build/` or `composeApp/build/generated/`.
  - Do not assume a single-platform runtime — changes should compile for KMP unless explicitly platform-scoped.

If anything here is unclear or you want more examples (tests, sample Gradle tasks, or typical refactors), tell me which area to expand.
