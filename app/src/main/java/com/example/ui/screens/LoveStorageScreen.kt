package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.models.CoupleInfo
import com.example.data.models.StorageItem
import com.example.ui.components.PrivacyLockOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoveStorageScreen(
    storageItems: List<StorageItem>,
    coupleInfo: CoupleInfo,
    onAddStorageItem: (String, String, Double, String, Boolean) -> Unit,
    onDeleteStorageItem: (Long) -> Unit,
    onUnlockVault: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var showUploadModal by remember { mutableStateOf(false) }
    var showLockModal by remember { mutableStateOf(coupleInfo.isVaultLocked && coupleInfo.isAppLockEnabled) }

    val categories = listOf("All", "Photos", "Videos", "Love Letters", "Voice Secrets")

    val filteredItems = remember(selectedCategory, storageItems) {
        if (selectedCategory == "All") storageItems
        else storageItems.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    val totalUsedMb = remember(storageItems) {
        storageItems.sumOf { it.fileSizeMb }
    }

    if (showLockModal) {
        PrivacyLockOverlay(
            expectedPin = coupleInfo.appLockPin,
            onUnlocked = {
                onUnlockVault()
                showLockModal = false
            },
            onDismiss = null
        )
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showUploadModal = true },
                icon = { Icon(Icons.Default.CloudUpload, contentDescription = "Upload") },
                text = { Text("Upload to Vault 🔐") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("upload_vault_fab")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Storage Meter Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderSpecial,
                                contentDescription = "Vault Storage",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Love Vault Storage 📁",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Encrypted",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "AES-256 🔒",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = String.format("%.1f MB of 20,000 MB (20 GB) Used", totalUsedMb),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = (totalUsedMb / 20000.0).toFloat().coerceIn(0.01f, 1.0f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Private cloud backup active for ${coupleInfo.partner1Name} & ${coupleInfo.partner2Name}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Category Filter Chips
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
                edgePadding = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = { Text(category) }
                    )
                }
            }

            // Storage File Items List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // File Type Icon / Preview
                            if (item.fileUrl.isNotBlank() && item.category == "Photos") {
                                AsyncImage(
                                    model = item.fileUrl,
                                    contentDescription = item.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(54.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = when (item.category) {
                                                "Videos" -> Icons.Default.Movie
                                                "Love Letters" -> Icons.Default.Mail
                                                "Voice Secrets" -> Icons.Default.Mic
                                                else -> Icons.Default.InsertDriveFile
                                            },
                                            contentDescription = item.category,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${item.category} • ${item.fileSizeMb} MB • Uploaded by ${item.uploadedBy}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Surface(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = MaterialTheme.shapes.extraSmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Text(
                                        text = if (item.isEncrypted) "🔐 Encrypted Vault" else "🔓 Public",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            IconButton(onClick = { onDeleteStorageItem(item.id) }) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete File",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showUploadModal) {
        UploadToVaultModal(
            onDismiss = { showUploadModal = false },
            onUpload = { title, category, size, url, isEncrypted ->
                onAddStorageItem(title, category, size, url, isEncrypted)
                showUploadModal = false
            }
        )
    }
}

@Composable
fun UploadToVaultModal(
    onDismiss: () -> Unit,
    onUpload: (String, String, Double, String, Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Photos") }
    var fileUrl by remember { mutableStateOf("") }
    var isEncrypted by remember { mutableStateOf(true) }

    val categoryList = listOf("Photos", "Videos", "Love Letters", "Voice Secrets", "Documents")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Upload File to Love Vault 📁") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("File Title / Memory Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Select File Category:", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    categoryList.take(3).forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    categoryList.drop(3).forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = fileUrl,
                    onValueChange = { fileUrl = it },
                    label = { Text("File / Image / Media Link (Optional)") },
                    placeholder = { Text("https://...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("AES-256 Vault Encryption 🔒", style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = isEncrypted,
                        onCheckedChange = { isEncrypted = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onUpload(title, category, 5.4, fileUrl, isEncrypted)
                    }
                }
            ) {
                Text("Secure Upload")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
