package com.example.fisioapp.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.databinding.MensajeLayoutBinding
import com.example.fisioapp.models.MensajeModel
import com.example.fisioapp.utils.setMarginsDp

class ChatViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var binding = MensajeLayoutBinding.bind(v)

    fun render(mensaje: MensajeModel, user: String){
        binding.tvMensaje.text = mensaje.mensaje
        binding.tvFechaMensaje.text = mensaje.fecha
        binding.tvUser.text = mensaje.user

        if (mensaje.user == user){
            binding.clayoutMsn.background = ColorDrawable(Color.parseColor("#66BB6A"))
            binding.cardLayoutMsn.setMarginsDp(0, 0, 18, 0)
        } else {
            binding.clayoutMsn.background = ColorDrawable(Color.parseColor("#A8BCA8"))
            binding.cardLayoutMsn.setMarginsDp(18, 0, 0, 0)
        }


    }
}