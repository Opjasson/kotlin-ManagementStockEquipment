package com.example.nasibakarjoss18_application.DataStore

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(
    name = "user_pref"
)

object TransactionPrefKey {
    val TRANSACTION_ID = stringPreferencesKey("transaction_id")
}