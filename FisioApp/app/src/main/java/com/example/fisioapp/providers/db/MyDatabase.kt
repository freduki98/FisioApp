package com.example.fisioapp.providers.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fisioapp.Aplicacion

class MyDatabase : SQLiteOpenHelper (Aplicacion.context, Aplicacion.DB, null, Aplicacion.version) {

    private val q = "CREATE TABLE ${Aplicacion.TABLA}(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre TEXT NOT NULL CHECK(length(nombre) <= 40)," +
            "direccion TEXT," +
            "dni TEXT UNIQUE NOT NULL CHECK(length(dni) == 9));"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(q)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        if(p2>p1){
            val borrarTabla = "drop table ${Aplicacion.TABLA};"
            p0?.execSQL(borrarTabla)
            onCreate(p0)
        }
    }


}