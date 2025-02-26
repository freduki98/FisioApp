package com.example.fisioapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fisioapp.R
import com.example.fisioapp.ui.activities.WebNoticiaActivity
import com.example.fisioapp.ui.adapters.NoticiaAdapter
import com.example.fisioapp.databinding.FragmentNoticiasBinding
import com.example.fisioapp.domain.models.NoticiasModel
import com.example.fisioapp.ui.viewmodels.NoticiasViewModel

class NoticiasFragment : Fragment(R.layout.fragment_noticias) {

    private lateinit var binding: FragmentNoticiasBinding
    private var lista = mutableListOf<NoticiasModel>()
    private lateinit var adapter: NoticiaAdapter
    private val viewModel : NoticiasViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticiasBinding.inflate(inflater, container, false)

        // Inicializamos el adaptador
        adapter = NoticiaAdapter(lista) { noticia -> irWebNoticia(noticia) }

        // Observamos el LiveData
        viewModel.listadoNoticias.observe(viewLifecycleOwner) {
            adapter.actualizarAdapter(it)
        }

        // Llamamos a la función para traer las noticias
        viewModel.traerNoticiasFisio()

        setRecycler()

        return binding.root
    }


    // ---------------------------------------------------------------------------------------------

    private fun setRecycler() {
        binding.rvNoticias.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNoticias.adapter = adapter
    }


    private fun irWebNoticia(noticia: NoticiasModel) {
        val intent = Intent(requireContext(), WebNoticiaActivity::class.java).apply {
            putExtra("URL", noticia.enlace)
        }
        startActivity(intent)
    }
}
