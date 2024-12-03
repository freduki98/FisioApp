package com.example.fisioapp.providers

import com.example.fisioapp.models.ListaNoticias
import retrofit2.http.GET
import retrofit2.http.Query

interface InterfazNoticias {
    @GET("v2/everything/")

    // Hay que utilizar el suspend cuando se quiera hacer un hilo secundario a la app principal para que se realice "por detras"
    suspend fun recuperarNoticias(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ) : retrofit2.Response<ListaNoticias>
}