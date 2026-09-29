package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.models.Song
import com.example.ui.components.FileDownloader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicSharingScreen(
    songs: List<Song>,
    onToggleFavorite: (Long) -> Unit,
    onAddSong: (title: String, artist: String, coverUrl: String, audioUrl: String) -> Unit,
    onSendSongToChat: (Song) -> Unit
) {
    var currentlyPlayingSong by remember { mutableStateOf<Song?>(songs.firstOrNull()) }
    var isPlaying by remember { mutableStateOf(false) }
    var progressPosition by remember { mutableFloatStateOf(0.35f) }
    var showAddSongModal by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Player Banner
        currentlyPlayingSong?.let { song ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("music_player_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = song.albumCover,
                            contentDescription = song.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(MaterialTheme.shapes.medium)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = song.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = song.artist,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.extraSmall,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = "Legally Streamed • Spotify/Apple Music Link",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Player Seek Slider
                    Slider(
                        value = progressPosition,
                        onValueChange = { progressPosition = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("1:14", style = MaterialTheme.typography.labelSmall)
                        Text("3:30", style = MaterialTheme.typography.labelSmall)
                    }

                    // Player Control Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onToggleFavorite(song.id) }) {
                            Icon(
                                imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (song.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "Previous")
                        }

                        FloatingActionButton(
                            onClick = { isPlaying = !isPlaying },
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play Pause"
                            )
                        }

                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.SkipNext, contentDescription = "Next")
                        }

                        IconButton(onClick = { onSendSongToChat(song) }) {
                            Icon(imageVector = Icons.Outlined.Send, contentDescription = "Send to Chat")
                        }
                    }
                }
            }
        }

        // Shared Playlist Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Shared Couple Playlist 🎵",
                style = MaterialTheme.typography.titleLarge
            )
            Button(onClick = { showAddSongModal = true }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Song")
            }
        }

        // Songs List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(songs, key = { it.id }) { song ->
                SongRowItem(
                    song = song,
                    isCurrent = song.id == currentlyPlayingSong?.id,
                    onSelect = {
                        currentlyPlayingSong = song
                        isPlaying = true
                    },
                    onSendToChat = { onSendSongToChat(song) },
                    onFavoriteToggle = { onToggleFavorite(song.id) }
                )
            }
        }
    }

    // Add Song Modal
    if (showAddSongModal) {
        var title by remember { mutableStateOf("") }
        var artist by remember { mutableStateOf("") }
        var coverUrl by remember { mutableStateOf("") }
        var audioUrl by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddSongModal = false },
            title = { Text("Add Song to Shared Jukebox") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Song Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = artist,
                        onValueChange = { artist = it },
                        label = { Text("Artist Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = coverUrl,
                        onValueChange = { coverUrl = it },
                        label = { Text("Album Artwork URL (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = audioUrl,
                        onValueChange = { audioUrl = it },
                        label = { Text("Audio MP3 / Stream Link (Legal)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (title.isNotBlank() && artist.isNotBlank()) {
                        onAddSong(title, artist, coverUrl, audioUrl)
                        showAddSongModal = false
                    }
                }) {
                    Text("Add to Playlist")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSongModal = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SongRowItem(
    song: Song,
    isCurrent: Boolean,
    onSelect: () -> Unit,
    onSendToChat: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = song.albumCover,
                    contentDescription = song.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.small)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = song.title, style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = "${song.artist} • Added by ${song.addedBy}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row {
                IconButton(onClick = onFavoriteToggle) {
                    Icon(
                        imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (song.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = {
                        FileDownloader.downloadFile(
                            context = context,
                            url = song.audioUrl,
                            title = song.title,
                            category = "Voice Secrets"
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download Song",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onSendToChat) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Send to Chat"
                    )
                }
            }
        }
    }
}
