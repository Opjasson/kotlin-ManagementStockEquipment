package com.example.nasibakarjoss18_application.Domain

import java.io.Serializable

data class ItemsModel(
    var itemId : Long = 0,
    var kategoriId : Long = 0,
    var nama : String = "",
    var deskripsi : String = "",
    var imgUrl : String = "",
    var popular : Boolean = false,
) : Serializable
