package com.example.fisioapp.models

import java.io.Serializable

data class ClienteModel(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val dni: String
) : Serializable