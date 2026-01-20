package com.example.nasibakarjoss18_application.Domain

import java.io.Serializable

data class BarangRekapModel(
    val barangId: String,
    val namaBarang: String,
    val totalMasuk: Int,
    val totalKeluar: Int
) : Serializable
