package com.example.data.api

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun getLoveAdvice(prompt: String, apiKey: String): String = withContext(Dispatchers.IO) {
        val cleanKey = apiKey.trim()
        if (cleanKey.isBlank() || cleanKey.startsWith("AIzaSyGemini_LoveNest_Key") || cleanKey.contains("ConfiguredKey")) {
            // Fallback sweet local love coach responses if key is not configured or default
            return@withContext getFallbackResponse(prompt)
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$cleanKey"

        // Build System Instruction and User Content in the standard format
        val requestJson = JSONObject()
        val contentsArray = JSONArray()
        val contentObject = JSONObject()
        val partsArray = JSONArray()
        val partObject = JSONObject()

        partObject.put("text", prompt)
        partsArray.put(partObject)
        contentObject.put("parts", partsArray)
        contentsArray.put(contentObject)
        requestJson.put("contents", contentsArray)

        // System Instruction configuration
        val systemInstructionObject = JSONObject()
        val systemPartsArray = JSONArray()
        val systemPartObject = JSONObject()
        systemPartObject.put("text", "You are 'Cupid AI ❤️', a warm, charming, and wise AI Relationship Coach and Love Guru inside the 'LoveNest' couple application. Your mission is to assist couples in strengthening their bond, writing beautiful romantic poetry, planning date ideas, or sending romantic words. Keep responses highly sweet, engaging, helpful, and under 3-4 sentences. Use emojis generously (💕, 🌹, 🥰, ✨, 💖). Answer beautifully in English, Hindi, or Hinglish depending on the user's input.")
        systemPartsArray.put(systemPartObject)
        systemInstructionObject.put("parts", systemPartsArray)
        requestJson.put("systemInstruction", systemInstructionObject)

        // Generation Config for sweet replies
        val generationConfig = JSONObject()
        generationConfig.put("temperature", 0.7)
        requestJson.put("generationConfig", generationConfig)

        val requestBody = requestJson.toString().toRequestBody(JSON_MEDIA_TYPE)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string()
                if (response.isSuccessful && !bodyString.isNullOrEmpty()) {
                    val responseJson = JSONObject(bodyString)
                    val candidates = responseJson.getJSONArray("candidates")
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.getJSONObject("content")
                    val parts = content.getJSONArray("parts")
                    val firstPart = parts.getJSONObject(0)
                    return@withContext firstPart.getString("text")
                } else {
                    Log.e(TAG, "API Error: ${response.code} - $bodyString")
                    return@withContext "Error: Gemini API error ${response.code}. Please verify your API Key in Settings! 🔑"
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "API Exception: ${e.message}", e)
            return@withContext getFallbackResponse(prompt)
        }
    }

    private fun getFallbackResponse(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("date") || lower.contains("planning") || lower.contains("ghumi") -> {
                "How about a cozy candle-lit dinner at home, followed by stargazing with your favorite songs? 🌌 Or plan a surprise weekend picnic! Romantic moments don't need to be expensive, just filled with undivided attention. 💕🌹"
            }
            lower.contains("poem") || lower.contains("sweet") || lower.contains("shayari") || lower.contains("love letter") -> {
                "Here is a sweet poem for your partner: \n'Teri dharakan hi meri zindagi ka sangeet hai, \nTu hi meri sabse pyari preet hai.' \nSend this with a rose, and watch them smile! 🥰✨💖"
            }
            lower.contains("sorry") || lower.contains("fight") || lower.contains("gussa") || lower.contains("argument") -> {
                "Every sweet couple has tiny fights! Go to them, give them a warm hug, and say: 'I value US more than any argument. I am sorry, let's have ice cream!' 🍦 A soft apology works wonders. 🥰❤️"
            }
            lower.contains("anniversary") || lower.contains("gift") -> {
                "For a perfect milestone, create a digital scrap-book inside our 'Memories' section! Combine your favorite photos, write a heartfelt letter, and set a special notification. Real effort is the best gift of all. 🎁💖✨"
            }
            else -> {
                "Hello, beautiful souls! I am Cupid AI, your relationship coach. 💕 Tell me how I can help you today—whether you want date ideas, romantic poems, or just a little relationship tip to brighten your day! 💖✨"
            }
        }
    }
}
