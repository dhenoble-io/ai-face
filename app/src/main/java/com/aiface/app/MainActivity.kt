package com.aiface.app

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

private val AppBackground = Color(0xFF090B12)
private val Panel = Color(0xFF141925)
private val Accent = Color(0xFF9B8CFF)
private val Muted = Color(0xFF9AA4B5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(primary = Accent, background = AppBackground, surface = Panel)) {
                AiFaceScreen()
            }
        }
    }
}

@Composable
private fun AiFaceScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var cameraEnabled by remember { mutableStateOf(false) }
    var faceCount by remember { mutableIntStateOf(0) }
    var cameraError by remember { mutableStateOf<String?>(null) }
    var importError by remember { mutableStateOf<String?>(null) }
    var importing by remember { mutableStateOf(false) }
    var referenceFile by remember {
        mutableStateOf(File(context.filesDir, "female_reference.mp4").takeIf { it.exists() && it.length() > 0L })
    }
    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    val cameraPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasCameraPermission = granted
        cameraEnabled = granted
        cameraError = if (granted) null else "Camera permission was not granted."
    }
    val videoPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            importing = true
            importError = null
            scope.launch {
                val result = withContext(Dispatchers.IO) { saveReferenceVideo(context, uri) }
                importing = false
                result.fold({ referenceFile = it }, { importError = it.message ?: "Video import failed." })
            }
        }
    }

    Surface(Modifier.fillMaxSize(), color = AppBackground) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Face, null, tint = Accent, modifier = Modifier.size(34.dp))
                Column(Modifier.padding(start = 12.dp)) {
                    Text("AI FACE", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Female transformation studio", color = Muted, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(Modifier.weight(1f))
                Icon(Icons.Rounded.Security, "Privacy", tint = Color(0xFF78D6AD))
            }

            Text("Your female reference", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF24213B))) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Authorised female identity video", color = Color.White, fontWeight = FontWeight.SemiBold)
                    if (referenceFile != null) {
                        ReferenceVideoPreview(referenceFile!!, Modifier.fillMaxWidth().height(220.dp))
                        Text("Video saved in this app's private storage.", color = Muted, style = MaterialTheme.typography.bodySmall)
                    } else {
                        Text("Import your reference video to save and preview it on this device.", color = Muted)
                    }
                    Button(
                        onClick = { videoPicker.launch(arrayOf("video/mp4", "video/*")) },
                        enabled = !importing,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Rounded.VideoLibrary, null)
                        Text(if (importing) "Importing…" else "Import / replace reference video", Modifier.padding(start = 8.dp))
                    }
                    importError?.let { Text(it, color = Color(0xFFFF9B9B)) }
                    Text("Video must be 30 seconds or shorter and under 100 MB.", color = Muted, style = MaterialTheme.typography.labelSmall)
                }
            }

            Text("Live camera", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            if (cameraEnabled && hasCameraPermission) {
                CameraFacePreview(
                    Modifier.fillMaxWidth().height(260.dp),
                    onFaceCount = { faceCount = it },
                    onError = { cameraError = it }
                )
                Text(if (faceCount > 0) "$faceCount face detected" else "Looking for a face…", color = Muted)
            } else {
                Box(Modifier.fillMaxWidth().height(170.dp).background(Panel, RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center) {
                    Text("Camera preview is off", color = Muted)
                }
            }
            cameraError?.let { Text(it, color = Color(0xFFFF9B9B)) }
            Button(
                onClick = {
                    if (cameraEnabled) {
                        cameraEnabled = false
                        faceCount = 0
                    } else if (hasCameraPermission) cameraEnabled = true
                    else cameraPermission.launch(Manifest.permission.CAMERA)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Rounded.CameraAlt, null)
                Text(if (cameraEnabled) "Stop camera" else "Enable camera", Modifier.padding(start = 8.dp))
            }

            Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Mic, null, tint = Accent)
                        Text("Female voice mode", Modifier.padding(start = 8.dp), color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                    Text("Voice conversion engine is not connected yet.", color = Muted, style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(
                "FEMALE MODE ONLY • Reference video import, camera preview and face detection are implemented. Face replacement, voice conversion and integration with other calling apps are not implemented yet. Synthetic media must be disclosed and used only with the reference owner's consent.",
                color = Muted, style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun ReferenceVideoPreview(file: File, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            VideoView(ctx).apply {
                setVideoURI(Uri.fromFile(file))
                setOnPreparedListener { player ->
                    player.isLooping = true
                    player.setVolume(0f, 0f)
                    start()
                }
            }
        }
    )
}

private fun saveReferenceVideo(context: android.content.Context, uri: Uri): Result<File> {
    val target = File(context.filesDir, "female_reference.mp4")
    val temp = File(context.filesDir, "female_reference.tmp")
    return try {
        val source = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open the selected video.")
        source.use { input ->
            FileOutputStream(temp).use { output ->
                val buffer = ByteArray(16 * 1024)
                var total = 0L
                while (true) {
                    val count = input.read(buffer)
                    if (count < 0) break
                    total += count
                    require(total <= 100L * 1024L * 1024L) { "Video exceeds the 100 MB limit." }
                    output.write(buffer, 0, count)
                }
            }
        }
        require(temp.length() > 0L) { "Selected video is empty." }
        val metadata = MediaMetadataRetriever()
        try {
            metadata.setDataSource(temp.absolutePath)
            require(metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO) == "yes") { "Selected file has no video track." }
            val duration = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
            require(duration in 1L..30_000L) { "Choose a video that is 30 seconds or shorter." }
        } finally {
            metadata.release()
        }
        if (target.exists()) target.delete()
        check(temp.renameTo(target)) { "Could not save the reference video." }
        Result.success(target)
    } catch (error: Exception) {
        temp.delete()
        Result.failure(error)
    }
}
