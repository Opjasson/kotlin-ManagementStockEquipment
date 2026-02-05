package com.example.nasibakarjoss18_application.Repository

import com.example.nasibakarjoss18_application.Helper.ConvertDateTime
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CartRepository {
    private val database = FirebaseFirestore.getInstance()
    private val convertDate = ConvertDateTime()

    //    Add cart
    fun addCart(
        userId: String,
        transaksiId: String,
        productId: String,
        jumlah: Long,
        onResult: (Boolean) -> Unit
    ) {
        var data = mapOf(
            "userId" to userId,
            "transaksiId" to transaksiId,
            "productId" to productId,
            "jumlah" to jumlah,
            "createdAt" to convertDate.formatTimestamp(Timestamp.now())
        )
        database.collection("cart")
            .add(data)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    //     get cart by transaksiId
    fun getCartByTransaksiId(
        transaksiId : String,
        callback : (List<CartModel>) -> Unit
    ) {
        database.collection("cart")
            .whereEqualTo("transaksiId", transaksiId)
            .get()
            .addOnSuccessListener {
                    snapshots ->
                val list = snapshots.documents.mapNotNull { doc ->
                    doc.toObject(CartModel::class.java)?.apply {
                        documentId = doc.id   // 🔥 isi documentId
                    }
                }
                callback(list)
            }
    }

    //     get getCartByProductAndTransaction
    suspend fun getCartByProductAndTransaction(
        userId: String,
        transaksiId: String,
        productId: String
    ): CartModel? {
        val snapshot = database.collection("cart")
            .whereEqualTo("userId", userId)
            .whereEqualTo("transaksiId", transaksiId)
            .whereEqualTo("productId", productId)
            .get()
            .await() // query snapshoot

        return snapshot.documents.firstOrNull()
            ?.toObject(CartModel::class.java)
            ?.apply {
                documentId = snapshot.documents.first().id
            }
    }

    //     get cart tby id
    suspend fun getCartHandleQty(
        cartId: String,
    ): CartModel? {
        val snapshot = database.collection("cart")
            .document(cartId)
            .get()
            .await() // query snapshoot

        return snapshot
            ?.toObject(CartModel::class.java)
            ?.apply {
                documentId = snapshot.id
            }
    }

    //    update jumlah cart
    suspend fun updateCartQty(cartId: String, newQty: Long) {
        database.collection("cart")
            .document(cartId)
            .update("jumlah", newQty)
            .await()
    }

    //    Delete cart
    fun deleteCart (cartId : String, onResult: (Boolean) -> Unit) {
        database.collection("cart")
            .document(cartId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }


}