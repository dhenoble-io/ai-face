# AI FACE

Private, Android-first prototype for consent-based, clearly disclosed live face and voice transformation.

## Current implementation
- Native Android application using Kotlin and Jetpack Compose.
- CameraX front-camera preview.
- On-device ML Kit face detection and face-count status.
- Fixed Female and Male identity selectors. Reference assets are not included yet.
- Female and Deep Male voice-mode selectors. Voice conversion is not connected yet.
- Visible disclosure that this is an AI transformation prototype.

## Important status
This milestone is a camera and face-detection foundation, NOT a completed face-swap or voice-conversion system. It does not yet alter a face, synthesize a voice, or feed a virtual camera/microphone into WhatsApp, Telegram, Zoom, or Google Meet.

External-call integration on Android 11 is device- and app-dependent. A normal Android app cannot universally replace another app's camera and microphone. Each target app must be tested on the actual handset. Do not root the phone as a first step.

## Build
Requirements: Android Studio, JDK 17, Android SDK 35, Gradle 8.9.

Open this repository in Android Studio, sync Gradle, and run the app on an Android 11+ device. Grant camera permission when prompted.

## Next milestones
1. Add and validate the two authorised reference assets.
2. Evaluate a commercially licensed, mobile-capable facial reenactment model against the handset's CPU/GPU/NPU limits.
3. Implement actual frame transformation and temporal-stability tests.
4. Evaluate a real-time voice-conversion engine and audio latency.
5. Investigate supported Android call-routing options per target app/device.
6. Add consent, privacy, deletion, and visible synthetic-media disclosure controls.

Never use another person's face or voice without their permission. AI-altered output must remain clearly disclosed.
