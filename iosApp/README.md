# iOS App

This directory should contain the Xcode project.
Since this project was generated via CLI, you need to set up the Xcode project manually or use the KMP Wizard to generate one.

The Kotlin framework will be generated in `composeApp/build/bin/iosArm64/podDebugFramework` (or similar depending on configuration).
To integrate:
1. Create a new iOS App in Xcode.
2. Add the framework search path to the build settings.
3. Import the `ComposeApp` framework.
