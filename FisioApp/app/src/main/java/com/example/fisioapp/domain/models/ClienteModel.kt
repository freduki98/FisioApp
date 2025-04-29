package com.example.fisioapp.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ClienteModel(
    @SerializedName("paciente_id") var paciente_id: String,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("apellidos") var apellidos: String,
    @SerializedName("direccion") var direccion: String,
    @SerializedName("telefono") var telefono: String,
    @SerializedName("fecha_nacimiento") var fecha_nacimiento: String,
    @SerializedName("fisio_id") var fisio_id: String
) : Serializable