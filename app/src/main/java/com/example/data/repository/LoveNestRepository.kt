package com.example.data.repository

import android.content.Context
import com.example.data.db.LoveNestDatabase
import com.example.data.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoveNestRepository(context: Context) {

    private val db = LoveNestDatabase.getInstance(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    val posts: Flow<List<Post>> = db.postDao().getAllPosts()
    val stories: Flow<List<Story>> = db.storyDao().getAllStories()
    val chatMessages: Flow<List<ChatMessage>> = db.chatDao().getAllMessages()
    val songs: Flow<List<Song>> = db.songDao().getAllSongs()
    val memories: Flow<List<Memory>> = db.memoryDao().getAllMemories()
    val notifications: Flow<List<NotificationItem>> = db.notificationDao().getAllNotifications()
    val storageItems: Flow<List<StorageItem>> = db.storageDao().getAllStorageItems()
    val watchMovies: Flow<List<WatchMovieItem>> = db.watchMovieDao().getAllMovies()

    private val _coupleInfo = MutableStateFlow(
        CoupleInfo(
            partner1Name = "Aarav",
            partner2Name = "Ananya",
            partner1Avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
            partner2Avatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300",
            partner1AvatarFrame = "Neon Heart Glow",
            partner2AvatarFrame = "Rose Flower Ring",
            anniversaryDate = "14 Feb 2023",
            relationshipDays = 948,
            coupleMood = "In Love 💕",
            loveKey = "LOVE-8829-NEST",
            isConnected = true,
            selectedTheme = "Romantic Crimson",
            selectedFontStyle = "Romantic Script",
            isAppLockEnabled = false,
            appLockPin = "1234",
            isVaultLocked = true
        )
    )
    val coupleInfo: StateFlow<CoupleInfo> = _coupleInfo.asStateFlow()


    private val _vpnState = MutableStateFlow(
        VpnState(
            isConnected = false,
            statusText = "Disconnected",
            serverName = "India - Mumbai Secure Node",
            protocol = "WireGuard (ChaCha20-Poly1305)",
            publicIp = "103.21.244.12",
            encryptedIp = "185.220.101.4",
            durationSeconds = 0,
            bytesSentMb = 0.0f,
            bytesReceivedMb = 0.0f
        )
    )
    val vpnState: StateFlow<VpnState> = _vpnState.asStateFlow()

    init {
        scope.launch {
            seedInitialDataIfEmpty()
        }
    }

    private suspend fun seedInitialDataIfEmpty() {
        // Seed initial posts if DB is empty
        val currentPosts = db.postDao().getAllPosts()
        // Simple check via DB insert on standard sample data
        db.postDao().insertPost(
            Post(
                id = 1,
                authorName = "Ananya Sharma ❤️",
                authorAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150",
                caption = "Sunset date with my love! Pure magic ✨ endlessly grateful for us. #LoveNest #CoupleGoals #Sunset",
                hashtags = "#LoveNest #CoupleGoals",
                mediaUrl = "https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=800",
                isVideo = false,
                likesCount = 28,
                commentsCount = 6,
                isLikedByMe = true,
                privacy = "Public",
                timestamp = System.currentTimeMillis() - 3600000 * 2
            )
        )
        db.postDao().insertPost(
            Post(
                id = 2,
                authorName = "Aarav Patel 🌹",
                authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                caption = "Short clip from our weekend trip in Udaipur 🏰 Water was crystal clear!",
                hashtags = "#UdaipurDiaries #Reel #Lovers",
                mediaUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800",
                isVideo = true,
                videoDuration = "0:24",
                likesCount = 45,
                commentsCount = 12,
                isLikedByMe = false,
                privacy = "Partner Only",
                timestamp = System.currentTimeMillis() - 3600000 * 6
            )
        )

        // Seed stories
        db.storyDao().insertStory(
            Story(
                id = 1,
                authorName = "Ananya",
                authorAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150",
                mediaUrl = "https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=600",
                isVideo = false,
                caption = "Morning coffee together ☕❤️",
                musicTitle = "Kesariya",
                musicArtist = "Arijit Singh",
                viewsCount = 18,
                timestamp = System.currentTimeMillis() - 1800000
            )
        )
        db.storyDao().insertStory(
            Story(
                id = 2,
                authorName = "Aarav",
                authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                mediaUrl = "https://images.unsplash.com/photo-1511285560929-80b456fea0bc?w=600",
                isVideo = true,
                caption = "Our favorite song playing 🎶",
                musicTitle = "Apna Bana Le",
                musicArtist = "Arijit Singh",
                viewsCount = 12,
                timestamp = System.currentTimeMillis() - 3600000 * 4
            )
        )

        // Seed Chat Messages
        db.chatDao().insertMessage(
            ChatMessage(
                id = 1,
                senderName = "Ananya",
                messageText = "Hey love! Did you see the new memory I uploaded?",
                timestamp = System.currentTimeMillis() - 3600000 * 3,
                isFromMe = false,
                isRead = true
            )
        )
        db.chatDao().insertMessage(
            ChatMessage(
                id = 2,
                senderName = "Aarav",
                messageText = "Yes sweetheart! It looks gorgeous 😍 Adding it to our shared album.",
                timestamp = System.currentTimeMillis() - 3600000 * 2,
                isFromMe = true,
                isRead = true
            )
        )
        db.chatDao().insertMessage(
            ChatMessage(
                id = 3,
                senderName = "Ananya",
                messageText = "Listen to this song I saved for us 🎵",
                songTitle = "Tum Hi Ho",
                songArtist = "Arijit Singh",
                songCoverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300",
                songUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                timestamp = System.currentTimeMillis() - 1800000,
                isFromMe = false,
                isRead = true
            )
        )

        // Seed Songs
        db.songDao().insertSong(
            Song(
                id = 1,
                title = "Kesariya",
                artist = "Arijit Singh & Pritam",
                albumCover = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=300",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                isFavorite = true,
                addedBy = "Ananya"
            )
        )
        db.songDao().insertSong(
            Song(
                id = 2,
                title = "Apna Bana Le",
                artist = "Arijit Singh & Sachin-Jigar",
                albumCover = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                isFavorite = true,
                addedBy = "Aarav"
            )
        )
        db.songDao().insertSong(
            Song(
                id = 3,
                title = "Pehli Nazar Mein",
                artist = "Atif Aslam",
                albumCover = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
                isFavorite = false,
                addedBy = "Ananya"
            )
        )

        // Seed Memories
        db.memoryDao().insertMemory(
            Memory(
                id = 1,
                title = "Our First Anniversary Trip ✈️",
                dateText = "14 Feb 2024",
                content = "A magical week spent in Manali. Snowy peaks, warm hot chocolate, and endless laughter together.",
                imageUrl = "https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=600",
                category = "Photo Album",
                isSpecialRemembrance = true
            )
        )
        db.memoryDao().insertMemory(
            Memory(
                id = 2,
                title = "Love Letter to My Forever ❤️",
                dateText = "24 Dec 2024",
                content = "Every day with you feels like a dream I never want to wake up from. Thank you for being my anchor.",
                imageUrl = "https://images.unsplash.com/photo-1516589178581-6cd7833ae3b2?w=600",
                category = "Love Letter",
                isSpecialRemembrance = true
            )
        )

        // Seed Notifications
        db.notificationDao().insertNotification(
            NotificationItem(
                id = 1,
                title = "New Song Shared 🎵",
                message = "Ananya shared 'Tum Hi Ho' in your Private Chat.",
                category = "Chat",
                timestamp = System.currentTimeMillis() - 1800000,
                isRead = false
            )
        )
        db.notificationDao().insertNotification(
            NotificationItem(
                id = 2,
                title = "Upcoming Anniversary Alert 🎉",
                message = "Your 1000 Days Milestone is coming up in 52 days!",
                category = "Anniversary",
                timestamp = System.currentTimeMillis() - 86400000,
                isRead = true
            )
        )

        // Seed Storage Items
        db.storageDao().insertStorageItem(
            StorageItem(
                id = 1,
                title = "Our First Paris Trip Photos 📸",
                category = "Photos",
                fileSizeMb = 14.2,
                fileUrl = "https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=800",
                uploadedBy = "Ananya",
                isEncrypted = true,
                privacy = "Private Vault"
            )
        )
        db.storageDao().insertStorageItem(
            StorageItem(
                id = 2,
                title = "Secret Anniversary Video Memory 🎥",
                category = "Videos",
                fileSizeMb = 128.5,
                fileUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800",
                uploadedBy = "Aarav",
                isEncrypted = true,
                privacy = "Partner Only"
            )
        )
        db.storageDao().insertStorageItem(
            StorageItem(
                id = 3,
                title = "Love Letter #1 - Forever Mine 💌",
                category = "Love Letters",
                fileSizeMb = 0.5,
                fileUrl = "",
                uploadedBy = "Ananya",
                isEncrypted = true,
                privacy = "Private Vault"
            )
        )
        db.storageDao().insertStorageItem(
            StorageItem(
                id = 4,
                title = "Late Night Voice Confession 🎙️",
                category = "Voice Secrets",
                fileSizeMb = 3.8,
                fileUrl = "",
                uploadedBy = "Aarav",
                isEncrypted = true,
                privacy = "Private Vault"
            )
        )

        // Seed Watch Together Movies
        db.watchMovieDao().insertMovie(
            WatchMovieItem(
                id = 1,
                title = "Romantic Sunset Walk in Paris 🌅",
                category = "Romance / Travel",
                duration = "1h 42m",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                thumbnailUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=800",
                description = "Experience the enchanting romantic streets of Paris under golden sunset lighting together.",
                addedBy = "Aarav & Ananya"
            )
        )
        db.watchMovieDao().insertMovie(
            WatchMovieItem(
                id = 2,
                title = "Couples Stargazing & Chill Lofi Beats 🌌",
                category = "Chill / Music Movie",
                duration = "2h 15m",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                thumbnailUrl = "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=800",
                description = "Relax and listen to romantic lofi melodies while gazing at glowing shooting stars.",
                addedBy = "Ananya"
            )
        )
        db.watchMovieDao().insertMovie(
            WatchMovieItem(
                id = 3,
                title = "Our Udaipur Vacation Highlights Reel 🏰",
                category = "Couples Memories",
                duration = "0h 35m",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                thumbnailUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800",
                description = "A sweet compilation of all our lake palace date moments in Rajasthan.",
                addedBy = "Aarav"
            )
        )
    }

    // Actions
    fun createPost(caption: String, hashtags: String, mediaUrl: String, isVideo: Boolean, privacy: String) {
        scope.launch {
            val newPost = Post(
                authorName = "Aarav Patel (You) ❤️",
                authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                caption = caption,
                hashtags = hashtags,
                mediaUrl = if (mediaUrl.isBlank()) "https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=800" else mediaUrl,
                isVideo = isVideo,
                videoDuration = if (isVideo) "0:15" else "",
                privacy = privacy,
                isUserPost = true,
                timestamp = System.currentTimeMillis()
            )
            db.postDao().insertPost(newPost)
        }
    }

    fun deletePost(postId: Long) {
        scope.launch {
            db.postDao().deletePostById(postId)
        }
    }

    fun toggleLike(postId: Long) {
        scope.launch {
            db.postDao().toggleLike(postId)
        }
    }

    fun addStory(mediaUrl: String, caption: String, musicTitle: String) {
        scope.launch {
            val newStory = Story(
                authorName = "You",
                authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                mediaUrl = if (mediaUrl.isBlank()) "https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=600" else mediaUrl,
                caption = caption,
                musicTitle = musicTitle,
                isUserStory = true,
                timestamp = System.currentTimeMillis()
            )
            db.storyDao().insertStory(newStory)
        }
    }

    fun sendMessage(text: String, mediaUrl: String? = null, song: Song? = null, isVoice: Boolean = false) {
        scope.launch {
            val msg = ChatMessage(
                senderName = "Aarav",
                messageText = text,
                mediaUrl = mediaUrl,
                isVoiceNote = isVoice,
                voiceDuration = if (isVoice) "0:08" else "0:00",
                songTitle = song?.title,
                songArtist = song?.artist,
                songCoverUrl = song?.albumCover,
                songUrl = song?.audioUrl,
                isFromMe = true,
                isRead = true,
                timestamp = System.currentTimeMillis()
            )
            db.chatDao().insertMessage(msg)
        }
    }

    fun deleteMessage(msgId: Long) {
        scope.launch {
            db.chatDao().deleteMessageById(msgId)
        }
    }

    fun toggleSongFavorite(songId: Long) {
        scope.launch {
            db.songDao().toggleFavorite(songId)
        }
    }

    fun addSongToPlaylist(title: String, artist: String, coverUrl: String, audioUrl: String) {
        scope.launch {
            val newSong = Song(
                title = title,
                artist = artist,
                albumCover = if (coverUrl.isBlank()) "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300" else coverUrl,
                audioUrl = if (audioUrl.isBlank()) "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3" else audioUrl,
                addedBy = "Aarav (You)"
            )
            db.songDao().insertSong(newSong)
        }
    }

    fun addMemory(title: String, dateText: String, content: String, imageUrl: String, category: String) {
        scope.launch {
            val newMemory = Memory(
                title = title,
                dateText = dateText,
                content = content,
                imageUrl = if (imageUrl.isBlank()) "https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=600" else imageUrl,
                category = category,
                timestamp = System.currentTimeMillis()
            )
            db.memoryDao().insertMemory(newMemory)
        }
    }

    // Storage Vault Actions
    fun addStorageItem(title: String, category: String, fileSizeMb: Double, fileUrl: String, isEncrypted: Boolean) {
        scope.launch {
            val item = StorageItem(
                title = title,
                category = category,
                fileSizeMb = fileSizeMb,
                fileUrl = if (fileUrl.isBlank()) "https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=600" else fileUrl,
                uploadedBy = "Aarav (You)",
                isEncrypted = isEncrypted,
                privacy = "Private Vault"
            )
            db.storageDao().insertStorageItem(item)
        }
    }

    fun deleteStorageItem(id: Long) {
        scope.launch {
            db.storageDao().deleteStorageItemById(id)
        }
    }

    // Watch Movie Actions
    fun addWatchMovie(title: String, category: String, duration: String, videoUrl: String, thumbnailUrl: String, description: String) {
        scope.launch {
            val movie = WatchMovieItem(
                title = title,
                category = category,
                duration = if (duration.isBlank()) "1h 30m" else duration,
                videoUrl = if (videoUrl.isBlank()) "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4" else videoUrl,
                thumbnailUrl = if (thumbnailUrl.isBlank()) "https://images.unsplash.com/photo-1518199266791-5375a83190b7?w=800" else thumbnailUrl,
                description = description,
                addedBy = "Aarav & Ananya"
            )
            db.watchMovieDao().insertMovie(movie)
        }
    }

    fun updateCoupleAvatarsAndFrames(
        p1Avatar: String,
        p2Avatar: String,
        p1Frame: String,
        p2Frame: String
    ) {
        _coupleInfo.value = _coupleInfo.value.copy(
            partner1Avatar = p1Avatar.ifBlank { _coupleInfo.value.partner1Avatar },
            partner2Avatar = p2Avatar.ifBlank { _coupleInfo.value.partner2Avatar },
            partner1AvatarFrame = p1Frame,
            partner2AvatarFrame = p2Frame
        )
    }

    fun updateFontStyle(newStyle: String) {
        _coupleInfo.value = _coupleInfo.value.copy(selectedFontStyle = newStyle)
    }

    fun toggleAppLock(enabled: Boolean, pin: String) {
        _coupleInfo.value = _coupleInfo.value.copy(
            isAppLockEnabled = enabled,
            appLockPin = pin.ifBlank { "1234" },
            isVaultLocked = enabled
        )
    }

    fun unlockVault() {
        _coupleInfo.value = _coupleInfo.value.copy(isVaultLocked = false)
    }

    fun lockVault() {
        _coupleInfo.value = _coupleInfo.value.copy(isVaultLocked = true)
    }

    fun updateApiKeys(firebaseKey: String, geminiKey: String) {
        _coupleInfo.value = _coupleInfo.value.copy(
            firebaseApiKey = firebaseKey,
            geminiApiKey = geminiKey,
            isApiKeyValid = firebaseKey.isNotBlank() && geminiKey.isNotBlank()
        )
    }

    fun updateCoupleMood(newMood: String) {
        _coupleInfo.value = _coupleInfo.value.copy(coupleMood = newMood)
    }

    fun updateTheme(newTheme: String) {
        _coupleInfo.value = _coupleInfo.value.copy(selectedTheme = newTheme)
    }

    fun toggleVpnConnection(serverName: String = "India - Mumbai Secure Node", protocol: String = "WireGuard (ChaCha20-Poly1305)") {
        val current = _vpnState.value
        if (current.isConnected) {
            _vpnState.value = current.copy(
                isConnected = false,
                statusText = "Disconnected",
                durationSeconds = 0
            )
        } else {
            _vpnState.value = current.copy(
                isConnected = true,
                statusText = "Connected • Secure Tunnel Active 🔒",
                serverName = serverName,
                protocol = protocol,
                durationSeconds = 1,
                bytesSentMb = 1.4f,
                bytesReceivedMb = 8.7f
            )
        }
    }

    fun markNotificationsRead() {
        scope.launch {
            db.notificationDao().markAllAsRead()
        }
    }
}

