package com.example.fisioapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fisioapp.R
import com.example.fisioapp.data.db.CrudClientes
import com.example.fisioapp.databinding.ActivityClientesBinding
import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.ui.adapters.ClienteAdapter

class ClientesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientesBinding

    // Adapter
    private var lista = mutableListOf<ClienteModel>()
    lateinit var adapter: ClienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setListener()
        setRecycler("")
    }

    private fun setListener() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
        binding.btnVolver.setOnClickListener{
            finish()
        }

        // listener para el searchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchQuery = query?.trim()?.lowercase() ?: ""
                if (searchQuery.isNotEmpty()) {
                    setRecycler(searchQuery)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = newText?.trim()?.lowercase() ?: ""
                if (searchQuery.isEmpty()) {
                    setRecycler("")
                }
                return true
            }
        })
    }

    private fun setRecycler(nombre: String) {
        binding.recyclerViewClientes.layoutManager = LinearLayoutManager(this)
        traerClientes(nombre)
        // Iniciamos el adapter con la lista de clientes
        adapter = ClienteAdapter(
            lista,
            { position -> borrarCliente(position) },
            { position -> actualizarCliente(position) })
        binding.recyclerViewClientes.adapter = adapter
    }


    // ------------------------------------- CRUD CLIENTES -----------------------------------------
    private fun traerClientes(nombre: String) {
        lista = CrudClientes().read(nombre)
        if (lista.size > 0) {
            binding.ivClientes.visibility = View.INVISIBLE
        } else {
            binding.ivClientes.visibility = View.VISIBLE
        }
    }

    private fun borrarCliente(position: Int) {
        val idCliente = lista[position].id
        lista.removeAt(position)
        if (CrudClientes().borrar(idCliente.toString())) {
            Toast.makeText(this, "Se eliminó con exito el cliente", Toast.LENGTH_SHORT)
                .show()
            adapter.notifyItemRemoved(position)
        } else {
            Toast.makeText(this, "No se ha podido eliminar el cliente", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun actualizarCliente(c: ClienteModel) {
        val i = Intent(this, AddActivity::class.java).apply {
            putExtra("CLIENTE", c)
        }
        startActivity(i)
    }

    // ---------------------------------------------------------------------------------------------


    override fun onRestart() {
        super.onRestart()
        setRecycler("")
    }

}