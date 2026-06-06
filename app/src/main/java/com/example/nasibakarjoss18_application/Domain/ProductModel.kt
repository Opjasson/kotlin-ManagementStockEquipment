package com.example.nasibakarjoss18_application.Domain

import java.io.Serializable

data class ProductModel(
    var documentId : String = "",
    var nama_product : String = "",
    var harga_product : Long = 0,
    var deskripsi_product : String = "",
    var imgUrl : String = "",
    var kategori_product : String = "",
    var promo : Boolean = false,
    var stok_product : Long = 0,
    var createdAt : String = ""
) : Serializable
