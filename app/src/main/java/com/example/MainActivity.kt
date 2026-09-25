package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.LoveNestTheme
import com.example.ui.viewmodel.LoveNestViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: LoveNestViewModel = viewModel()
            val coupleInfo by viewModel.coupleInfo.collectAsStateWithLifecycle()

            LoveNestTheme(fontStyle = coupleInfo.selectedFontStyle) {
                LoveNestApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun LoveNestApp(
    viewModel: LoveNestViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf(LoveNestScreen.HOME) }
    var showHindiGuide by remember { mutableStateOf(false) }
    var isAppLockedState by remember { mutableStateOf(false) }

    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val stories by viewModel.stories.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val songs by viewModel.songs.collectAsStateWithLifecycle()
    val memories by viewModel.memories.collectAsStateWithLifecycle()
    val storageItems by viewModel.storageItems.collectAsStateWithLifecycle()
    val watchMovies by viewModel.watchMovies.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val coupleInfo by viewModel.coupleInfo.collectAsStateWithLifecycle()
    val vpnState by viewModel.vpnState.collectAsStateWithLifecycle()

    val unreadNotifications = remember(notifications) { notifications.count { !it.isRead } }

    if (isAppLockedState) {
        PrivacyLockOverlay(
            expectedPin = coupleInfo.appLockPin,
            onUnlocked = { isAppLockedState = false },
            onDismiss = null
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LoveNestTopAppBar(
                currentScreen = currentScreen,
                vpnConnected = vpnState.isConnected,
                isAppLocked = isAppLockedState || coupleInfo.isAppLockEnabled,
                onChatClick = { currentScreen = LoveNestScreen.CHAT },
                onVpnClick = { currentScreen = LoveNestScreen.VPN },
                onLockClick = { isAppLockedState = true },
                onHindiGuideClick = { showHindiGuide = true }
            )
        },
        bottomBar = {
            LoveNestBottomNavigation(
                currentScreen = currentScreen,
                onScreenSelected = { currentScreen = it },
                unreadNotificationsCount = unreadNotifications
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                LoveNestScreen.HOME -> HomeScreen(
                    posts = posts,
                    stories = stories,
                    onLikeClick = { viewModel.toggleLike(it) },
                    onDeletePost = { viewModel.deletePost(it) },
                    onNavigateToStories = { currentScreen = LoveNestScreen.STORIES },
                    onNavigateToUpload = { currentScreen = LoveNestScreen.UPLOAD },
                    onShareToChat = { post ->
                        viewModel.sendMessage("Shared post from feed: ${post.caption}", post.mediaUrl)
                        currentScreen = LoveNestScreen.CHAT
                    }
                )

                LoveNestScreen.REELS -> ReelsScreen(
                    posts = posts,
                    onLikeClick = { viewModel.toggleLike(it) },
                    onShareClick = { post ->
                        viewModel.sendMessage("Check out this video reel: ${post.caption}", post.mediaUrl)
                        currentScreen = LoveNestScreen.CHAT
                    }
                )

                LoveNestScreen.STORIES -> StoriesScreen(
                    stories = stories,
                    onAddStoryClick = { currentScreen = LoveNestScreen.UPLOAD },
                    onCloseClick = { currentScreen = LoveNestScreen.HOME }
                )

                LoveNestScreen.CHAT -> ChatScreen(
                    messages = chatMessages,
                    coupleInfo = coupleInfo,
                    onSendMessage = { text, mediaUrl, isVoice ->
                        viewModel.sendMessage(text, mediaUrl, null, isVoice)
                    },
                    onDeleteMessage = { viewModel.deleteMessage(it) }
                )

                LoveNestScreen.WATCH_TOGETHER -> WatchTogetherScreen(
                    movies = watchMovies,
                    chatMessages = chatMessages,
                    coupleInfo = coupleInfo,
                    onSendMessage = { text ->
                        viewModel.sendMessage(text)
                    },
                    onAddMovie = { title, cat, dur, url, thumb, desc ->
                        viewModel.addWatchMovie(title, cat, dur, url, thumb, desc)
                    }
                )

                LoveNestScreen.STORAGE -> LoveStorageScreen(
                    storageItems = storageItems,
                    coupleInfo = coupleInfo,
                    onAddStorageItem = { title, cat, size, url, isEncrypted ->
                        viewModel.addStorageItem(title, cat, size, url, isEncrypted)
                    },
                    onDeleteStorageItem = { id ->
                        viewModel.deleteStorageItem(id)
                    },
                    onUnlockVault = {
                        viewModel.unlockVault()
                    }
                )

                LoveNestScreen.UPLOAD -> UploadPostScreen(
                    onPostCreated = { caption, hashtags, mediaUrl, isVideo, privacy ->
                        viewModel.createPost(caption, hashtags, mediaUrl, isVideo, privacy)
                        currentScreen = if (isVideo) LoveNestScreen.REELS else LoveNestScreen.HOME
                    },
                    onStoryCreated = { mediaUrl, caption, musicTitle ->
                        viewModel.addStory(mediaUrl, caption, musicTitle)
                        currentScreen = LoveNestScreen.STORIES
                    }
                )

                LoveNestScreen.MUSIC -> MusicSharingScreen(
                    songs = songs,
                    onToggleFavorite = { viewModel.toggleSongFavorite(it) },
                    onAddSong = { title, artist, coverUrl, audioUrl ->
                        viewModel.addSongToPlaylist(title, artist, coverUrl, audioUrl)
                    },
                    onSendSongToChat = { song ->
                        viewModel.sendMessage(text = "Shared song: ${song.title} - ${song.artist}", song = song)
                        currentScreen = LoveNestScreen.CHAT
                    }
                )

                LoveNestScreen.MEMORIES -> MemoriesScreen(
                    memories = memories,
                    coupleInfo = coupleInfo,
                    onMoodChange = { viewModel.updateCoupleMood(it) },
                    onAddMemory = { title, dateText, content, imageUrl, category ->
                        viewModel.addMemory(title, dateText, content, imageUrl, category)
                    }
                )

                LoveNestScreen.PROFILE -> CoupleProfileScreen(
                    coupleInfo = coupleInfo,
                    onThemeChange = { viewModel.updateTheme(it) },
                    onUpdateAvatarsAndFrames = { p1Avatar, p2Avatar, p1Frame, p2Frame ->
                        viewModel.updateCoupleAvatarsAndFrames(p1Avatar, p2Avatar, p1Frame, p2Frame)
                    }
                )

                LoveNestScreen.VPN -> VpnScreen(
                    vpnState = vpnState,
                    onToggleVpn = { server, protocol ->
                        viewModel.toggleVpnConnection(server, protocol)
                    }
                )

                LoveNestScreen.NOTIFICATIONS -> NotificationsScreen(
                    notifications = notifications,
                    onMarkAllRead = { viewModel.markNotificationsRead() }
                )

                LoveNestScreen.SETTINGS -> SettingsScreen(
                    coupleInfo = coupleInfo,
                    onUpdateFontStyle = { viewModel.updateFontStyle(it) },
                    onToggleAppLock = { enabled, pin ->
                        viewModel.toggleAppLock(enabled, pin)
                    },
                    onUpdateApiKeys = { fbKey, gemKey ->
                        viewModel.updateApiKeys(fbKey, gemKey)
                    },
                    onOpenHindiGuide = { showHindiGuide = true }
                )
            }
        }

        if (showHindiGuide) {
            HindiGuideModal(onDismiss = { showHindiGuide = false })
        }
    }
}
