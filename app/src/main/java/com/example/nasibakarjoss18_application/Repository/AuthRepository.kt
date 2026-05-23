package com.example.nasibakarjoss18_application.Repository

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
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null && user.isEmailVerified) {
                    callback(true, user.uid)
                } else {
                    // Jika belum verifikasi, logout paksa
                    auth.signOut()
                    callback(false, "Email Anda belum diverifikasi. Silakan cek kotak masuk email Anda.")
                }
            }
            .addOnFailureListener { e ->
                callback(false, e.message.toString())
            }
    }

    fun registrasi (
        username : String,
        email : String,
        password : String,
        onResult:(Boolean, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                val uid = user?.uid ?: ""
                
                // Simpan data ke Firestore dengan role default "kasir"
                val userMap =  hashMapOf(
                    "username" to username,
                    "email" to email,
                    "role" to "kepala toko"
                )
                database.collection("users").document(uid).set(userMap)
                
                // Kirim Email Verifikasi
                user?.sendEmailVerification()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Logout setelah registrasi agar user harus login & verifikasi dulu
                            auth.signOut()
                            onResult(true, "Registrasi berhasil. Silakan cek email Anda untuk verifikasi.")
                        } else {
                            onResult(false, "Gagal mengirim email verifikasi: ${task.exception?.message}")
                        }
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message.toString())
            }
    }

    fun logout () {
        auth.signOut()
    }

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