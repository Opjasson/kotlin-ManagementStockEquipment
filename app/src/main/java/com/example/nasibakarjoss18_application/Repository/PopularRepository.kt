package com.example.nasibakarjoss18_application.Repository

import android.util.Log
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.google.android.gms.common.api.internal.StatusCallback
import com.google.firebase.firestore.FirebaseFirestore

class PopularRepository {
    private val database = FirebaseFirestore.getInstance()

//    get item based on popular
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

//     get item by itemId
fun getItemByItemId(
    itemId: Long,
    callback: (List<ItemsModel>) -> Unit
) {
    database.collection("items")
        .whereEqualTo("itemId", itemId)
        .get()
        .addOnSuccessListener {
            snapshots ->
            val list = snapshots.documents.mapNotNull { doc ->
                doc.toObject(ItemsModel::class.java)?.apply {
                    documentId = doc.id   // 🔥 isi documentId
                }
            }
            callback(list)
        }
}

    //     get item alat makan
    fun getItemAlatMakan(
        kategoriId: Long,
        callback: (List<ItemsModel>) -> Unit
    ) {
        database.collection("items")
            .whereEqualTo("kategoriId", 2)
            .get()
            .addOnSuccessListener {
                Log.d("alatMakan", "data : ${it.size()}")
                callback(it.toObjects(ItemsModel::class.java).toMutableList())
            }
    }

//    Update item
fun updateItem(
    itemId : String,
    nama : String,
    deskripsi : String,
    jumlahBarang : Long,
    popular : Boolean,
    imgUrl : String,
    onResult: (Boolean) -> Unit
){
    var data = mapOf(
        "nama" to nama,
        "deskripsi" to deskripsi,
        "jumlahBarang" to jumlahBarang,
        "popular" to popular,
        "imgUrl" to imgUrl
    )
    database.collection("items")
        .document(itemId)
        .update(data)
        .addOnSuccessListener {
            onResult(true)
        }
        .addOnFailureListener {
            onResult(false)
        }
}

    //    Get all items MainActivity
    fun getAllItems(callback: (MutableList<ItemsModel>) -> Unit) {
        database.collection("items")
            .get()
            .addOnSuccessListener {
                callback(it.toObjects(ItemsModel::class.java).toMutableList())
            }
    }

    //    Handle search items MainActivity
    fun searchItems(
        keyword: String,
        callback: (MutableList<ItemsModel>) -> Unit
    ) {
        database
            .collection("items")
            .orderBy("nama")
            .startAt(keyword)
            .endAt(keyword + "\uf8ff")
            .get()
            .addOnSuccessListener {
                Log.d("Data", "size : ${it.size()}")
                callback(it.toObjects(ItemsModel::class.java).toMutableList())
            }
    }
}