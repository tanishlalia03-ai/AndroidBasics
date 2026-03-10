package com.example.androidbasics.mlkit

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

class TranslationHelper {
    private var translator: Translator? = null

    suspend fun prepareTranslator() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.HINDI)
            .build()

        translator = Translation.getClient(options)

        val downloadOptions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        try {
            translator?.downloadModelIfNeeded(downloadOptions)?.await()
            Log.d("Translation Helper", "Model downloaded successfully")
        } catch (e: Exception) {
            Log.e("Translation Helper", "Download error: ${e.message}")
        }
    }

    suspend fun translateText(text: String): String {
        // Return the result of the try-catch block directly
        return try {
            translator?.translate(text)?.await() ?: "Translator not ready"
        } catch (e: Exception) {
            Log.e("Helper class", "Translation error: ${e.message}")
            "Error"
        }
    } // translateText ends here

    fun close() {
        translator?.close()
    }
}