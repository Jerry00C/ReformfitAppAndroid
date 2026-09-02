# Reformfit App Android

Android application for a fitness and wellness booking platform. The app integrates with Mindbody for class schedules, client data, and purchases, while also using Firebase services for authentication, analytics, and data storage.

## Overview

This project is a single-module Android app built with Java and AndroidX. It includes:

- location and service landing screens
- class browsing and schedule views
- fitness service information pages
- member profile and mine screens
- purchase and checkout flows
- support for blog/news content and health calculators
- Mindbody API integration for classes and client data

## Tech Stack

- Android SDK 30
- Java
- AndroidX / Material Components
- Firebase Authentication, Firestore, Realtime Database, Analytics
- Volley for HTTP networking
- Glide for image loading
- LiveChat SDK

## Project Structure

```text
.
├── app/
│   ├── build.gradle
│   ├── google-services.json
│   ├── proguard-rules.pro
│   └── src/
│       ├── androidTest/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/reformfitapp/
│       │   └── res/
│       └── test/
├── build.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── README.md
```

### Main package areas

- `com.example.reformfitapp` — app screens, fragments, navigation, and core app classes
- `com.example.reformfitapp.purchaseFragment` — purchase-related screens and checkout logic
- `com.example.reformfitapp.serviceInfo` — service detail pages
- `com.example.reformfitapp.expandedFunc` — health calculators, reports, and blog/news content
- `com.example.reformfitapp.main` / `ui.main` — tab and pager components
- `com.example.reformfitapp.mine` — member profile-related features

## Key Features

### 1. Home and service navigation
The main app flow is centered around `MainBottomNaviService`, which hosts bottom navigation for:

- location
- classes
- purchase
- video

### 2. Mindbody integration
The app uses a custom Mindbody client layer to:

- fetch user tokens
- retrieve class schedules and service data
- load client information
- handle booking-related actions such as adding/removing clients from classes and waitlists

Relevant classes include:

- `MindbodyClass`
- `MindbodyClient`
- `MindbodyLocation`
- `MindbodyClassModel`
- `MindbodyVisitHistory`
- `MindbodyAddClientToClass`
- `MindbodyRemoveClientFromClass`

### 3. Booking and purchase flow
Purchase-related screens and logic are under the `purchaseFragment` package. The app includes flows for:

- memberships
- passes
- private lessons
- class purchase screens
- payment method and payment history handling

### 4. Fitness and wellness content
The app contains informational screens and calculators for:

- BMI
- BMR
- TDEE
- diet and health content
- blog/news articles
- training/service information

## Prerequisites

Before building the project, make sure you have:

- Android Studio
- JDK 8 or compatible version
- Android SDK with platform 30
- Gradle wrapper included in the repo

## Setup

1. Open the project in Android Studio.
2. Let Gradle sync the dependencies.
3. Ensure `google-services.json` is present in `app/` for Firebase configuration.
4. Check the Android SDK and build tools match the project version.

## Build

From the project root:

```bash
./gradlew assembleDebug
```

Or use Android Studio's standard build/run flow.

## Run

Use Android Studio to launch the app on an emulator or physical Android device.

## Notes

- The project uses older Android dependencies and an older Gradle/AGP configuration, so compatibility issues may appear if run with newer tooling without adjustments.
- Some packages and classes appear to be legacy implementations and may require cleanup or modernization.
- The app contains hardcoded Mindbody API credentials and configuration values in several service classes.

## License

This project does not include a license file. Please confirm the intended licensing terms with the project owner before publishing or redistributing the code.
