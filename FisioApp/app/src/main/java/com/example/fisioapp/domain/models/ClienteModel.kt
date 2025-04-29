package com.example.fisioapp.domain.models

import java.io.Serializable

data class ClienteModel(
    val paciente_id: String,
    val nombre: String,
    val apellidos: String,
    val direccion: String,
    val telefono: String,
    val fecha_nacimiento: String,
    val fisio_id: String,
) : Serializable