package com.example.fisioapp.providers

import android.util.Log
import com.example.fisioapp.models.MensajeModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MensajesProvider {
    private val database = FirebaseDatabase.getInstance().getReference("chat")
    fun getMensajes (nodo: String , datosMensajes : (MutableList<MensajeModel>) -> Unit){
        database.child(nodo).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaMensajes = mutableListOf<MensajeModel>()
                for (item in snapshot.children){
                    val mensajes = item.getValue(MensajeModel::class.java)
                    listaMensajes.add(mensajes!!)
                }
                listaMensajes.sortBy { it.fecha }
                datosMensajes(listaMensajes)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al obtener los datos ${error.message}")
            }

        })
    }
}