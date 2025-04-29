package com.example.fisioapp.domain.models

import java.io.Serializable

data class DiagnosticoModel(
    val id: String,
    val sistema_lesionado: String,
    val zona_afectada: String
): Serializable

