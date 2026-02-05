package com.example.nasibakarjoss18_application.Domain

import java.io.Serializable

data class LaporanModel(
    val cartItems: List<LaporanProductModel>
) : Serializable

