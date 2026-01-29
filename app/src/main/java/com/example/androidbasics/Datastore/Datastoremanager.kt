package com.example.androidbasics.Datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

// DataStore instance
val Context.dataStore by preferencesDataStore(name = "user_prefs")

object DataStoreManager {

    // --- Keys ---
    private val USER_NAME = stringPreferencesKey("user_name")
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    // --- CREATE / UPDATE ---
    suspend fun saveUserName(context: Context, name: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME] = name
        }
    }

    suspend fun saveLoginStatus(context: Context, loggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = loggedIn
        }
    }

    // --- READ ---
    suspend fun getUserName(context: Context): String {
        val prefs = context.dataStore.data.first()
        return prefs[USER_NAME] ?: ""
    }

    suspend fun isLoggedIn(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[IS_LOGGED_IN] ?: false
    }

    // --- DELETE ---
    suspend fun clearUserName(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(USER_NAME)
        }
    }

    suspend fun clearLoginStatus(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(IS_LOGGED_IN)
        }
    }

    // --- DELETE ALL ---
    suspend fun clearAll(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

}