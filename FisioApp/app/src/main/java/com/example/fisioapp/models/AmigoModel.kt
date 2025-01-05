package com.example.fisioapp.models

import java.io.Serializable

data class AmigoModel(
    val nombre: String,
    val apellidos: String,
    val user : String,
    val fechaRegistro: String,
) : Serializable