package com.example.nasibakarjoss18_application.Repository

import android.util.Log
import com.example.nasibakarjoss18_application.Domain.BarangMasukModel
import com.example.nasibakarjoss18_application.Domain.ItemsModel
import com.example.nasibakarjoss18_application.Helper.ConvertDateTime
import com.google.android.gms.common.api.internal.StatusCallback
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore


class PopularRepository {
    private val database = FirebaseFirestore.getInstance()
    private val convertDate = ConvertDateTime()

    //    get item based on popular
    fun getPopularItem(
        callback: (List<ItemsModel>) -> Unit
    ) {
        database.collection("items")
            .whereEqualTo("popular", true)
            .get()
            .addOnSuccessListener { snapshots ->
                val list = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(ItemsModel::class.java)?.apply {
                        documentId = doc.id   // 🔥 isi documentId
                    }
                }
                callback(list)
            }
    }

    //     get item by itemId
    fun getItemByItemId(
        itemId: String,
        callback: (ItemsModel?) -> Unit
    ) {
        database.collection("items")
            .document(itemId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val item = document.toObject(ItemsModel::class.java)?.apply {
                        documentId = document.id // ✅ isi documentId
                    }
                    callback(item)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    //     get item alat makan <= 3
    fun getItemAlatMakan(
        callback: (List<ItemsModel>) -> Unit
    ) {
        database.collection("items")
            .whereEqualTo("kategoriId", 2)
            .get()
            .addOnSuccessListener {
                    snapshots ->
                val list = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(ItemsModel::class.java)?.apply {
                        documentId = doc.id   // 🔥 isi documentId
                    }
                }.filter { item ->
                    item.jumlahBarang <= 3
                }
                callback(list)
            }
    }

    //     get item alat makan all
    fun getItemAlatMakanAll(
        kategoriId: Long,
        callback: (List<ItemsModel>) -> Unit
    ) {
        database.collection("items")
            .whereEqualTo("kategoriId", kategoriId)
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

    //     get item alat masak <= 3
    fun getItemAlatMasak(
        callback: (List<ItemsModel>) -> Unit
    ) {
        database.collection("items")
            .whereEqualTo("kategoriId", 0)
            .get()
            .addOnSuccessListener {
                    snapshots ->
                val list = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(ItemsModel::class.java)?.apply {
                        documentId = doc.id   // 🔥 isi documentId
                    }
                }.filter { item ->
                    item.jumlahBarang <= 3
                }
                callback(list)
            }
    }

    //     get item alat cuci <= 3
    fun getItemAlatCuci(
        callback: (List<ItemsModel>) -> Unit
    ) {
        database.collection("items")
            .whereEqualTo("kategoriId", 1)
            .get()
            .addOnSuccessListener {
                snapshots ->
                val list = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(ItemsModel::class.java)?.apply {
                        documentId = doc.id   // 🔥 isi documentId
                    }
                }.filter { item ->
                    item.jumlahBarang <= 3
                }
                callback(list)
            }
    }

    //     get barang masuk
//    fun getBarangMasuk(
//        tanggal: Timestamp,
//        callback: (List<BarangMasukModel>) -> Unit
//    ) {
//        database.collection("barang_masuk")
//            .whereEqualTo("createdAt", tanggal)
//            .get()
//            .addOnSuccessListener {
//                callback(it)
//            }
//    }

    //    Update item
    fun updateItem(
        itemId: String,
        nama: String,
        deskripsi: String,
        jumlahBarang: Long,
        popular: Boolean,
        imgUrl: String,
        onResult: (Boolean) -> Unit
    ) {
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
                Log.d("hasil", "isi : ${it}")
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    //    Add item
    fun createItem(
        nama: String,
        deskripsi: String,
        popular: Boolean,
        imgUrl: String,
        kategoriId: Long,
        onResult: (Boolean) -> Unit
    ) {
        var data = mapOf(
            "nama" to nama,
            "deskripsi" to deskripsi,
            "jumlahBarang" to 0,
            "popular" to popular,
            "imgUrl" to imgUrl,
            "kategoriId" to kategoriId,
            "createdAt" to Timestamp.now()
        )
        database.collection("items")
            .add(data)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    //    Add stok barang
    fun addStockItem(
        barangId: String,
        barang_masuk: Long,
        onResult: (Boolean) -> Unit
    ) {
        val dateCreatedAt = convertDate.formatTimestamp(Timestamp.now())
        var data = mapOf(
            "barangId" to barangId,
            "barang_masuk" to barang_masuk,
            "createdAt" to dateCreatedAt
        )
        database.collection("barang_masuk")
            .add(data)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    //    Kurang stok barang
    fun minStockItem(
        barangId: String,
        barang_keluar: Long,
        onResult: (Boolean) -> Unit
    ) {
        var data = mapOf(
            "barangId" to barangId,
            "barang_keluar" to barang_keluar,
            "createdAt" to Timestamp.now()
        )
        database.collection("barang_keluar")
            .add(data)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    //    Get all items MainActivity
    fun getAllItems(callback: (List<ItemsModel>) -> Unit) {
        database.collection("items")
            .get()
            .addOnSuccessListener { snapshots ->
                val list = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(ItemsModel::class.java)?.apply {
                        documentId = doc.id   // 🔥 isi documentId
                    }
                }
                callback(list)
            }
    }

    //    Handle search items MainActivity
    fun searchItems(
        keyword: String,
        callback: (List<ItemsModel>) -> Unit
    ) {
        database
            .collection("items")
            .orderBy("nama")
            .startAt(keyword)
            .endAt(keyword + "\uf8ff")
            .get()
            .addOnSuccessListener { snapshots ->
                val list = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(ItemsModel::class.java)?.apply {
                        documentId = doc.id   // 🔥 isi documentId
                    }
                }
                callback(list)
            }
    }
}