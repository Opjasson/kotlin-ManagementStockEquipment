package com.example.nasibakarjoss18_application.Domain

import java.io.Serializable

data class LaporanProductModel(
    val createdAt : String,
    val cartId: String,
    val productId: String,
    val nama: String,
    val harga: Long,
    val kategori: String,
    val jumlah: Long,
    val imgUrl: String
) : Serializable
