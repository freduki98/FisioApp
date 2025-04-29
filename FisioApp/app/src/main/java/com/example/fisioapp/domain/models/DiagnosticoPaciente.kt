package com.example.fisioapp.domain.models

import java.io.Serializable

data class DiagnosticoPaciente(
    val paciente_id: String,
    val fisio_id: String,
    val diagnostico_id: String,
    val fecha_diagnostico: String?,
    val fecha_inicio_tratamiento: String?,
    val fecha_fin_tratamiento: String?,
    val sintomas: String,
    val medicamentos: String
) : Serializable