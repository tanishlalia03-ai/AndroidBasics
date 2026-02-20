package com.example.androidbasics.FCM

import android.content.Context
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.InputStream
import java.io.IOException

object NotificationSender {
    @Throws(IOException::class)
    private fun getServiceAccountFile(context: Context): InputStream {
        return context.assets.open("service-account.json")
    }

    private fun getAccessToken(context: Context): String? {
        return try {
            val googleCredentials = GoogleCredentials.fromStream(getServiceAccountFile(context))
                .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))

            googleCredentials.refreshIfExpired()
            googleCredentials.accessToken.tokenValue
        } catch (e: Exception) {
            Log.e("FCM_AUTH", "Error getting token: ${e.message}")
            null
        }
    }

    fun sendNotificationToUser(fcmToken: String, title: String, body: String, key: String, context: Context, type: String, name: String) {
        val client = OkHttpClient()
        val auth = FirebaseAuth.getInstance()
        val currentUserMail = auth.currentUser?.email ?: "No Email"

        val json = JSONObject().apply {
            put("message", JSONObject().apply {
                put("token", fcmToken)
                put("data", JSONObject().apply {
                    put("title", title)
                    put("body", body)
                    put("senderMail", currentUserMail)
                    put("messageId", key)
                    put("type", type)
                    put("name", name)
                })
            })
        }

        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = getAccessToken(context)

            if (accessToken != null) {
                val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())


                val request = Request.Builder()
                    .url("https://fcm.googleapis.com/v1/projects/practise-4169a/messages:send")
                    .addHeader("Authorization", "Bearer $accessToken")
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build()

                try {
                    val response = client.newCall(request).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Log.d("FCM_RESULT", "Notification sent! Code: ${response.code}")
                        } else {
                            Log.e("FCM_RESULT", "Failed: ${response.code} ${response.body?.string()}")
                        }
                    }
                } catch (e: IOException) {
                    Log.e("FCM_RESULT", "Network error: ${e.message}")
                }
            } else {
                withContext(Dispatchers.Main) {
                    Log.e("FCM_ERROR", "AccessToken is null - Check your JSON file name in assets")
                }
            }
        }
    }
}