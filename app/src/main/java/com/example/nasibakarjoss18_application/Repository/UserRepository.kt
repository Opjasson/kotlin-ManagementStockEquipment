package com.example.nasibakarjoss18_application.Repository

import android.util.Log
import com.example.nasibakarjoss18_application.Domain.UsersModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
                val user = document.toObject(UsersModel::class.java)
                onResult(user)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

}