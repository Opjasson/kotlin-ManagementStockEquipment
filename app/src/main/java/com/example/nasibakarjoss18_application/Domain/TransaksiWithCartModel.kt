package com.example.nasibakarjoss18_application.Domain

import java.io.Serializable

data class TransaksiWithCartModel(
    val transaksiId: String,
    val transaksi: TransaksiModel,
    val cartItems: List<HistoryProductModel>
) : Serializable
