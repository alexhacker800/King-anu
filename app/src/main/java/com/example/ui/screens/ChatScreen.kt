package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Report
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
import com.example.data.models.ChatMessage
import com.example.data.models.CoupleInfo
import com.example.ui.components.FileDownloader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    coupleInfo: CoupleInfo,
    onSendMessage: (text: String, mediaUrl: String?, isVoice: Boolean) -> Unit,
    onDeleteMessage: (Long) -> Unit
) {
    val context = LocalContext.current
    var textInput by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var showPartnerInfoModal by remember { mutableStateOf(false) }
    var selectedMessageForOption by remember { mutableStateOf<ChatMessage?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Partner Chat Top Header
        Surface(
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.noRippleClickable { showPartnerInfoModal = true }
                ) {
                    Box {
                        AsyncImage(
                            model = coupleInfo.partner2Avatar,
                            contentDescription = coupleInfo.partner2Name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color.Green, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "${coupleInfo.partner2Name} ❤️",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Online • ${coupleInfo.coupleMood}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "E2E Encrypted",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            reverseLayout = false,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatMessageItem(
                    message = msg,
                    onLongClick = { selectedMessageForOption = msg }
                )
            }

            // Typing Indicator
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                ) {
                    Text(
                        text = "Ananya is typing... 💕",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Cupid AI Suggestion Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Cupid AI:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            val suggestions = listOf(
                "Date Tips 💡" to "/ai Plan a perfect sweet date",
                "Love Poem ✍️" to "/ai Write a romantic poem for us",
                "Apologize 🥺" to "/ai Suggest sweet words to apologize"
            )
            suggestions.forEach { (label, promptText) ->
                SuggestionChip(
                    onClick = { textInput = promptText },
                    label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                )
            }
        }

        // Input Bar
        Surface(
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onSendMessage("Sent romantic photo 📸", "https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=600", false)
                }) {
                    Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = "Send Photo")
                }

                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Love message...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .testTag("chat_input_field"),
                    maxLines = 3,
                    shape = CircleShape
                )

                if (textInput.isBlank()) {
                    IconButton(
                        onClick = {
                            isRecordingVoice = !isRecordingVoice
                            if (!isRecordingVoice) {
                                onSendMessage("🎤 Voice Note (0:08)", null, true)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isRecordingVoice) Icons.Default.StopCircle else Icons.Default.Mic,
                            contentDescription = "Voice Note",
                            tint = if (isRecordingVoice) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                onSendMessage(textInput, null, false)
                                textInput = ""
                            }
                        },
                        modifier = Modifier.testTag("send_message_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Message",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    // Partner Info & Security Dialog
    if (showPartnerInfoModal) {
        AlertDialog(
            onDismissRequest = { showPartnerInfoModal = false },
            title = { Text("Couple Connection Security") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Connected Partner: ${coupleInfo.partner2Name}")
                    Text("Love Key: ${coupleInfo.loveKey}")
                    Text("Status: Connected & E2E Encrypted 🔒")
                    Divider()
                    Button(
                        onClick = { showPartnerInfoModal = false },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Disconnect / Block Partner")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPartnerInfoModal = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Options Modal for Selected Message
    selectedMessageForOption?.let { msg ->
        AlertDialog(
            onDismissRequest = { selectedMessageForOption = null },
            title = { Text("Message Options") },
            text = { Text(msg.messageText) },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (msg.mediaUrl != null || msg.songUrl != null) {
                        TextButton(onClick = {
                            val downloadUrl = msg.mediaUrl ?: msg.songUrl ?: ""
                            val downloadTitle = msg.songTitle ?: if (msg.isVoiceNote) "Voice Note" else "Chat Media"
                            val downloadCategory = if (msg.songUrl != null) "Voice Secrets" else if (msg.isVoiceNote) "Voice Secrets" else "Photos"
                            FileDownloader.downloadFile(context, downloadUrl, downloadTitle, downloadCategory)
                            selectedMessageForOption = null
                        }) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Download")
                        }
                    }

                    if (msg.isFromMe) {
                        TextButton(onClick = {
                            onDeleteMessage(msg.id)
                            selectedMessageForOption = null
                        }) {
                            Icon(Icons.Outlined.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete")
                        }
                    } else {
                        TextButton(onClick = {
                            selectedMessageForOption = null
                        }) {
                            Icon(Icons.Outlined.Report, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Report Message")
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onLongClick: () -> Unit
) {
    val alignment = if (message.isFromMe) Alignment.End else Alignment.Start
    val bubbleColor = if (message.isFromMe) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (message.isFromMe) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onLongClick() }
            .testTag("chat_msg_${message.id}"),
        horizontalAlignment = alignment
    ) {
        Surface(
            color = bubbleColor,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!message.songTitle.isNullOrBlank()) {
                    // Song Card in Chat
                    Card(
                        modifier = Modifier
                            .width(220.dp)
                            .padding(bottom = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = message.songCoverUrl,
                                contentDescription = "Song Cover",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(MaterialTheme.shapes.small)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = message.songTitle ?: "",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = message.songArtist ?: "",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }

                if (message.mediaUrl != null) {
                    AsyncImage(
                        model = message.mediaUrl,
                        contentDescription = "Attachment",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(180.dp)
                            .clip(MaterialTheme.shapes.small)
                            .padding(bottom = 6.dp)
                    )
                }

                Text(
                    text = message.messageText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "10:42 AM",
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.7f)
                    )
                    if (message.isFromMe) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Read",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
