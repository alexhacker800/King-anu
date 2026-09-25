package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

enum class LoveNestScreen(val title: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    REELS("Reels", Icons.Default.OndemandVideo),
    STORIES("Stories", Icons.Default.AutoAwesome),
    CHAT("Chat", Icons.Default.Favorite),
    WATCH_TOGETHER("Watch Movie", Icons.Default.Movie),
    STORAGE("Love Storage", Icons.Default.FolderSpecial),
    UPLOAD("Upload", Icons.Default.AddCircleOutline),
    MUSIC("Music", Icons.Default.MusicNote),
    MEMORIES("Memories", Icons.Default.PhotoLibrary),
    PROFILE("Profile", Icons.Default.People),
    VPN("VPN", Icons.Default.VpnKey),
    NOTIFICATIONS("Alerts", Icons.Default.Notifications),
    SETTINGS("Settings", Icons.Default.Settings)
}

@Composable
fun LoveNestBottomNavigation(
    currentScreen: LoveNestScreen,
    onScreenSelected: (LoveNestScreen) -> Unit,
    unreadNotificationsCount: Int = 0
) {
    Surface(
        tonalElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LoveNestScreen.entries.forEach { screen ->
                val isSelected = currentScreen == screen
                FilterChip(
                    selected = isSelected,
                    onClick = { onScreenSelected(screen) },
                    label = { Text(screen.title) },
                    leadingIcon = {
                        BadgedBox(
                            badge = {
                                if (screen == LoveNestScreen.NOTIFICATIONS && unreadNotificationsCount > 0) {
                                    Badge { Text("$unreadNotificationsCount") }
                                } else if (screen == LoveNestScreen.CHAT) {
                                    Badge { Text("❤️") }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.testTag("nav_${screen.name.lowercase()}")
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoveNestTopAppBar(
    currentScreen: LoveNestScreen,
    vpnConnected: Boolean,
    isAppLocked: Boolean = false,
    onChatClick: () -> Unit,
    onVpnClick: () -> Unit,
    onLockClick: () -> Unit,
    onHindiGuideClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "LoveNest ❤️",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = currentScreen.title,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        },
        actions = {
            // Quick Chat shortcut button
            IconButton(
                onClick = onChatClick,
                modifier = Modifier.testTag("top_chat_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Quick Chat",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            // App Lock / Privacy button
            IconButton(
                onClick = onLockClick,
                modifier = Modifier.testTag("top_lock_button")
            ) {
                Icon(
                    imageVector = if (isAppLocked) Icons.Default.Lock else Icons.Outlined.LockOpen,
                    contentDescription = "Privacy Lock",
                    tint = if (isAppLocked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // VPN quick status icon
            IconButton(
                onClick = onVpnClick,
                modifier = Modifier.testTag("top_vpn_button")
            ) {
                Icon(
                    imageVector = if (vpnConnected) Icons.Default.VpnKey else Icons.Outlined.VpnKey,
                    contentDescription = "VPN Status",
                    tint = if (vpnConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // Hindi Beginner Guide button
            FilledTonalButton(
                onClick = onHindiGuideClick,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier
                    .padding(end = 6.dp)
                    .testTag("hindi_guide_button")
            ) {
                Text("हिंदी 🇮🇳", style = MaterialTheme.typography.labelSmall)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

