package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.models.CoupleInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoupleProfileScreen(
    coupleInfo: CoupleInfo,
    onThemeChange: (String) -> Unit,
    onUpdateAvatarsAndFrames: (String, String, String, String) -> Unit = { _, _, _, _ -> }
) {
    val themes = listOf("Romantic Crimson", "Soft Rose", "Midnight Passion", "Sunset Glow")
    var showDpCustomizer by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Cover Photo Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=1000",
                contentDescription = "Cover Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Intertwined Couple Avatars with Romantic Frame Effects
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 44.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Partner 1 Avatar
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.sweepGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.tertiary,
                                        MaterialTheme.colorScheme.primary
                                    )
                                )
                            )
                    )
                    AsyncImage(
                        model = coupleInfo.partner1Avatar,
                        contentDescription = coupleInfo.partner1Name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                            .clip(CircleShape)
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    modifier = Modifier
                        .offset(x = (-10).dp)
                        .size(34.dp),
                    shadowElevation = 6.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }

                // Partner 2 Avatar
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.offset(x = (-20).dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.sweepGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.tertiary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                )
                            )
                    )
                    AsyncImage(
                        model = coupleInfo.partner2Avatar,
                        contentDescription = coupleInfo.partner2Name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                            .clip(CircleShape)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(52.dp))

        // Names & Relationship Title
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${coupleInfo.partner1Name} & ${coupleInfo.partner2Name}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Madly in Love • ${coupleInfo.relationshipDays} Days Together ❤️",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Love Key: ${coupleInfo.loveKey}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Button to Customize Love DP & Frame
            OutlinedButton(
                onClick = { showDpCustomizer = true },
                shape = CircleShape,
                modifier = Modifier.testTag("customize_dp_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Customize Love DP & Frame ✨")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Custom Profile Theme Selector
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Custom Romantic Profile Theme",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    themes.forEach { theme ->
                        FilterChip(
                            selected = coupleInfo.selectedTheme == theme,
                            onClick = { onThemeChange(theme) },
                            label = { Text(theme) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("142", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Shared Posts", style = MaterialTheme.typography.labelSmall)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("38", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Love Letters", style = MaterialTheme.typography.labelSmall)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("24", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Shared Songs", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showDpCustomizer) {
        LoveDpCustomizerModal(
            coupleInfo = coupleInfo,
            onDismiss = { showDpCustomizer = false },
            onSave = { p1Avatar, p2Avatar, p1Frame, p2Frame ->
                onUpdateAvatarsAndFrames(p1Avatar, p2Avatar, p1Frame, p2Frame)
                showDpCustomizer = false
            }
        )
    }
}

@Composable
fun LoveDpCustomizerModal(
    coupleInfo: CoupleInfo,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var p1Avatar by remember { mutableStateOf(coupleInfo.partner1Avatar) }
    var p2Avatar by remember { mutableStateOf(coupleInfo.partner2Avatar) }
    var p1Frame by remember { mutableStateOf(coupleInfo.partner1AvatarFrame) }
    var p2Frame by remember { mutableStateOf(coupleInfo.partner2AvatarFrame) }

    val frameOptions = listOf("Neon Heart Glow", "Rose Flower Ring", "Crown & Sparkles", "Golden Ring")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Customize Love DP & Frame ✨") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("${coupleInfo.partner1Name}'s DP Link:", style = MaterialTheme.typography.labelMedium)
                OutlinedTextField(
                    value = p1Avatar,
                    onValueChange = { p1Avatar = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("${coupleInfo.partner2Name}'s DP Link:", style = MaterialTheme.typography.labelMedium)
                OutlinedTextField(
                    value = p2Avatar,
                    onValueChange = { p2Avatar = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Select Romantic Avatar Frame Effect:", style = MaterialTheme.typography.labelMedium)
                frameOptions.forEach { frame ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = p1Frame == frame,
                            onClick = {
                                p1Frame = frame
                                p2Frame = frame
                            }
                        )
                        Text(frame)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(p1Avatar, p2Avatar, p1Frame, p2Frame) }) {
                Text("Save Attractive Love DP ❤️")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
