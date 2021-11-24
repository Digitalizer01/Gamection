package com.example.gamection

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserInfo(
    var fechanacimiento: String? = "",
    var nombre: String? = "",
    var sexo: String? = "",
    var usuario: String? = ""
)