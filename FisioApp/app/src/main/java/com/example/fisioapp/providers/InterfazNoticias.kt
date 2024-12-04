package com.example.fisioapp.providers

import com.example.fisioapp.models.ListaNoticias
import retrofit2.http.GET
import retrofit2.http.Query

interface InterfazNoticias {
    @GET("v2/everything/")

    // Se utiliza suspend para que se pueda ejecutar en un hilo separado
    suspend fun recuperarNoticias(
        @Query("q") query: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String
    ) : retrofit2.Response<ListaNoticias>
}