package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadPostScreen(
    onPostCreated: (caption: String, hashtags: String, mediaUrl: String, isVideo: Boolean, privacy: String) -> Unit,
    onStoryCreated: (mediaUrl: String, caption: String, musicTitle: String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Post, 1 = Reel Video, 2 = Story
    var caption by remember { mutableStateOf("") }
    var hashtags by remember { mutableStateOf("#LoveNest #Lovers") }
    var mediaUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=800") }
    var musicTitle by remember { mutableStateOf("") }
    var selectedPrivacy by remember { mutableStateOf("Partner Only") }
    var compressVideo by remember { mutableStateOf(true) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableFloatStateOf(0f) }

    val privacyOptions = listOf("Public", "Partner Only", "Private")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tab Selector
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
            ) {
                Text("Post")
            }
            SegmentedButton(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
            ) {
                Text("Reel Video")
            }
            SegmentedButton(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
            ) {
                Text("Story")
            }
        }

        // Media Preview Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = mediaUrl,
                    contentDescription = "Media Preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (selectedTab == 1) Icons.Default.Videocam else Icons.Default.Photo,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (selectedTab == 1) "Video Preview (Thumbnail Selected)" else "Photo Media Selected",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Quick Preset Media Pickers
        Text("Sample Media Assets:", style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = mediaUrl == "https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=800",
                onClick = { mediaUrl = "https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=800" },
                label = { Text("Romantic Couple") }
            )
            FilterChip(
                selected = mediaUrl == "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800",
                onClick = { mediaUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800" },
                label = { Text("Nature Scenic") }
            )
        }

        // Caption Input
        OutlinedTextField(
            value = caption,
            onValueChange = { caption = it },
            label = { Text("Write a caption, emojis, or love note...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .testTag("upload_caption_input"),
            maxLines = 4
        )

        if (selectedTab != 2) {
            // Hashtags Input
            OutlinedTextField(
                value = hashtags,
                onValueChange = { hashtags = it },
                label = { Text("Hashtags") },
                modifier = Modifier.fillMaxWidth()
            )

            // Privacy Settings
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Privacy Control", style = MaterialTheme.typography.titleSmall)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        privacyOptions.forEach { option ->
                            FilterChip(
                                selected = selectedPrivacy == option,
                                onClick = { selectedPrivacy = option },
                                label = { Text(option) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = when (option) {
                                            "Public" -> Icons.Default.Public
                                            "Partner Only" -> Icons.Default.Favorite
                                            else -> Icons.Default.Lock
                                        },
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
            }
        } else {
            // Story Music Link
            OutlinedTextField(
                value = musicTitle,
                onValueChange = { musicTitle = it },
                label = { Text("Add Song Sticker (e.g. Kesariya - Arijit Singh)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Video Compression Switch (For Videos)
        if (selectedTab == 1) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Compress Video Before Upload", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Reduces 15MB video down to 3.2MB for smooth play on 4G/5G mobile networks.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = compressVideo,
                        onCheckedChange = { compressVideo = it }
                    )
                }
            }
        }

        // Uploading Progress
        AnimatedVisibility(visible = isUploading) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Uploading to Secure Cloud...", style = MaterialTheme.typography.labelSmall)
                    Text("${(uploadProgress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { uploadProgress },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Submit Button
        Button(
            onClick = {
                isUploading = true
                uploadProgress = 0.4f
                if (selectedTab == 2) {
                    onStoryCreated(mediaUrl, caption, musicTitle)
                } else {
                    onPostCreated(caption, hashtags, mediaUrl, selectedTab == 1, selectedPrivacy)
                }
                uploadProgress = 1.0f
                isUploading = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("submit_post_button"),
            enabled = !isUploading
        ) {
            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when (selectedTab) {
                    0 -> "Share Post"
                    1 -> "Publish Video Reel"
                    else -> "Post Story (24h)"
                }
            )
        }
    }
}
