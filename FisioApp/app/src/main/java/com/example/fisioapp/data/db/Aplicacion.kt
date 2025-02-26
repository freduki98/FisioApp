package com.example.fisioapp.data.db

import android.app.Application
import android.content.Context

class Aplicacion : Application() {

    companion object{
        // Constantes de la base de datos
        const val version = 6
        const val DB = "Base_clientes"
        const val TABLA = "clientes"
        lateinit var context: Context
        lateinit var llave : MyDatabase
    }


    override fun onCreate(){
        super.onCreate()
        // Inicializamos la base de datos
        context =applicationContext
        llave = MyDatabase()
    }
}