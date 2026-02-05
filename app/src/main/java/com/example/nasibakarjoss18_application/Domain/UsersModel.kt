package com.example.nasibakarjoss18_application.Domain

import java.io.Serializable

data class UsersModel(
    var documentId : String = "",
    var username : String = "",
    var email : String = "",
    var role : String = ""
): Serializable
