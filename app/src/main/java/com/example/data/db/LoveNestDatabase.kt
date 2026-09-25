package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.models.*

@Database(
    entities = [
        Post::class,
        Story::class,
        ChatMessage::class,
        Song::class,
        Memory::class,
        NotificationItem::class,
        StorageItem::class,
        WatchMovieItem::class
    ],
    version = 2,
    exportSchema = false
)
abstract class LoveNestDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun storyDao(): StoryDao
    abstract fun chatDao(): ChatDao
    abstract fun songDao(): SongDao
    abstract fun memoryDao(): MemoryDao
    abstract fun notificationDao(): NotificationDao
    abstract fun storageDao(): StorageDao
    abstract fun watchMovieDao(): WatchMovieDao


    companion object {
        @Volatile
        private var INSTANCE: LoveNestDatabase? = null

        fun getInstance(context: Context): LoveNestDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoveNestDatabase::class.java,
                    "lovenest_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
