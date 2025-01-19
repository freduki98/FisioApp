package com.example.fisioapp.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.databinding.AmigoLayoutBinding
import com.example.fisioapp.models.AmigoModel

class ContactoViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var binding = AmigoLayoutBinding.bind(v)

    fun render(
        am: AmigoModel,
        irChat: (String) -> Unit,
        onDeleteClickListener: (String) -> Unit,
        onAddClickListener: (String) -> Unit,
        add: Boolean
    ){
        binding.tvUsuarioAmigo.text = "User: " + am.user
        binding.tvNombreAmigo.text = "Nombre: " + am.nombre
        binding.tvApellidosAmigo.text = "Apellidos: " + am.apellidos
        binding.tvFechaRegistro.text = "Fecha de registro: " + am.fechaRegistro

        if(add){
            binding.btnAdd.visibility = View.VISIBLE
            binding.btnEliminar.visibility = View.INVISIBLE
            binding.btnChatear.visibility = View.INVISIBLE
        } else {
            binding.btnAdd.visibility = View.INVISIBLE
            binding.btnEliminar.visibility = View.VISIBLE
            binding.btnChatear.visibility = View.VISIBLE
        }

        binding.btnEliminar.setOnClickListener{
            onDeleteClickListener(am.user)
        }

        binding.btnAdd.setOnClickListener{
            onAddClickListener(am.user)
        }
        binding.btnChatear.setOnClickListener{
            irChat(am.user)
        }



    }
}