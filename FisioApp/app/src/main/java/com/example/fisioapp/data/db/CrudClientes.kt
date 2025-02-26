package com.example.fisioapp.data.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.fisioapp.domain.models.ClienteModel

class CrudClientes {
    fun create (c: ClienteModel): Long{
        val con = Aplicacion.llave.writableDatabase
        return try {
            // Insertar el cliente en la base de datos
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


    fun read(nombreCliente: String): MutableList<ClienteModel> {
        val lista = mutableListOf<ClienteModel>()
        val con = Aplicacion.llave.readableDatabase

        // Variables para la consulta
        val nombreCli = nombreCliente;
        var selection: String? = null
        var selectionArgs: Array<String>? = null

        if(nombreCli != ""){
            selection = "nombre like ?"
            selectionArgs = arrayOf("%$nombreCli%")
        }

        try {
            // Ejecución de la consulta
            val cursor = con.query(
                Aplicacion.TABLA,
                arrayOf("id","dni", "nombre", "direccion", "lesion", "tratamiento"),
                selection,
                selectionArgs,
                null,
                null,
                null
            )
            while (cursor.moveToNext()) {
                val id = cursor.getInt(0)
                val dni = cursor.getString(1)
                val nombre = cursor.getString(2)
                val direccion = cursor.getString(3)
                val lesion = cursor.getString(4)
                val tratamiento = cursor.getString(5)
                val cliente = ClienteModel(id, dni, nombre, direccion, lesion, tratamiento)

                // Agregar el cliente a la lista
                lista.add(cliente)
            }
        } catch (e: Exception){
            e.printStackTrace()
        } finally {
            con.close()
        }
        // Devolvemos la lista de clientes
        return lista
    }


    public fun borrar (dni: String): Boolean{
        val con = Aplicacion.llave.writableDatabase
        // Borramos el cliente de la base de datos
        val clienteBorrado = con.delete(Aplicacion.TABLA, "id=?", arrayOf(dni))
        con.close()
        return clienteBorrado > 0
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

    // Función de extensión para convertir un objeto ClienteModel en un ContentValues
    private fun ClienteModel.toContentValues(): ContentValues{
        return ContentValues().apply {
            put("dni", dni)
            put("nombre", nombre)
            put("direccion", direccion)
            put("lesion", lesion)
            put("tratamiento", tratamiento)

        }
    }
}