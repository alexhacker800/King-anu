package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.models.Story

@Composable
fun StoriesScreen(
    stories: List<Story>,
    onAddStoryClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    if (stories.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("No active 24-hour stories", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onAddStoryClick) {
                    Text("Add a Story")
                }
            }
        }
        return
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    val currentStory = stories[currentIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("stories_viewer_screen")
    ) {
        // Story Image Preview
        AsyncImage(
            model = currentStory.mediaUrl,
            contentDescription = "Story Media",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Top Navigation Bars & Progress Indicators
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            // Story Progress Bars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                stories.forEachIndexed { index, _ ->
                    LinearProgressIndicator(
                        progress = { if (index <= currentIndex) 1f else 0f },
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(CircleShape),
                        color = Color.White,
                        trackColor = Color.Gray.copy(alpha = 0.5f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Author Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = currentStory.authorAvatar,
                        contentDescription = currentStory.authorName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = currentStory.authorName,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            text = "⏳ Expires in 22h",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.LightGray
                        )
                    }
                }

                IconButton(onClick = onCloseClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Story",
                        tint = Color.White
                    )
                }
            }
        }

        // Left/Right Tap Navigation Overlay
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .noRippleClickable {
                        if (currentIndex > 0) currentIndex--
                    }
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .noRippleClickable {
                        if (currentIndex < stories.size - 1) currentIndex++ else onCloseClick()
                    }
            )
        }

        // Bottom Details Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (currentStory.musicTitle.isNotBlank()) {
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${currentStory.musicTitle} • ${currentStory.musicArtist}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (currentStory.caption.isNotBlank()) {
                Text(
                    text = currentStory.caption,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Viewer Info & Protection Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = CircleShape
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "👁️ ${currentStory.viewsCount} Viewers",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ) {
                    Text(
                        text = "🔒 No Downloads Allowed",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier =
    this.then(
        Modifier.clickable(
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
            indication = null
        ) {
            onClick()
        }
    )
