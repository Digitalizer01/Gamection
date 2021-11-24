package com.example.gamection

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ConsolaInfo(
    var juegos: ArrayList<JuegoInfo>?
)
