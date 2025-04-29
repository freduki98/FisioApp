package com.example.fisioapp.data.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClientesProvider {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://apirestfuldam-g5bug8hcemgxd2c7.spaincentral-01.azurewebsites.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val clienteApiService = retrofit.create(InterfazClientes::class.java)
}