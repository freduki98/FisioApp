package com.example.fisioapp.domain.models

import java.io.Serializable

data class ClienteModel(
    val id: Int,
    val dni: String,
    val nombre: String,
    val direccion: String,
    val lesion: String,
    val tratamiento: String
) : Serializable