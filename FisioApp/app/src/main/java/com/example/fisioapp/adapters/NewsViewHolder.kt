package com.example.fisioapp.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.databinding.NewsApiLayoutBinding
import com.example.fisioapp.models.NewsModel
import com.squareup.picasso.Picasso

class NewsViewHolder(v: View):RecyclerView.ViewHolder(v) {
    val binding = NewsApiLayoutBinding.bind(v)

    // fun render (noticia: NewsModel, onItemClick: (NewsModel) -> Unit){
    fun render (noticia: NewsModel){
        binding.tvTitulo.text = noticia.titulo
        binding.tvDescripcion.text = noticia.descripcion
        binding.tvFecha.text = noticia.fecha.substring(0,10)
        binding.tvEnlace.text = noticia.enlace
        Picasso.get().load(noticia.imagen).into(binding.ivNews)

        // Funcionalidad detalle
//        itemView.setOnClickListener{
//            onItemClick(noticia)
//        }
    }
}