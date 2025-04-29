package com.example.fisioapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.databinding.LayoutDiagnosticoBinding
import com.example.fisioapp.domain.models.DiagnosticoModel

class DiagnosticoAdapter(
    private var lista: List<DiagnosticoModel>,
    private var onClickDiagnostico: (DiagnosticoModel) -> Unit
) : RecyclerView.Adapter<DiagnosticoAdapter.DiagnosticoViewHolder>() {

    fun actualizarAdapter(list: List<DiagnosticoModel>){
        lista = list.toList().toMutableList()
        notifyDataSetChanged()
    }

    inner class DiagnosticoViewHolder(val binding: LayoutDiagnosticoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(diagnostico: DiagnosticoModel) {
            binding.tvId.text = diagnostico.id
            binding.tvSistemaLesionado.text = diagnostico.sistema_lesionado
            binding.tvZonaAfectada.text = diagnostico.zona_afectada


            itemView.setOnClickListener {
                onClickDiagnostico(diagnostico)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosticoViewHolder {
        val binding = LayoutDiagnosticoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DiagnosticoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiagnosticoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size
}
