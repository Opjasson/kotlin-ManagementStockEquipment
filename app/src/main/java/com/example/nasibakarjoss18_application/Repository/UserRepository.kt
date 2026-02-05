package com.example.nasibakarjoss18_application.Repository

import android.util.Log
import com.example.nasibakarjoss18_application.Domain.UsersModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import okhttp3.Callback

class UserRepository {
    val database = FirebaseFirestore.getInstance()


    fun getUsersByUid (
        onResult: (UsersModel?) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        database.collection("users")
            .document(uid.toString())
            .get()
            .addOnSuccessListener {
                    document ->
                val list = document.toObject(UsersModel::class.java)?.apply {
                    documentId = document.id   // 🔥 isi documentId
                }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    suspend fun getUsersByUserId(userId: String): UsersModel? {
        val doc = database.collection("users")
            .document(userId)
            .get()
            .await()

        return doc.toObject(UsersModel::class.java)?.apply {
            documentId = doc.id
        }
    }

}