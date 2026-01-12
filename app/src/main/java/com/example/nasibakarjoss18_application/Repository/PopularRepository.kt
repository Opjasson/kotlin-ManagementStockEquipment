package com.example.nasibakarjoss18_application.Repository

import android.util.Log
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.google.android.gms.common.api.internal.StatusCallback
import com.google.firebase.firestore.FirebaseFirestore

class PopularRepository {
    private val database = FirebaseFirestore.getInstance()

    fun getPopularItem (
        callback: (MutableList<ItemsModel>) -> Unit
    ) {
        database.collection("items")
            .whereEqualTo("popular", true)
            .get()
            .addOnSuccessListener {
                callback(it.toObjects(ItemsModel::class.java).toMutableList())
            }
    }
}