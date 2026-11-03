# AI FACE — Web Studio

A mobile-first browser recording site for AI FACE.

## Current working features
- Front-camera and rear-camera capture through browser permissions.
- Live preview, mirror toggle, camera switching and resolution selection.
- Optional microphone capture (original voice; no voice conversion).
- Record, stop and download recordings as WebM.
- Local reference-video preview (max 100 MB); media is not uploaded by this page.
- Persistent synthetic-media notice.

## Honest feature status
This is a live recording foundation, not yet a face-restoration system. The uploaded reference clip is previewed locally but is not currently applied to the live camera. No face swap, facial reconstruction, skin restoration, expression transfer, voice conversion, cloud upload, or third-party call-camera integration is implemented. Do not advertise those functions as working until an appropriate licensed model is integrated and device-tested.

## Run locally
Serve the `site` folder over HTTPS or localhost (camera access is blocked on ordinary insecure HTTP):
`python -m http.server 8080` from the site directory, then open `http://localhost:8080`.

For a public deployment, connect this repository to Cloudflare Pages and set the build output directory to `site`; no build command is required. GitHub Pages can also publish the folder after Pages is enabled for the repository.

## Device notes
Designed responsively for mobile browsers, including Android Chrome. 720p is the recommended starting point on entry-level phones. Recording format depends on browser support (WebM in Chrome).
