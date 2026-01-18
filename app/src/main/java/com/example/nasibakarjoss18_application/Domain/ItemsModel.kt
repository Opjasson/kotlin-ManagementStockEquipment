package com.example.nasibakarjoss18_application.Domain

import com.google.firebase.Timestamp
import java.io.Serializable

data class ItemsModel(
    var documentId : String = "",
    var itemId : Long = 0,
    var kategoriId : Long = 0,
    var nama : String = "",
    var deskripsi : String = "",
    var imgUrl : String = "",
    var popular : Boolean = false,
    var jumlahBarang : Long = 0,

    var createdAt : Timestamp? = null
) : Serializable
