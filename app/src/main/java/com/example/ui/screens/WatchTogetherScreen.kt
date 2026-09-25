package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.models.ChatMessage
import com.example.data.models.CoupleInfo
import com.example.data.models.WatchMovieItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchTogetherScreen(
    movies: List<WatchMovieItem>,
    chatMessages: List<ChatMessage>,
    coupleInfo: CoupleInfo,
    onSendMessage: (String) -> Unit,
    onAddMovie: (String, String, String, String, String, String) -> Unit
) {
    var selectedMovie by remember(movies) {
        mutableStateOf(movies.firstOrNull() ?: WatchMovieItem(
            id = 0,
            title = "Romantic Sunset Walk in Paris 🌅",
            category = "Romance / Travel",
            duration = "1h 42m",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=800",
            description = "Experience the romantic streets of Paris under golden sunset lighting together.",
            addedBy = "Aarav & Ananya"
        ))
    }

    var isPlaying by remember { mutableStateOf(true) }
    var currentProgress by remember { mutableFloatStateOf(0.24f) }
    var liveReactionEmoji by remember { mutableStateOf<String?>(null) }
    var chatInputText by remember { mutableStateOf("") }
    var showAddMovieDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // Automatic progress simulation when playing
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(1000)
            currentProgress = (currentProgress + 0.005f).coerceAtMost(1.0f)
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddMovieDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Movie") },
                text = { Text("Add Custom Video 🎬") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.testTag("add_movie_fab")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Main Movie Video Player Box
            Card(
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = selectedMovie.thumbnailUrl,
                        contentDescription = selectedMovie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )

                    // Dark Gradient Overlay for controls visibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.5f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )

                    // Sync Status Badge
                    Surface(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (isPlaying) Color(0xFF4CAF50) else Color(0xFFFF9800))
                            )
                            Text(
                                text = if (isPlaying) "🟢 In Sync with ${coupleInfo.partner2Name}" else "⏸️ Paused together",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                        }
                    }

                    // Center Play/Pause Touch Overlay
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .testTag("movie_play_pause_btn")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // Floating Reaction Animation Badge
                    if (liveReactionEmoji != null) {
                        Surface(
                            color = Color.Magenta.copy(alpha = 0.85f),
                            shape = CircleShape,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 24.dp)
                        ) {
                            Text(
                                text = liveReactionEmoji!!,
                                fontSize = 36.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    // Bottom Player Progress Bar & Controls
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedMovie.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${(currentProgress * 100).toInt()}% • ${selectedMovie.duration}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.LightGray
                            )
                        }

                        Slider(
                            value = currentProgress,
                            onValueChange = {
                                currentProgress = it
                                isPlaying = true
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Quick Couples Live Reaction Bar
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val reactions = listOf("❤️ Love", "💋 Kiss", "🍿 Popcorn", "🌹 Rose", "😭 Cry", "🤗 Hug")
                    reactions.forEach { reaction ->
                        AssistChip(
                            onClick = {
                                val emoji = reaction.split(" ").firstOrNull() ?: "❤️"
                                liveReactionEmoji = emoji
                                onSendMessage("Sent movie reaction $emoji while watching ${selectedMovie.title}")
                                scope.launch {
                                    delay(2000)
                                    liveReactionEmoji = null
                                }
                            },
                            label = { Text(reaction, fontSize = 12.sp) }
                        )
                    }
                }
            }

            // Tabs / Section: Watch Movies List & Side Chat
            var selectedTab by remember { mutableIntStateOf(0) }
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Movie Playlist 🍿") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Couple Side Chat 💬") }
                )
            }

            if (selectedTab == 0) {
                // Movie Playlist Catalog
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(movies, key = { it.id }) { movie ->
                        val isCurrentMovie = movie.id == selectedMovie.id
                        Card(
                            onClick = {
                                selectedMovie = movie
                                currentProgress = 0.0f
                                isPlaying = true
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCurrentMovie) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                AsyncImage(
                                    model = movie.thumbnailUrl,
                                    contentDescription = movie.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(80.dp, 60.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = movie.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${movie.category} • ${movie.duration}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = movie.description,
                                        style = MaterialTheme.typography.labelSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                if (isCurrentMovie) {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircle,
                                        contentDescription = "Playing Now",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Couple Side Chat during Movie Watching
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(chatMessages.takeLast(10)) { msg ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (msg.isFromMe) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .align(if (msg.isFromMe) Alignment.End else Alignment.Start)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = msg.senderName,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = msg.messageText,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = chatInputText,
                            onValueChange = { chatInputText = it },
                            placeholder = { Text("Comment while watching...") },
                            modifier = Modifier.weight(1f),
                            shape = CircleShape
                        )
                        IconButton(
                            onClick = {
                                if (chatInputText.isNotBlank()) {
                                    onSendMessage(chatInputText)
                                    chatInputText = ""
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send Comment",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddMovieDialog) {
        AddMovieModal(
            onDismiss = { showAddMovieDialog = false },
            onMovieAdded = { title, cat, dur, url, thumb, desc ->
                onAddMovie(title, cat, dur, url, thumb, desc)
                showAddMovieDialog = false
            }
        )
    }
}

@Composable
fun AddMovieModal(
    onDismiss: () -> Unit,
    onMovieAdded: (String, String, String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Romantic Movie") }
    var duration by remember { mutableStateOf("1h 30m") }
    var videoUrl by remember { mutableStateOf("") }
    var thumbnailUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Movie to Watch Together 🎬") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Movie / Video Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = videoUrl,
                    onValueChange = { videoUrl = it },
                    label = { Text("Stream URL / MP4 Link") },
                    placeholder = { Text("https://example.com/movie.mp4") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = thumbnailUrl,
                    onValueChange = { thumbnailUrl = it },
                    label = { Text("Cover Image URL") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onMovieAdded(
                            title, category, duration, videoUrl, thumbnailUrl, description
                        )
                    }
                }
            ) {
                Text("Add to Movie Night")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
