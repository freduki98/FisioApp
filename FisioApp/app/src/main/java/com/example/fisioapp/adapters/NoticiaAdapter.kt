package com.example.fisioapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.models.NoticiasModel

class NoticiaAdapter(var lista: MutableList<NoticiasModel>, private val irWebNoticia: (NoticiasModel) -> Unit): RecyclerView.Adapter<NoticiaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.noticias_api_layout, parent, false)
        return NoticiaViewHolder(v)
    }

    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
        val noticia = lista[position]
        holder.render(noticia, irWebNoticia)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

}