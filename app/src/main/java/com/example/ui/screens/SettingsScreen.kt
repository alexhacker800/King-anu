package com.example.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.models.CoupleInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    coupleInfo: CoupleInfo,
    onUpdateFontStyle: (String) -> Unit,
    onToggleAppLock: (Boolean, String) -> Unit,
    onUpdateApiKeys: (String, String) -> Unit,
    onOpenHindiGuide: () -> Unit
) {
    var showDataDeletionDialog by remember { mutableStateOf(false) }
    var showApiKeyModal by remember { mutableStateOf(false) }
    var showPinSetupModal by remember { mutableStateOf(false) }

    val fontStyles = listOf("Romantic Script", "Serif Elegance", "Modern Sans", "Playful Love")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "LoveNest Settings & Security ⚙️",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Beginners Hindi Tutorial Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            onClick = onOpenHindiGuide,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("open_hindi_guide_card")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Beginners Hindi Tutorial Guide 🇮🇳",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "फोन में टेस्ट करने, APK बनाने और Server / API Key Setup की पूरी जानकारी।",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Romantic Font Style Selector Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TextFields,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Romantic Font Style ✒️",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Select your favorite romantic typography style for the app:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                fontStyles.forEach { fontStyleName ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = coupleInfo.selectedFontStyle == fontStyleName,
                            onClick = { onUpdateFontStyle(fontStyleName) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = fontStyleName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (coupleInfo.selectedFontStyle == fontStyleName) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Privacy Lock & Passcode Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
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
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "App & Storage Passcode Lock 🔐",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Switch(
                        checked = coupleInfo.isAppLockEnabled,
                        onCheckedChange = { enabled ->
                            if (enabled) {
                                showPinSetupModal = true
                            } else {
                                onToggleAppLock(false, coupleInfo.appLockPin)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (coupleInfo.isAppLockEnabled) "App and Love Vault are protected with PIN: ****"
                    else "Protect your private couple chat, photos, and letters with a 4-digit PIN.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (coupleInfo.isAppLockEnabled) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { showPinSetupModal = true },
                        modifier = Modifier.testTag("change_pin_btn")
                    ) {
                        Text("Change 4-Digit Passcode 🔑")
                    }
                }
            }
        }

        // API Key & Cloud Setup Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
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
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "API Key & Cloud Setup 🔑",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        color = if (coupleInfo.isApiKeyValid) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                        shape = CircleShape
                    ) {
                        Text(
                            text = if (coupleInfo.isApiKeyValid) "🟢 Connected" else "🔴 Key Missing",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Firebase Auth, Firestore Database, and Gemini AI key configurations.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { showApiKeyModal = true },
                    modifier = Modifier.testTag("configure_api_keys_btn")
                ) {
                    Icon(imageVector = Icons.Default.SettingsSuggest, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Configure API Keys & Backend")
                }
            }
        }

        // Privacy & Account Deletion Controls
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Privacy & Data Ownership Controls",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You have full control over your data. Delete account, posts, and media records permanently at any time.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { showDataDeletionDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("delete_data_button")
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete My Account & Data")
                }
            }
        }

        // App Info Footer
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("LoveNest ❤️ Version 2.0.0 Pro", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text("Complete Private Social & Couple Platform with Personal VPN & Movie Sync.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }

    if (showPinSetupModal) {
        PinSetupModal(
            currentPin = coupleInfo.appLockPin,
            onDismiss = { showPinSetupModal = false },
            onSavePin = { pin ->
                onToggleAppLock(true, pin)
                showPinSetupModal = false
            }
        )
    }

    if (showApiKeyModal) {
        ApiKeySetupModal(
            firebaseKey = coupleInfo.firebaseApiKey,
            geminiKey = coupleInfo.geminiApiKey,
            onDismiss = { showApiKeyModal = false },
            onSaveKeys = { fbKey, gemKey ->
                onUpdateApiKeys(fbKey, gemKey)
                showApiKeyModal = false
            }
        )
    }

    if (showDataDeletionDialog) {
        AlertDialog(
            onDismissRequest = { showDataDeletionDialog = false },
            title = { Text("Delete Account & All Data?") },
            text = { Text("This will permanently remove your couple profile, shared memories, chats, and uploaded posts from LoveNest.") },
            confirmButton = {
                Button(
                    onClick = { showDataDeletionDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Confirm Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDataDeletionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PinSetupModal(
    currentPin: String,
    onDismiss: () -> Unit,
    onSavePin: (String) -> Unit
) {
    var pin by remember { mutableStateOf(currentPin) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set 4-Digit Passcode 🔐") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Enter a 4-digit numeric code to protect LoveNest:")
                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 4) pin = it },
                    label = { Text("4-Digit PIN") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (pin.length == 4) onSavePin(pin) }
            ) {
                Text("Save PIN")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ApiKeySetupModal(
    firebaseKey: String,
    geminiKey: String,
    onDismiss: () -> Unit,
    onSaveKeys: (String, String) -> Unit
) {
    var fbKey by remember { mutableStateOf(firebaseKey) }
    var gemKey by remember { mutableStateOf(geminiKey) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Configure API Keys & Backend 🔑") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enter your custom API credentials to connect real backend services:")

                OutlinedTextField(
                    value = fbKey,
                    onValueChange = { fbKey = it },
                    label = { Text("Firebase Web API Key") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = gemKey,
                    onValueChange = { gemKey = it },
                    label = { Text("Gemini AI Key") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSaveKeys(fbKey, gemKey) }) {
                Text("Connect & Save Keys")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
