package com.example.gamection

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class JuegoInfo(
    var genero: String? = "",
    var fecha_adicion: String? = "",
)
