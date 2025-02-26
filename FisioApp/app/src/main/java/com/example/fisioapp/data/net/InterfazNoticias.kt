package com.example.fisioapp.data.net

import com.example.fisioapp.domain.models.Noticias
import retrofit2.http.GET
import retrofit2.http.Query

interface InterfazNoticias {
    @GET("v2/everything/")

    // Se utiliza suspend para que se pueda ejecutar en un hilo separado
    suspend fun traerNoticiasFisio(

        // Parámetros de la función que serán los parámetros de la URL
        @Query("q") query: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String
    ) : Noticias
}