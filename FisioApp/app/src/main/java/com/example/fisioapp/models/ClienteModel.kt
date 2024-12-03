package com.example.fisioapp.models

import java.io.Serializable

data class ClienteModel(
    val dni: String,
    val nombre: String,
    val direccion: String,
    val lesion: String,
    val tratamiento: String
) : Serializable