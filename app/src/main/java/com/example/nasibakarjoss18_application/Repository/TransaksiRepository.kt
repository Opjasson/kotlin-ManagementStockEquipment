package com.example.nasibakarjoss18_application.Repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.nasibakarjoss18_application.Domain.CartModel
import com.example.nasibakarjoss18_application.Domain.ProductModel
import com.example.nasibakarjoss18_application.Domain.TransaksiModel
import com.example.nasibakarjoss18_application.Helper.ConvertDateTime
import kotlinx.coroutines.tasks.await
import java.util.UUID

class TransaksiRepository {
    private val database = FirebaseFirestore.getInstance()
    private val convertDate = ConvertDateTime()

    fun createTransaksi(
        userId: String,
        totalHarga: Long,
        catatanTambahan: String,
        buktiTransfer: String,
        onResult: (String) -> Unit
    ) {
        val transactionId = UUID.randomUUID().toString()
        val data = mapOf(
            "userId" to userId,
            "totalHarga" to totalHarga,
            "nominalBayar" to 0L,
            "catatanTambahan" to catatanTambahan,
            "buktiTransfer" to buktiTransfer,
            "createdAt" to convertDate.formatTimestamp(Timestamp.now())
        )
        database.collection("transaksi")
            .document(transactionId)
            .set(data)
            .addOnSuccessListener {
                onResult(transactionId)
            }
            .addOnFailureListener {
                onResult("")
            }
    }

    fun updateTransaksi(
        transaksiId: String,
        totalHarga: Long,
        nominalBayar: Long,
        catatanTambahan: String,
        buktiTransfer: String,
        onResult: (Boolean) -> Unit
    ) {
        val data = mapOf(
            "totalHarga" to totalHarga,
            "nominalBayar" to nominalBayar,
            "catatanTambahan" to catatanTambahan,
            "buktiTransfer" to buktiTransfer,
        )
        database.collection("transaksi")
            .document(transaksiId)
            .update(data)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    suspend fun getCartHistoryTransaksi(transaksiId: String): List<CartModel> {
        val snapshot = database.collection("cart")
            .whereEqualTo("transaksiId", transaksiId)
            .get()
            .await()
        return snapshot.toObjects(CartModel::class.java)
    }

    suspend fun getTransaksiByUser(userId: String): List<Pair<String, TransaksiModel>> {
        val snapshot = database.collection("transaksi")
            .whereEqualTo("userId", userId)
            .get()
            .await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(TransaksiModel::class.java)?.let {
                doc.id to it
            }
        }
    }

    suspend fun getProductById(productId: String): ProductModel? {
        val doc = database.collection("product")
            .document(productId)
            .get()
            .await()
        return doc.toObject(ProductModel::class.java)
    }

    suspend fun getTransaksiByDate(tanggal1: String, tanggal2: String): List<Pair<String, TransaksiModel>> {
        val snapshot = database.collection("transaksi")
            .whereGreaterThanOrEqualTo("createdAt", tanggal1)
            .whereLessThanOrEqualTo("createdAt", tanggal2)
            .orderBy("createdAt")
            .get()
            .await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(TransaksiModel::class.java)?.let {
                doc.id to it
            }
        }
    }
}