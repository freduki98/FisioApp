package com.example.fisioapp.providers.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.fisioapp.Aplicacion
import com.example.fisioapp.models.ClienteModel

class CrudClientes {
    fun create (c: ClienteModel): Long{

        // val cv = c.toContentValues()
        val con = Aplicacion.llave.writableDatabase // Abrimos la base de datos en modo escritura

        return try {
            // Insertamos el contacto en la base de datos
            con.insertWithOnConflict(
                Aplicacion.TABLA,
                null,
                c.toContentValues(),
                SQLiteDatabase.CONFLICT_IGNORE
            )

        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        } finally{
            con.close()
        }

    }

    fun read(): MutableList<ClienteModel> {
        val lista = mutableListOf<ClienteModel>()
        val con = Aplicacion.llave.readableDatabase
        try {
            val cursor = con.query(
                Aplicacion.TABLA,
                arrayOf("id", "nombre", "direccion", "dni"),
                null,
                null,
                null,
                null,
                null
            )
            while (cursor.moveToNext()) {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val direccion = cursor.getString(2)
                val dni = cursor.getString(3)
                val cliente = ClienteModel(id, nombre, direccion, dni)

                lista.add(cliente)
            }
        } catch (e: Exception){
            e.printStackTrace()
        } finally {
            con.close()
        }
        return lista
    }

    public fun borrar (id: Int): Boolean{
        val con = Aplicacion.llave.writableDatabase
        val clienteBorrado = con.delete(Aplicacion.TABLA, "id=?", arrayOf(id.toString()))
        con.close()
        return clienteBorrado > 0
    }

    public fun borrarTodo (){
        val con = Aplicacion.llave.writableDatabase
        con.execSQL("delete from ${Aplicacion.TABLA}")
        con.close()
    }

    public fun actualizar (c: ClienteModel): Boolean{
        val con = Aplicacion.llave.writableDatabase
        val values = c.toContentValues()
        var filasActualizadas = 0
        val q = "select id from ${Aplicacion.TABLA} where dni=? AND id <> ?"
        val cursor = con.rawQuery(q, arrayOf(c.dni, c.id.toString()))
        val existeDni = cursor.moveToFirst()
        cursor.close()

        if(!existeDni){
            filasActualizadas = con.update(Aplicacion.TABLA, values,"id=?", arrayOf(c.id.toString()))
        }
        con.close()
        return filasActualizadas > 0
    }

    private fun ClienteModel.toContentValues(): ContentValues{
        return ContentValues().apply {

            // Las claves deben coincidir con los campos de la tabla de la base de datos
            put("nombre", nombre)
            put("direccion", direccion)
            put("dni", dni)

        }
    }
}