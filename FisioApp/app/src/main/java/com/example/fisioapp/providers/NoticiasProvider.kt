package com.example.fisioapp.providers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NoticiasProvider {
    val retrofit = Retrofit.Builder().
    baseUrl("https://newsapi.org/").
    addConverterFactory(GsonConverterFactory.create()).
    build()
    val newProvider = retrofit.create(InterfazNoticias::class.java)
}