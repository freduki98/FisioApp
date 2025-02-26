package com.example.fisioapp.domain.models

import java.io.Serializable

data class ContactoModel(
    val nombre: String,
    val apellidos: String,
    val user : String,
    val fechaRegistro: Long,
) : Serializable