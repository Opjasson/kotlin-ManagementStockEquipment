package com.example.nasibakarjoss18_application.DataStore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference(private val context: Context) {

    private val USER_ID = stringPreferencesKey("user_id")

    // SIMPAN userId
    suspend fun saveUserId(userId: String) {
        context.dataStore.edit {
            it[USER_ID] = userId
        }
    }

    // AMBIL userId
    fun getUserId(): Flow<String> {
        return context.dataStore.data.map {
            it[USER_ID] ?: ""
        }
    }
}