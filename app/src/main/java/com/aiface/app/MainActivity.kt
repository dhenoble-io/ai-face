package com.aiface.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

private val AppBackground = Color(0xFF090B12)
private val Panel = Color(0xFF141925)
private val Accent = Color(0xFF9B8CFF)
private val Muted = Color(0xFF9AA4B5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Accent, background = AppBackground, surface = Panel,
                onBackground = Color.White, onSurface = Color.White
            )) { AiFaceScreen() }
        }
    }
}

@Composable
private fun AiFaceScreen() {
    var cameraEnabled by remember { mutableStateOf(false) }
    var faceCount by remember { mutableIntStateOf(0) }
    var cameraError by remember { mutableStateOf<String?>(null) }

    val context = androidx.compose.ui.platform.LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    val cameraPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasCameraPermission = granted
        cameraEnabled = granted
        cameraError = if (granted) null else "Camera permission was not granted."
    }

    Surface(modifier = Modifier.fillMaxSize(), color = AppBackground) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(42.dp).background(Accent.copy(alpha = 0.16f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Rounded.Face, contentDescription = null, tint = Accent) }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text("AI FACE", fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Female transformation studio", color = Muted, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(Modifier.weight(1f))
                Icon(Icons.Rounded.Security, contentDescription = "Privacy", tint = Color(0xFF78D6AD))
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Your female reference", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("One fixed female identity, used only with the reference owner's permission.", color = Muted)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF24213B)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(48.dp).background(Accent.copy(alpha = 0.16f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Rounded.Face, contentDescription = null, tint = Accent, modifier = Modifier.size(28.dp)) }
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text("Female identity", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text("Reference video received • permission confirmed by user", color = Muted, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Live camera", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.weight(1f))
                    Text(if (cameraEnabled) "CAMERA ON" else "CAMERA OFF",
                        color = if (cameraEnabled) Color(0xFF78D6AD) else Muted,
                        style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }

                if (cameraEnabled && hasCameraPermission) {
                    CameraFacePreview(
                        modifier = Modifier.fillMaxWidth().height(270.dp),
                        onFaceCount = { faceCount = it },
                        onError = { cameraError = it }
                    )
                    Text(if (faceCount > 0) "$faceCount face detected" else "Looking for a face…",
                        color = Muted, style = MaterialTheme.typography.bodySmall)
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(210.dp)
                            .background(Panel, RoundedCornerShape(22.dp))
                            .border(1.dp, Color(0xFF293246), RoundedCornerShape(22.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Rounded.CameraAlt, contentDescription = null, tint = Accent, modifier = Modifier.size(34.dp))
                            Text("Camera preview", color = Color.White, fontWeight = FontWeight.Medium)
                            Text("Turn on your camera to begin face detection.", color = Muted, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                cameraError?.let { Text(it, color = Color(0xFFFF9B9B), style = MaterialTheme.typography.bodySmall) }

                Button(
                    onClick = {
                        if (cameraEnabled) {
                            cameraEnabled = false
                            faceCount = 0
                        } else if (hasCameraPermission) {
                            cameraEnabled = true
                        } else {
                            cameraPermission.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Color(0xFF11111A))
                ) {
                    Icon(Icons.Rounded.CameraAlt, contentDescription = null)
                    Text(if (cameraEnabled) "Stop camera" else "Enable camera", modifier = Modifier.padding(start = 8.dp))
                }
            }

            Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Mic, contentDescription = null, tint = Accent)
                        Text("Voice mode", modifier = Modifier.padding(start = 9.dp), fontWeight = FontWeight.SemiBold)
                    }
                    Text("Female voice", color = Color.White, fontWeight = FontWeight.Medium)
                    Text("Female voice conversion is not connected yet.", color = Muted, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.weight(1f))
            Text(
                "FEMALE MODE ONLY • This build currently provides camera preview and face detection. Face replacement and voice conversion are not connected. Synthetic media must be disclosed.",
                color = Muted, style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
