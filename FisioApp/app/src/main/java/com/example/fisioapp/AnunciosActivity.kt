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
import com.example.fisioapp.adapters.NewsAdapter
import com.example.fisioapp.databinding.ActivityAnunciosBinding
import com.example.fisioapp.models.NewsModel
import com.example.fisioapp.providers.NewProvider.newProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnunciosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnunciosBinding
    private var lista= mutableListOf<NewsModel>()
    var adapter = NewsAdapter(lista) // { noticia -> irDetalle(noticia) }
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

        setListeners()
        api = getString(R.string.news_api)
        setRecycler()
        traerNoticias("physical+therapy")
        Toast.makeText(this, "Noticias de Salud por defecto", Toast.LENGTH_SHORT).show()

    }

//    private fun irDetalle(noticia: NewsModel) {
//        val i = Intent(this, DetalleActivity::class.java).apply {
//            // Para pasar una imagen tiene que implementar la interfaz serializable el objeto. En este caso PixaModel
//            putExtra("NOTICIA", noticia)
//        }
//        startActivity(i)
//    }

    private fun setListeners() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?) : Boolean {
                val buscado = query.toString().trim().lowercase()
                if(buscado.isNotEmpty()){
                    traerNoticias(buscado)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun traerNoticias(query: String) {
        // Dispatcher.IO Hilo de entrada y salida de datos
        lifecycleScope.launch(Dispatchers.IO) {
            val datos = newProvider.recuperarNoticias(query, api)
            val lista = datos.body()

            //Volvemos al hilo principal para operar en el programa con las imagenes obtenidas
            withContext(Dispatchers.Main){
                if(datos.isSuccessful){
                    val miLista : MutableList<NewsModel> = lista?.listaNoticiasApiNews ?: emptyList<NewsModel>().toMutableList()
                    adapter.lista = miLista
                    // Avisamos al adapter que se han realizado cambios y que actualice
                    adapter.notifyDataSetChanged()
                } else {
                    // Siempre que mostremos un mensaje hay que referencias en que contexto estamos aunque estemos en el hilo principal
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