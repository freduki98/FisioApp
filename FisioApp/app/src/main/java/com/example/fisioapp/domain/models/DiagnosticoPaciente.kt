package com.example.fisioapp.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DiagnosticoPaciente(
    @SerializedName("paciente_id") var paciente_id: String,
    @SerializedName("fisio_id") var fisio_id: String,
    @SerializedName("diagnostico_id") var diagnostico_id: String,
    @SerializedName("fecha_diagnostico") var fecha_diagnostico: String?,
    @SerializedName("fecha_inicio_tratamiento") var fecha_inicio_tratamiento: String?,
    @SerializedName("fecha_fin_tratamiento") var fecha_fin_tratamiento: String?,
    @SerializedName("sintomas") var sintomas: String,
    @SerializedName("medicamentos") var medicamentos: String
) : Serializable