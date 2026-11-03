# AI FACE — Web Studio

Mobile-first browser-based live camera and recording studio.

## Implemented
- Front and rear camera capture, live preview, mirror control and resolution selection.
- Optional microphone capture.
- MediaPipe Face Landmarker for live face mesh tracking (GPU with CPU fallback).
- Local reference video import and one-face landmark extraction.
- Experimental live reference-texture mapping: reference-video face texture is warped across the live face mesh.
- Adjustable effect strength, composited canvas preview, recording and WebM download.
- Reference and camera frames are processed in the browser; this page does not upload them.
- Persistent synthetic-media disclosure.

## Important limitations
The live effect is a geometric texture-mapping prototype, **not** a neural face-restoration or photorealistic face-reconstruction model. It does not restore missing facial detail, synthesize unseen angles, or guarantee natural mouth/eye animation. The reference clip is sampled at one frame; large head turns, expression changes, occlusions, lighting differences and mobile GPU limits can cause visible seams or distortion. This has not yet been benchmarked on the TECNO LE6. No voice conversion or third-party calling-app camera integration is included.

The MediaPipe JavaScript package and model are loaded from public CDNs/model hosting, so the first run needs an internet connection. Camera access requires HTTPS or localhost. MediaRecorder output is WebM in supported browsers.

## Run
Serve the `site` folder over HTTPS or localhost. For example:
`python -m http.server 8080`
Then open `http://localhost:8080`.

For Cloudflare Pages, use `site` as the build output directory and leave the build command empty.
