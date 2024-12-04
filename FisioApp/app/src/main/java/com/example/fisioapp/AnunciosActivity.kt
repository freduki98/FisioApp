package com.example.fisioapp

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fisioapp.adapters.NoticiasAdapter
import com.example.fisioapp.databinding.ActivityAnunciosBinding
import com.example.fisioapp.models.NoticiasModel
import com.example.fisioapp.providers.NoticiasProvider.newProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnunciosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnunciosBinding
    private var lista= mutableListOf<NoticiasModel>()
    var adapter = NoticiasAdapter(lista)
    var api = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAnunciosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        api = getString(R.string.noticias_api)
        setRecycler()
        traerNoticias("fisioterapia")

    }

    private fun traerNoticias(query: String) {

        lifecycleScope.launch(Dispatchers.IO) {
            val datos = newProvider.recuperarNoticias(query, api)
            val lista = datos.body()

            withContext(Dispatchers.Main){
                if(datos.isSuccessful){
                    val miLista : MutableList<NoticiasModel> = lista?.listaNoticiasApiNews ?: emptyList<NoticiasModel>().toMutableList()
                    adapter.lista = miLista
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@AnunciosActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun setRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAnuncios.layoutManager = layoutManager
        binding.recyclerViewAnuncios.adapter = adapter
    }



}