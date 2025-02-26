package com.example.fisioapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.databinding.LayoutClienteBinding
import com.example.fisioapp.domain.models.ClienteModel

class ClienteAdapter(
    var lista: MutableList<ClienteModel>,
    private val borrarCliente: (Int) -> Unit,
    private val actualizarCliente: (ClienteModel) -> Unit
) : RecyclerView.Adapter<ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_cliente, parent, false)
        return ClienteViewHolder(v)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        holder.render(lista[position], borrarCliente, actualizarCliente)
    }
}

class ClienteViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val binding = LayoutClienteBinding.bind(v)

    fun render(
        c: ClienteModel,
        borrarCliente: (Int) -> Unit,
        actualizarCliente: (ClienteModel) -> Unit
    ) {
        binding.apply {
            tvDni.text = "Dni: ${c.dni}"
            tvNombreCompleto.text = c.nombre
            tvDireccion.text = "Dirección: ${c.direccion}"
            tvLesion.text = "Lesión: ${c.lesion}"
            tvTratamiento.text = "Tratamiento: ${c.tratamiento}"


            btnBorrar.setOnClickListener {
                borrarCliente(adapterPosition)
            }

            btnUpdate.setOnClickListener {
                actualizarCliente(c)
            }
        }
    }
}
