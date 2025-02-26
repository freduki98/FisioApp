package com.example.fisioapp.data.repository

import com.example.fisioapp.data.net.NoticiasProvider
import com.example.fisioapp.domain.models.NoticiasModel
import com.example.fisioapp.utils.Constants.API_KEY
import com.example.fisioapp.utils.Constants.LANGUAGE
import com.example.fisioapp.utils.Constants.QUERY

class NoticiasRepository {
    private val apiService = NoticiasProvider.listadoNoticiasService

    // Función para traer las noticias de la API
    suspend fun getNoticiasFisio (): List<NoticiasModel>{
        return apiService.traerNoticiasFisio(QUERY, LANGUAGE, API_KEY).listaNoticiasApi
    }

}