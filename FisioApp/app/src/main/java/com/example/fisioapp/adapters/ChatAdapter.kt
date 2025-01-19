package com.example.fisioapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.models.MensajeModel

class ChatAdapter(
    val user: String,
    var lista: MutableList<MensajeModel>
): RecyclerView.Adapter<ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.mensaje_layout, parent, false)
        return ChatViewHolder(v)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.render(lista[position], user)
    }
}