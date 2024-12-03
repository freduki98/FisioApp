package com.example.fisioapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NoticiasModel(
    @SerializedName("title") var titulo: String,
    @SerializedName("description") var descripcion: String,
    @SerializedName("urlToImage") var imagen: String,
    @SerializedName("url") var enlace: String,
    @SerializedName("publishedAt") var fecha: String
) : Serializable

data class ListaNoticias(
    @SerializedName("articles") val listaNoticiasApiNews: MutableList<NoticiasModel>
)