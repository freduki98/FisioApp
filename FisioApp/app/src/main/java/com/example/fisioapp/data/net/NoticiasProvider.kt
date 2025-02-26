package com.example.fisioapp.data.net

import com.example.fisioapp.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NoticiasProvider {
    val retrofit = Retrofit.Builder().
    baseUrl(BASE_URL).
    addConverterFactory(GsonConverterFactory.create()).
    build()

    // Creamos el servicio para acceder a la API
    val listadoNoticiasService = retrofit.create(InterfazNoticias::class.java)
}