package com.example.flutterhub_jetpackcompose.screen.components

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import androidx.compose.ui.Modifier
import com.example.flutterhub_jetpackcompose.R
import com.example.flutterhub_jetpackcompose.screen.components.screen.openTheLink


@Composable
fun WatchInstructions(context: Context, onDismiss: () -> Unit) {
    AlertDialog(
        title = {
            Text(text = "Video Tutorial:", modifier = Modifier.fillMaxWidth())
        },
        text = {
            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    val mediaItem =
                        MediaItem.fromUri("android.resource://${context.packageName}/${R.raw.instruction}")
                    setMediaItem(mediaItem)
                    prepare()
                    playWhenReady = true
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    exoPlayer.release()
                }
            }

            AndroidView(
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = true // Set to false if you don't want user controls
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp) // Adjust height as needed
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openTheLink(
                        context,
                        "https://pl.kotl.in/uEvbtyfH0"
                    )
                }
            ) {
                Text("Confirm")
            }
        },
        onDismissRequest = {
            onDismiss()
        },



        )
}
