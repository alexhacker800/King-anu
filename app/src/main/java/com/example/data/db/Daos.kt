package com.example.data.db

import androidx.room.*
import com.example.data.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deletePostById(id: Long)

    @Query("UPDATE posts SET likesCount = likesCount + (CASE WHEN isLikedByMe = 1 THEN -1 ELSE 1 END), isLikedByMe = NOT isLikedByMe WHERE id = :postId")
    suspend fun toggleLike(postId: Long)
}

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories ORDER BY timestamp DESC")
    fun getAllStories(): Flow<List<Story>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: Story)

    @Query("DELETE FROM stories WHERE id = :id")
    suspend fun deleteStoryById(id: Long)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages WHERE id = :id")
    suspend fun deleteMessageById(id: Long)
}

@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY id DESC")
    fun getAllSongs(): Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song)

    @Query("UPDATE songs SET isFavorite = NOT isFavorite WHERE id = :songId")
    suspend fun toggleFavorite(songId: Long)
}

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memories ORDER BY timestamp DESC")
    fun getAllMemories(): Flow<List<Memory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: Memory)

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteMemoryById(id: Long)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationItem)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
}

@Dao
interface StorageDao {
    @Query("SELECT * FROM storage_items ORDER BY timestamp DESC")
    fun getAllStorageItems(): Flow<List<StorageItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStorageItem(item: StorageItem)

    @Query("DELETE FROM storage_items WHERE id = :id")
    suspend fun deleteStorageItemById(id: Long)
}

@Dao
interface WatchMovieDao {
    @Query("SELECT * FROM watch_movies ORDER BY id ASC")
    fun getAllMovies(): Flow<List<WatchMovieItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: WatchMovieItem)

    @Query("DELETE FROM watch_movies WHERE id = :id")
    suspend fun deleteMovieById(id: Long)
}

