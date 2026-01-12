package com.example.nasibakarjoss18_application.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.nasibakarjoss18_application.Domain.KategoriModel
import com.google.android.gms.common.api.internal.StatusCallback
import com.google.firebase.firestore.FirebaseFirestore

class KategoriRepository {
    private val database = FirebaseFirestore.getInstance()

//    Handle kategori
    fun getKategori (
        callback: (MutableList<KategoriModel>) -> Unit
    ) {
        database.collection("kategori")
            .get()
            .addOnSuccessListener {
                Log.d("LISTDATA99", "ada : ${it.size()}")
                callback(it.toObjects(KategoriModel::class.java).toMutableList())
            }
    }
}