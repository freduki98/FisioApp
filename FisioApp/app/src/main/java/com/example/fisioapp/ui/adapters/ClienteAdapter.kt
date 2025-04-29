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
    private val borrarCliente: (ClienteModel) -> Unit,
    private val actualizarCliente: (ClienteModel) -> Unit,
    private val onClickCliente: (ClienteModel) -> Unit
) : RecyclerView.Adapter<ClienteViewHolder>() {

    fun actualizarAdapter(list: List<ClienteModel>){
        lista = list.toList().toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_cliente, parent, false)
        return ClienteViewHolder(v)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        holder.render(lista[position], borrarCliente, actualizarCliente, onClickCliente)
    }
}

class ClienteViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val binding = LayoutClienteBinding.bind(v)

    fun render(
        c: ClienteModel,
        borrarCliente: (ClienteModel) -> Unit,
        actualizarCliente: (ClienteModel) -> Unit,
        onClickCliente: (ClienteModel) -> Unit
    ) {
        binding.apply {
            tvNombreCompleto.text = c.nombre + " " + c.apellidos
            tvCorreo.text = c.paciente_id
            tvTelefono.text = c.telefono


            btnBorrar.setOnClickListener {
                borrarCliente(c)
            }

            btnUpdate.setOnClickListener {
                actualizarCliente(c)
            }

            itemView.setOnClickListener {
                onClickCliente(c)
            }
        }
    }
}
