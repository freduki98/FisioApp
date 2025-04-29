package com.example.fisioapp.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DiagnosticoModel(
    @SerializedName("id") var id: String,
    @SerializedName("sistema_lesionado") var sistema_lesionado: String,
    @SerializedName("zona_afectada") var zona_afectada: String
): Serializable

