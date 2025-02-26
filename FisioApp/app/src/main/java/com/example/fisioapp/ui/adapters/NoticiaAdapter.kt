package com.example.fisioapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.databinding.LayoutNoticiasBinding
import com.example.fisioapp.domain.models.NoticiasModel
import com.squareup.picasso.Picasso

class NoticiaAdapter(
    var lista: MutableList<NoticiasModel>,
    private val irWebNoticia: (NoticiasModel) -> Unit
) : RecyclerView.Adapter<NoticiaViewHolder>() {

    fun actualizarAdapter(it: List<NoticiasModel>) {
        lista = it.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_noticias, parent, false)
        return NoticiaViewHolder(v)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
        holder.render(lista[position], irWebNoticia)
    }
}

class NoticiaViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val binding = LayoutNoticiasBinding.bind(v)

    fun render(noticia: NoticiasModel, irWebNoticia: (NoticiasModel) -> Unit) {
        binding.apply {
            tvTitulo.text = noticia.titulo
            tvDescripcion.text = noticia.descripcion
            tvFecha.text = noticia.fecha.substring(0,10)
            tvEnlace.text = noticia.enlace

            Picasso.get().load(noticia.imagen).into(ivNews)

            btnInfo.setOnClickListener { irWebNoticia(noticia) }
        }
    }
}
