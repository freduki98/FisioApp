package com.example.fisioapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.models.ClienteModel

class ClienteAdapter(
    var lista: MutableList<ClienteModel>, private val borrarCliente: (Int) -> Unit, private val actualizarCliente: (ClienteModel) -> Unit): RecyclerView.Adapter<ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cliente_layout, parent, false)
        return ClienteViewHolder(v)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        holder.render(lista[position], borrarCliente, actualizarCliente)
    }
}