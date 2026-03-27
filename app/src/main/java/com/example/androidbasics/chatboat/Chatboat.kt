package com.example.androidbasics.chatboat

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content // Essential Import
import org.json.JSONObject

object Chatboat {
    fun escapeText(text: String): String {
        return JSONObject.quote(text)
    }

    private val geminiModel by lazy {
        GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = "AIzaSyDra-X1pw_1yjBETMkc5Ffnc3yn67joi74"
        )
    }

    suspend fun getGeminiTextResponse(prompt: String, onSuccess: (String) -> Unit) {
        try {
            val response = geminiModel.generateContent(
                content {
                    text(prompt)
                }
            )

            val responseText = response.text ?: "No response from Gemini"

            // If you want the text to be formatted for a JSON body, keep escapeText.
            // If you want to show it directly in a TextView, just use responseText.
            val result = escapeText(responseText)

            onSuccess(result)
        } catch (e: Exception) {
            e.printStackTrace()
            onSuccess("Error: ${e.message ?: "Unable to fetch response!"}")
        }
    }
}