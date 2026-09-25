package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.*
import com.example.data.repository.LoveNestRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class LoveNestViewModel(application: Application) : AndroidViewModel(application) {

    val repository = LoveNestRepository(application)

    val posts: StateFlow<List<Post>> = repository.posts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val stories: StateFlow<List<Story>> = repository.stories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val songs: StateFlow<List<Song>> = repository.songs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val memories: StateFlow<List<Memory>> = repository.memories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val storageItems: StateFlow<List<StorageItem>> = repository.storageItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val watchMovies: StateFlow<List<WatchMovieItem>> = repository.watchMovies.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val notifications: StateFlow<List<NotificationItem>> = repository.notifications.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val coupleInfo: StateFlow<CoupleInfo> = repository.coupleInfo
    val vpnState: StateFlow<VpnState> = repository.vpnState

    fun createPost(caption: String, hashtags: String, mediaUrl: String, isVideo: Boolean, privacy: String) {
        repository.createPost(caption, hashtags, mediaUrl, isVideo, privacy)
    }

    fun deletePost(postId: Long) {
        repository.deletePost(postId)
    }

    fun toggleLike(postId: Long) {
        repository.toggleLike(postId)
    }

    fun addStory(mediaUrl: String, caption: String, musicTitle: String) {
        repository.addStory(mediaUrl, caption, musicTitle)
    }

    fun sendMessage(text: String, mediaUrl: String? = null, song: Song? = null, isVoice: Boolean = false) {
        repository.sendMessage(text, mediaUrl, song, isVoice)
    }

    fun deleteMessage(msgId: Long) {
        repository.deleteMessage(msgId)
    }

    fun toggleSongFavorite(songId: Long) {
        repository.toggleSongFavorite(songId)
    }

    fun addSongToPlaylist(title: String, artist: String, coverUrl: String, audioUrl: String) {
        repository.addSongToPlaylist(title, artist, coverUrl, audioUrl)
    }

    fun addMemory(title: String, dateText: String, content: String, imageUrl: String, category: String) {
        repository.addMemory(title, dateText, content, imageUrl, category)
    }

    fun addStorageItem(title: String, category: String, fileSizeMb: Double, fileUrl: String, isEncrypted: Boolean) {
        repository.addStorageItem(title, category, fileSizeMb, fileUrl, isEncrypted)
    }

    fun deleteStorageItem(id: Long) {
        repository.deleteStorageItem(id)
    }

    fun addWatchMovie(title: String, category: String, duration: String, videoUrl: String, thumbnailUrl: String, description: String) {
        repository.addWatchMovie(title, category, duration, videoUrl, thumbnailUrl, description)
    }

    fun updateCoupleAvatarsAndFrames(p1Avatar: String, p2Avatar: String, p1Frame: String, p2Frame: String) {
        repository.updateCoupleAvatarsAndFrames(p1Avatar, p2Avatar, p1Frame, p2Frame)
    }

    fun updateFontStyle(style: String) {
        repository.updateFontStyle(style)
    }

    fun toggleAppLock(enabled: Boolean, pin: String) {
        repository.toggleAppLock(enabled, pin)
    }

    fun unlockVault() {
        repository.unlockVault()
    }

    fun lockVault() {
        repository.lockVault()
    }

    fun updateApiKeys(firebaseKey: String, geminiKey: String) {
        repository.updateApiKeys(firebaseKey, geminiKey)
    }

    fun updateCoupleMood(mood: String) {
        repository.updateCoupleMood(mood)
    }

    fun updateTheme(themeName: String) {
        repository.updateTheme(themeName)
    }


    fun toggleVpnConnection(serverName: String = "India - Mumbai Secure Node", protocol: String = "WireGuard (ChaCha20-Poly1305)") {
        repository.toggleVpnConnection(serverName, protocol)
    }

    fun markNotificationsRead() {
        repository.markNotificationsRead()
    }
}
