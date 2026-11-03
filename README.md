# AI FACE

AI FACE is an Android-first, clearly disclosed camera prototype. This test build lets you import a reference clip locally, preview it, open the front camera, and verify on-device face detection.

## Included in this test build
- Kotlin + Jetpack Compose Android app.
- CameraX front-camera preview.
- On-device ML Kit face detection and face-count status.
- Import and replace a video using Android's document picker.
- Validate imported videos: video track required, maximum 30 seconds, maximum 100 MB.
- Store the imported clip in app-private storage and preview it silently in a loop.
- Persistent **AI-ALTERED PREVIEW • SYNTHETIC MEDIA** disclosure over the live camera preview.
- GitHub Actions builds a debug APK and publishes it as the `ai-face-debug-apk` workflow artifact for 14 days.

## Install and test
1. Open the **Actions** tab in this repository: https://github.com/dhenoble-io/ai-face/actions
2. Open the newest successful **Android build** run.
3. Under **Artifacts**, download `ai-face-debug-apk`.
4. Extract the ZIP and install `app-debug.apk` on the TECNO LE6 (Android 11+).
5. Allow camera permission.
6. Test **Enable camera**, face detection, **Import / replace reference video**, and reference playback.

Android may ask you to allow installation from the browser or file manager used to open the APK. Only install a build from this repository.

## What this build does not do
This is a functional camera/reference-video test app, not a completed face/voice transformation product. It does **not** transfer a real person's identity onto the live camera, perform voice conversion, record or stream altered output, or replace the camera/microphone inside WhatsApp, Telegram, Zoom, or Google Meet.

The reference clip is only imported and previewed; it is not used for identity transfer. A future avatar/filter feature should use a licensed fictional identity and keep synthetic-media disclosure visible. Do not use AI output to mislead call participants about who they are seeing or hearing.

## Device compatibility
The project uses Android API 30 as its minimum (Android 11). Camera performance, heat, memory use, and battery impact still need to be tested on the actual TECNO LE6. A successful CI build does not guarantee device compatibility.

## Privacy
- The selected clip is copied to app-private internal storage.
- This app build does not upload the selected clip.
- The app requests camera permission only; microphone permission is not needed by the implemented features.
- Uninstalling the app removes its private app data.

## Build locally
Requirements: Android Studio, JDK 17, Android SDK 35, Gradle 8.9. Open the repository in Android Studio, sync Gradle, and run the app on an Android 11+ device.
