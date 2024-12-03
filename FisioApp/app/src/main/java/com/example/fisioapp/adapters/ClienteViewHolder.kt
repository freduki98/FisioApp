package com.example.fisioapp.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.databinding.ClienteLayoutBinding
import com.example.fisioapp.models.ClienteModel

class ClienteViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var binding = ClienteLayoutBinding.bind(v)

    fun render(
        c: ClienteModel,
        borrarCliente: (Int) -> Unit,
        actualizarCliente: (ClienteModel) -> Unit
    ){
        binding.tvDni.text = "Dni: " + c.dni
        binding.tvNombreCompleto.text = "Nombre: " + c.nombre
        binding.tvDireccion.text = "Direccion: " + c.direccion
        binding.tvLesion.text = "Lesión: " + c.lesion
        binding.tvTratamientoActual.text = "Tratamiento: " + c.tratamiento

        binding.btnBorrar.setOnClickListener{
            borrarCliente(adapterPosition)
        }

        binding.btnUpdate.setOnClickListener{
            actualizarCliente(c)
        }
    }
}
