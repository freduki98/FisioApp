package com.example.fisioapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.models.NoticiasModel

class NoticiasAdapter(var lista: MutableList<NoticiasModel>, private val irWebNoticia: (NoticiasModel) -> Unit): RecyclerView.Adapter<NoticiasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiasViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.noticias_api_layout, parent, false)
        return NoticiasViewHolder(v)
    }

    override fun onBindViewHolder(holder: NoticiasViewHolder, position: Int) {
        val noticia = lista[position]
        holder.render(noticia, irWebNoticia)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

}