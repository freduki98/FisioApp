package com.example.fisioapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.models.AmigoModel

class ContactoAdapter(
    var lista: MutableList<AmigoModel>,
    private val irChat: (String) -> Unit,
    private val onDeleteClickListener: (String) -> Unit,
    private val onAddClickListener: (String) -> Unit,
    var add: Boolean
): RecyclerView.Adapter<ContactoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.amigo_layout, parent, false)
        return ContactoViewHolder(v)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        holder.render(lista[position], irChat, onDeleteClickListener, onAddClickListener, add)
    }
}