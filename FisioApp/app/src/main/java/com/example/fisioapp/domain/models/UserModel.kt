package com.example.fisioapp.domain.models

import java.io.Serializable

data class UserModel(
    val correo: String,
    val nombre: String,
    val apellidos: String,
    val fechaNac: String,
    val especialidad: String
) : Serializable