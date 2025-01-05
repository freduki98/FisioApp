package com.example.fisioapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.models.AmigoModel

class AmigosAdapter(
    var lista: MutableList<AmigoModel>,
    private val onDeleteClickListener: (String) -> Unit,
    private val onAddClickListener: (String) -> Unit,
    var add: Boolean
): RecyclerView.Adapter<AmigoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.amigo_layout, parent, false)
        return AmigoViewHolder(v)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: AmigoViewHolder, position: Int) {
        holder.render(lista[position], onDeleteClickListener, onAddClickListener, add)
    }
}