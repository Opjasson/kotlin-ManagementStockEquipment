package com.example.nasibakarjoss18_application.Repository

import com.example.nasibakarjoss18_application.Domain.ProductModel
import com.example.nasibakarjoss18_application.Helper.ConvertDateTime
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepository {
    private val database = FirebaseFirestore.getInstance()
    private val convertDate = ConvertDateTime()

    //    Add item
    fun createItem(
        nama_product: String,
        deskripsi_product: String,
        harga_product: Long,
        kategori_product: String,
        imgUrl: String,
        promo: Boolean,
        onResult: (Boolean) -> Unit
    ) {
        var data = mapOf(
            "nama_product" to nama_product,
            "deskripsi_product" to deskripsi_product,
            "harga_product" to harga_product,
            "kategori_product" to kategori_product,
            "imgUrl" to imgUrl,
            "promo" to promo,
            "createdAt" to convertDate.formatTimestamp(Timestamp.now())
        )
        database.collection("product")
            .add(data)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    //    Get all product
    fun getAllItems(callback: (List<ProductModel>) -> Unit) {
        database.collection("product")
            .get()
            .addOnSuccessListener { snapshots ->
                val list = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(ProductModel::class.java)?.apply {
                        documentId = doc.id   // 🔥 isi documentId
                    }
                }
                callback(list)
            }
    }

    //    Delete product
    fun deleteProduct (productId : String, onResult: (Boolean) -> Unit) {
        database.collection("product")
            .document(productId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    //     get product by kategori
    fun getProductByKategori(
        kategori : String,
        callback : (List<ProductModel>) -> Unit
    ) {
        database.collection("product")
            .whereEqualTo("kategori_product", kategori)
            .get()
            .addOnSuccessListener {
                    snapshots ->
                val list = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(ProductModel::class.java)?.apply {
                        documentId = doc.id   // 🔥 isi documentId
                    }
                }
                callback(list)
            }
    }

    //     get product offer
    fun getProductOffer(
        callback: (List<ProductModel>) -> Unit
    ) {
        database.collection("product")
            .whereEqualTo("promo", true)
            .get()
            .addOnSuccessListener {
                    snapshots ->
                val list = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(ProductModel::class.java)?.apply {
                        documentId = doc.id   // 🔥 isi documentId
                    }
                }
                callback(list)
            }
    }

    //     get product by productId
    suspend fun getProductByProductId(
        productId: String
    ) : ProductModel? {
        return try {
            val document = database.collection("product")
                .document(productId)
                .get()
                .await() // 🔥 penting (kotlinx-coroutines-play-services)

            if (document.exists()) {
                document.toObject(ProductModel::class.java)?.apply {
                    documentId = document.id
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}