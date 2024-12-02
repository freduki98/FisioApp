package com.example.fisioapp

import android.app.Application
import android.content.Context
import com.example.fisioapp.providers.db.MyDatabase

class Aplicacion : Application() {
    companion object{
        const val version = 4
        const val DB = "Base_clientes"
        const val TABLA = "clientes"
        lateinit var context: Context
        lateinit var llave : MyDatabase
    }

    override fun onCreate(){
        super.onCreate()
        context =applicationContext
        llave = MyDatabase()
    }
}