# AI FACE

Private, Android-first prototype for consent-based, clearly disclosed female face and voice transformation.

## Current implementation
- Native Android app using Kotlin and Jetpack Compose.
- CameraX front-camera preview.
- On-device ML Kit face detection and face-count status.
- Female-only identity workflow.
- Import an authorised MP4/video reference through Android's document picker.
- Validate the selected file: it must contain a video track, be no longer than 30 seconds, and be under 100 MB.
- Copy the reference into app-private internal storage and preview it locally.
- Female voice mode is displayed as a planned feature; no voice-conversion engine is connected.

## Important status
This milestone imports and previews the authorised reference video, and provides camera preview and face detection. It does NOT yet perform face replacement, facial reenactment, voice conversion, or virtual camera/microphone routing into WhatsApp, Telegram, Zoom, or Google Meet.

A normal Android app cannot universally replace another app's camera and microphone. External-call integration on Android 11 is device- and app-dependent and must be tested on the actual handset. Do not root the phone as a first step.

## Import the reference
Run the app, tap **Import / replace reference video**, and select the authorised clip (including 1000426689.mp4) from the Android file picker. The app copies it into private app storage. This feature does not upload the video to a server.

## Build
Requirements: Android Studio, JDK 17, Android SDK 35, Gradle 8.9.

Open this repository in Android Studio, sync Gradle, and run the app on an Android 11+ device. Grant camera permission when prompted.

## Next engineering milestones
1. Benchmark a commercially licensed, mobile-capable facial reenactment model against the handset's CPU/GPU/NPU limits.
2. Implement actual frame transformation and temporal-stability tests using the imported reference.
3. Evaluate a real-time female voice-conversion engine and audio latency.
4. Investigate supported Android call-routing options per target app/device.
5. Add consent confirmation, privacy controls, secure deletion, and visible synthetic-media disclosure.
6. Test performance, thermal behaviour, battery use, and compatibility on the target Android 11 handset.

Never use another person's face or voice without their permission. AI-altered output must remain clearly disclosed.
