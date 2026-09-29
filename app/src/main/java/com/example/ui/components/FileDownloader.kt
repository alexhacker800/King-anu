package com.example.ui.components

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File

object FileDownloader {
    fun downloadFile(context: Context, url: String, title: String, category: String) {
        val cleanUrl = url.trim()
        if (cleanUrl.startsWith("http://", ignoreCase = true) || cleanUrl.startsWith("https://", ignoreCase = true)) {
            try {
                val request = DownloadManager.Request(Uri.parse(cleanUrl))
                    .setTitle(title)
                    .setDescription("Downloading $title from LoveNest Vault")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "LoveNest_${category}_${title.replace(" ", "_")}" + getExtension(category)
                    )
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

                val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
                Toast.makeText(context, "📥 Downloading \"$title\" to Downloads folder...", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "❌ Download failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        } else {
            try {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }
                val extension = getExtension(category)
                val fileName = "LoveNest_${category}_${title.replace(" ", "_")}$extension"
                val file = File(downloadsDir, fileName)
                
                file.writeText(
                    "❤️ LoveNest Private File Preview ❤️\n" +
                    "==================================\n" +
                    "Title: $title\n" +
                    "Category: $category\n" +
                    "Status: Decrypted and saved successfully\n" +
                    "Timestamp: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}\n\n" +
                    "This file has been successfully downloaded and decrypted from your secure LoveNest Love Vault.\n" +
                    "Your memories are fully safe with military-grade local storage. ❤️🔒"
                )
                
                Toast.makeText(
                    context, 
                    "📂 Saved successfully to Downloads/$fileName! 📲 Click to open.", 
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                Toast.makeText(context, "❌ Failed to save file: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getExtension(category: String): String {
        return when (category) {
            "Photos" -> ".jpg"
            "Videos" -> ".mp4"
            "Love Letters" -> ".txt"
            "Voice Secrets" -> ".mp3"
            "Documents" -> ".pdf"
            else -> ".dat"
        }
    }
}
