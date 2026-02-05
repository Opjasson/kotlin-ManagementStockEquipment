package com.example.nasibakarjoss18_application.DataStore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.text.get

class TransaksiPreference(private val context: Context) {
    suspend fun saveTransactionId(transactionId: String) {
        context.dataStore.edit { pref ->
            pref[TransactionPrefKey.TRANSACTION_ID] = transactionId
        }
    }

    fun getTransactionId(): Flow<String?> {
        return context.dataStore.data
            .map { pref -> pref[TransactionPrefKey.TRANSACTION_ID] }
    }

    suspend fun clearTransactionId() {
        context.dataStore.edit {
            it.remove(TransactionPrefKey.TRANSACTION_ID)
        }
    }
}