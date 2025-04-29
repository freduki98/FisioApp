package com.example.fisioapp.data.net

import com.example.fisioapp.utils.Constants.BASE_URL_API_REST_AZURE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClientesProvider {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_API_REST_AZURE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val clienteApiService = retrofit.create(InterfazClientes::class.java)
}