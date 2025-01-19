package com.example.fisioapp.models

import java.io.Serializable

// Es obligatorio inicializar los valores por defecto
data class MensajeModel (
    val user: String = "",
    val fecha: String = "",
    val mensaje: String = ""
): Serializable