package com.example.fisioapp

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
import com.example.fisioapp.adapters.ClienteAdapter
import com.example.fisioapp.databinding.ActivityClienteBinding
import com.example.fisioapp.models.ClienteModel
import com.example.fisioapp.providers.db.CrudClientes

class ClientesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClienteBinding
    private var lista = mutableListOf<ClienteModel>()
    lateinit var adapter: ClienteAdapter

    private var context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setListener()
        setRecycler("")
        title = "Clientes"

    }

    private fun setRecycler(nombre: String) {
        var layoutManager = LinearLayoutManager(context)
        binding.recyclerViewClientes.layoutManager = layoutManager
        traerRegistros(nombre)
        adapter = ClienteAdapter(
            lista,
            { position -> borrarCliente(position) },
            { position -> actualizarCliente(position) })
        binding.recyclerViewClientes.adapter = adapter


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

    private fun traerRegistros(nombre: String) {

        lista = CrudClientes().read(nombre)
            if (lista.size > 0) {
                binding.ivClientes.visibility = View.INVISIBLE
            } else {
                binding.ivClientes.visibility = View.VISIBLE
            }
    }

    private fun setListener() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?) : Boolean {
                val buscado = query.toString().trim().lowercase()
                if(buscado.isNotEmpty()){
                    setRecycler(buscado)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }



    override fun onRestart() {
        super.onRestart()
        setRecycler("")
    }

}