package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val authorAvatar: String,
    val caption: String,
    val hashtags: String,
    val mediaUrl: String,
    val isVideo: Boolean = false,
    val videoDuration: String = "0:15",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val privacy: String = "Public", // Public, Partner Only, Private
    val timestamp: Long = System.currentTimeMillis(),
    val isUserPost: Boolean = false
)

@Entity(tableName = "stories")
data class Story(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val authorAvatar: String,
    val mediaUrl: String,
    val isVideo: Boolean = false,
    val caption: String = "",
    val musicTitle: String = "",
    val musicArtist: String = "",
    val viewsCount: Int = 1,
    val timestamp: Long = System.currentTimeMillis(),
    val isUserStory: Boolean = false
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderName: String,
    val messageText: String,
    val mediaUrl: String? = null,
    val isVoiceNote: Boolean = false,
    val voiceDuration: String = "0:00",
    val songTitle: String? = null,
    val songArtist: String? = null,
    val songCoverUrl: String? = null,
    val songUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromMe: Boolean = true,
    val isRead: Boolean = true,
    val replyToText: String? = null
)

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val artist: String,
    val albumCover: String,
    val audioUrl: String,
    val durationSeconds: Int = 210,
    val isFavorite: Boolean = false,
    val addedBy: String = "Aarav & Ananya",
    val platformName: String = "Spotify / Legal Stream"
)

@Entity(tableName = "memories")
data class Memory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val dateText: String,
    val content: String,
    val imageUrl: String,
    val category: String = "Photo Album", // Love Letter, Photo Album, Milestone Event
    val isSpecialRemembrance: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val category: String, // Chat, Post, Story, Anniversary
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "storage_items")
data class StorageItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String = "Photos", // "Photos", "Videos", "Love Letters", "Voice Secrets", "Documents"
    val fileSizeMb: Double = 2.5,
    val fileUrl: String = "",
    val uploadedBy: String = "Aarav & Ananya",
    val timestamp: Long = System.currentTimeMillis(),
    val isEncrypted: Boolean = true,
    val privacy: String = "Private Vault"
)

@Entity(tableName = "watch_movies")
data class WatchMovieItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String = "Romantic Movie",
    val duration: String = "1h 45m",
    val videoUrl: String = "",
    val thumbnailUrl: String = "",
    val description: String = "",
    val addedBy: String = "Couple"
)

data class CoupleInfo(
    val partner1Name: String = "Aarav",
    val partner2Name: String = "Ananya",
    val partner1Avatar: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
    val partner2Avatar: String = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300",
    val partner1AvatarFrame: String = "Neon Heart Glow", // "Neon Heart Glow", "Rose Flower Ring", "Crown & Sparkles", "Golden Ring"
    val partner2AvatarFrame: String = "Rose Flower Ring",
    val anniversaryDate: String = "2023-02-14",
    val relationshipDays: Long = 948,
    val coupleMood: String = "In Love 💕",
    val loveKey: String = "LOVE-8829-NEST",
    val isConnected: Boolean = true,
    val selectedTheme: String = "Romantic Crimson",
    val selectedFontStyle: String = "Romantic Script", // "Romantic Script", "Serif Elegance", "Modern Sans", "Playful Love"
    val isAppLockEnabled: Boolean = false,
    val appLockPin: String = "1234",
    val isVaultLocked: Boolean = true,
    val firebaseApiKey: String = "AIzaSyLoveNest_8829_ConfiguredKey",
    val geminiApiKey: String = "AIzaSyGemini_LoveNest_Key",
    val isApiKeyValid: Boolean = true
)

data class VpnState(
    val isConnected: Boolean = false,
    val statusText: String = "Disconnected",
    val serverName: String = "India - Mumbai Secure Node",
    val protocol: String = "WireGuard (ChaCha20)",
    val publicIp: String = "103.21.244.12",
    val encryptedIp: String = "185.220.101.4",
    val durationSeconds: Long = 0,
    val bytesSentMb: Float = 0.0f,
    val bytesReceivedMb: Float = 0.0f
)

