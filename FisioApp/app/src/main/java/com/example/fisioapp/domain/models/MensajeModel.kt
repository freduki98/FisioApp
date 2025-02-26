package com.example.fisioapp.domain.models

import java.io.Serializable

data class MensajeModel (
    val user: String = "",
    val mensaje: String = "",
    val fecha: Long = 0L,
): Serializable