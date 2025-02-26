package com.example.fisioapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.databinding.LayoutContactoBinding
import com.example.fisioapp.domain.models.ContactoModel
import com.example.fisioapp.utils.fechaFormateada

// Clase para el adapter
class ContactoAdapter(
    var lista: MutableList<ContactoModel>,
    private val irChat: (String) -> Unit,
    private val onDeleteClickListener: (String) -> Unit,
    private val onAddClickListener: (String) -> Unit,
    var add: Boolean
) : RecyclerView.Adapter<ContactoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_contacto, parent, false)
        return ContactoViewHolder(v)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        holder.render(lista[position], irChat, onDeleteClickListener, onAddClickListener, add)
    }
}

// Clase para el ViewHolder
class ContactoViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val binding = LayoutContactoBinding.bind(v)

    fun render(
        am: ContactoModel,
        irChat: (String) -> Unit,
        onDeleteClickListener: (String) -> Unit,
        onAddClickListener: (String) -> Unit,
        add: Boolean
    ) {
        // Configurar la vista con los datos del contacto
        binding.apply {
            tvUsuarioAmigo.text = "Usuario: ${am.user}"
            tvNombreAmigo.text = "Nombre: ${am.nombre} ${am.apellidos}"
            tvFechaMensaje.text = "Fecha de registro: ${am.fechaRegistro.fechaFormateada()}"

            // Si add es true, mostramos el botón de añadir, si no lo ocultamos
            if (add) {
                btnAdd.visibility = View.VISIBLE
                btnEliminar.visibility = View.INVISIBLE
                btnChatear.visibility = View.INVISIBLE
            } else {
                btnAdd.visibility = View.GONE
                btnEliminar.visibility = View.VISIBLE
                btnChatear.visibility = View.VISIBLE
            }

            // Configuramos los listeners de los botones
            btnEliminar.setOnClickListener { onDeleteClickListener(am.user) }
            btnAdd.setOnClickListener { onAddClickListener(am.user) }
            btnChatear.setOnClickListener { irChat(am.user) }
        }
    }
}

