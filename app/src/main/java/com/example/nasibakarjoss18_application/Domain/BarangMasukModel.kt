package com.example.nasibakarjoss18_application.Domain

import com.google.firebase.Timestamp
import java.io.Serializable

data class BarangMasukModel(
    var barangId : String = "",
    var barang_masuk : Long = 0,
    var createdAt : Timestamp = Timestamp.now()
) : Serializable
