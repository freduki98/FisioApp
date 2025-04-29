package com.example.fisioapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.databinding.LayoutMensajeBinding
import com.example.fisioapp.domain.models.MensajeModel
import com.example.fisioapp.utils.emailExtract
import com.example.fisioapp.utils.fechaFormateada

class ChatAdapter(
    var lista: MutableList<MensajeModel>,
    private val user: String,
) : RecyclerView.Adapter<ChatViewHolder>() {

    // Actualizamos la lista
    fun updateAdapter(lista: MutableList<MensajeModel>) {
        this.lista = lista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_mensaje, parent, false)
        return ChatViewHolder(v)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.render(lista[position], user)
    }
}

class ChatViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val binding = LayoutMensajeBinding.bind(v)

    fun render(mensaje: MensajeModel, user: String) {
        binding.tvMensaje.text = mensaje.mensaje
        binding.tvFecha.text = (mensaje.fecha).fechaFormateada()
        binding.tvEmail.text = (mensaje.user).emailExtract()

        val params = binding.cardViewChat.layoutParams as FrameLayout.LayoutParams

        // Desplazamos el constraint layout a la derecha si el mensaje es del usuario
        if (mensaje.user == user) {
            binding.clChat.setBackgroundColor(binding.tvEmail.context.getColor(R.color.white))
            params.marginStart = 100
            params.marginEnd = 0

            // Desplazamos el constraint layout a la izquierda si el mensaje es del otro usuario
        } else {
            binding.clChat.setBackgroundColor(binding.tvEmail.context.getColor(R.color.gris))
            params.marginStart = 0
            params.marginEnd = 100
        }

    }
}
