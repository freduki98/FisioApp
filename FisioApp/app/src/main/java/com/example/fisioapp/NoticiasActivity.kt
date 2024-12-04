package com.example.fisioapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fisioapp.adapters.NoticiasAdapter
import com.example.fisioapp.databinding.ActivityAnunciosBinding
import com.example.fisioapp.fragments.MenuFragment
import com.example.fisioapp.models.NoticiasModel
import com.example.fisioapp.providers.NoticiasProvider.newProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoticiasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnunciosBinding

    private var lista= mutableListOf<NoticiasModel>()
    var adapter = NoticiasAdapter(lista)

    var api = ""

    private lateinit var fragment : MenuFragment
    private val bundle = Bundle()

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

        Toast.makeText(this, "Activity Noticias", Toast.LENGTH_SHORT).show()

        api = getString(R.string.noticias_api)

        setRecycler()
        traerNoticias()

        recogerBotonPulsado()
        inicializarFragment()
        cargarFragment(fragment)


    }

    private fun setRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAnuncios.layoutManager = layoutManager
        binding.recyclerViewAnuncios.adapter = adapter
    }

    private fun traerNoticias() {

        // IO es para operaciones de entrada y salida de datos
        lifecycleScope.launch(Dispatchers.IO) {
            val datos = newProvider.recuperarNoticias("fisioterapia","es", api)
            val lista = datos.body()

            // Main es para actualizar la interfaz de usuario
            withContext(Dispatchers.Main){
                if(datos.isSuccessful){
                    val miLista : MutableList<NoticiasModel> = lista?.listaNoticiasApiNews ?: emptyList<NoticiasModel>().toMutableList()
                    adapter.lista = miLista
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@NoticiasActivity, "Error al intentar recuperar las noticias", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun recogerBotonPulsado() {
        val datos = intent.extras
        if(datos != null){
            val botonPulsado = datos.getInt("BOTONPULSADO")
            bundle.putInt("BOTONPULSADO", botonPulsado)
        }
    }

    private fun inicializarFragment() {
        fragment = MenuFragment().apply {
            arguments = bundle
        }
    }

    private fun cargarFragment(fg: MenuFragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcv_menu_anuncios, fg)
        }
    }





}