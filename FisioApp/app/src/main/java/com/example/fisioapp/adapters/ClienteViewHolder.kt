package com.example.fisioapp.adapters

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ClienteLayoutBinding
import com.example.fisioapp.models.ClienteModel

class ClienteViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var binding = ClienteLayoutBinding.bind(v)

    fun render(
        c: ClienteModel,
        borrarArticulo: (Int) -> Unit,
        actualizarArticulo: (ClienteModel) -> Unit
    ){
        binding.tvNombreCompleto.text = c.nombre // + "(${c.id})"
        binding.tvDireccion.text = c.direccion
        binding.tvDni.text = c.dni

        binding.btnBorrar.setOnClickListener{
            borrarArticulo(adapterPosition)
        }

        binding.btnUpdate.setOnClickListener{
            actualizarArticulo(c)
        }
    }
}
