package com.example.fisioapp.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.databinding.NoticiasApiLayoutBinding
import com.example.fisioapp.models.NoticiasModel
import com.squareup.picasso.Picasso

class NoticiasViewHolder(v: View):RecyclerView.ViewHolder(v) {
    val binding = NoticiasApiLayoutBinding.bind(v)

    fun render (noticia: NoticiasModel){
        binding.tvTitulo.text = noticia.titulo
        binding.tvDescripcion.text = noticia.descripcion
        binding.tvFecha.text = noticia.fecha.substring(0,10)
        binding.tvEnlace.text = noticia.enlace
        Picasso.get().load(noticia.imagen).into(binding.ivNews)

    }
}