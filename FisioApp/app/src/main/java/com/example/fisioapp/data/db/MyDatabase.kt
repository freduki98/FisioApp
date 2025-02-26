package com.example.fisioapp.data.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabase : SQLiteOpenHelper (Aplicacion.context, Aplicacion.DB, null, Aplicacion.version) {

    // Creación de la tabla
    private val q = "CREATE TABLE ${Aplicacion.TABLA}(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "dni TEXT UNIQUE NOT NULL," +
            "nombre TEXT NOT NULL," +
            "direccion TEXT not null," +
            "lesion TEXT not null," +
            "tratamiento TEXT not null);"

    // Creación de la base de datos
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(q)
    }

    // Actualización de la base de datos
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        if(p2>p1){
            val borrarTabla = "drop table ${Aplicacion.TABLA};"
            p0?.execSQL(borrarTabla)
            onCreate(p0)
        }
    }


}