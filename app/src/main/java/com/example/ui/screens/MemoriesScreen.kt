package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.models.CoupleInfo
import com.example.data.models.Memory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoriesScreen(
    memories: List<Memory>,
    coupleInfo: CoupleInfo,
    onMoodChange: (String) -> Unit,
    onAddMemory: (title: String, dateText: String, content: String, imageUrl: String, category: String) -> Unit
) {
    var showAddMemoryModal by remember { mutableStateOf(false) }
    val moods = listOf("In Love 💕", "Happy 😊", "Missing You 🥺", "Romantic 🌹", "Cuddly 🧸")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Relationship Milestone Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("relationship_counter_card"),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${coupleInfo.partner1Name} ❤️ ${coupleInfo.partner2Name}",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "TOGETHER FOR",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )

                        Text(
                            text = "${coupleInfo.relationshipDays} Days",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White
                        )

                        Text(
                            text = "22 Hours • 14 Minutes • 30 Seconds",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        ) {
                            Text(
                                text = "Anniversary: ${coupleInfo.anniversaryDate}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Mood Check-in Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Couple Mood Check-in", style = MaterialTheme.typography.titleMedium)
                        Text(coupleInfo.coupleMood, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        moods.forEach { mood ->
                            FilterChip(
                                selected = coupleInfo.coupleMood == mood,
                                onClick = { onMoodChange(mood) },
                                label = { Text(mood) }
                            )
                        }
                    }
                }
            }
        }

        // Shared Calendar & Reminders
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Event, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Shared Calendar & Reminders 📅", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("• Next Anniversary: 14 Feb 2025 (In 148 days)", style = MaterialTheme.typography.bodyMedium)
                    Text("• Ananya's Birthday: 22 Nov 2024 (In 65 days)", style = MaterialTheme.typography.bodyMedium)
                    Text("• First Date Anniversary: 04 Oct 2024 (In 16 days)", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Love Letters & Memory Albums Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Love Notes & Photo Albums", style = MaterialTheme.typography.titleLarge)
                Button(onClick = { showAddMemoryModal = true }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Memory")
                }
            }
        }

        // Memory Items
        items(memories, key = { it.id }) { memory ->
            MemoryCard(memory = memory)
        }
    }

    // Add Memory Modal
    if (showAddMemoryModal) {
        var title by remember { mutableStateOf("") }
        var dateText by remember { mutableStateOf("18 Sep 2024") }
        var content by remember { mutableStateOf("") }
        var imageUrl by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Photo Album") }

        AlertDialog(
            onDismissRequest = { showAddMemoryModal = false },
            title = { Text("Create New Couple Memory") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Memory Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = dateText,
                        onValueChange = { dateText = it },
                        label = { Text("Date") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Write Love Note / Letter") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("Photo URL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (title.isNotBlank()) {
                        onAddMemory(title, dateText, content, imageUrl, category)
                        showAddMemoryModal = false
                    }
                }) {
                    Text("Save Memory")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMemoryModal = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun MemoryCard(memory: Memory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("memory_card_${memory.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            if (memory.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = memory.imageUrl,
                    contentDescription = memory.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = memory.title, style = MaterialTheme.typography.titleMedium)
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(
                            text = memory.category,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🗓️ ${memory.dateText}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = memory.content, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
