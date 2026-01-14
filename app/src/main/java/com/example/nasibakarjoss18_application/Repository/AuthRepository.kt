package com.example.nasibakarjoss18_application.Repository

import android.content.Context
import android.util.Log
import com.google.android.gms.common.api.internal.StatusCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {
    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUserId() : String? {
        return auth.currentUser?.uid
    }

    fun login (
        email : String,
        password : String,
        callback: (Boolean, String) -> Unit,
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                result ->
                val uid = result.user!!.uid
        callback(true, uid)
        }
            .addOnFailureListener {
                e ->
                callback(false, e.message.toString())
            }
    }

//  Registrasi Repository

    fun registrasi (
        username : String,
        email : String,
        password : String,
        onResult:(Boolean, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                result ->
        val uid = result.user!!.uid
        val user =  hashMapOf(
            "username" to username,
            "email" to email
        )
                database.collection("users").document(uid).set(user)
                onResult(true, "")
            }
            .addOnFailureListener {
                e ->
                onResult(false, e.message.toString())
            }
    }

//    Logout repository
fun logout () {
    auth.signOut()
}

//    Forgot password repository
fun lupaPassword (
    email : String,
    callback: (Boolean) -> Unit
) {
    auth.sendPasswordResetEmail(email)
        .addOnSuccessListener {
            callback(true)
        }
}



}