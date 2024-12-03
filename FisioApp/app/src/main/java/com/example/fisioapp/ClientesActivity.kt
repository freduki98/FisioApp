package com.example.fisioapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
        setRecycler()
        title = "Clientes"

    }

    private fun setRecycler() {
        var layoutManager = LinearLayoutManager(this)
        binding.recyclerViewClientes.layoutManager = layoutManager
        traerRegistros()
        adapter = ClienteAdapter(
            lista,
            { position -> borrarCliente(position) },
            { position -> actualizarCliente(position) })
        binding.recyclerViewClientes.adapter = adapter


    }


    private fun borrarCliente(position: Int) {
        val idCliente = lista[position].dni
        // Lo elimino de la lista
        lista.removeAt(position)
        // Lo elimino de la base de datos
        if (CrudClientes().borrar(idCliente)) {
            // Notifico al adapter que se ha eliminado un elemento
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

    private fun traerRegistros() {
        lista = CrudClientes().read()
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
    }



    override fun onRestart() {
        super.onRestart()
        setRecycler()
    }

    // ----------------------------- MENU PRINCIPAL ------------------------------------//


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_salir -> {
                finish()
            }

            R.id.item_borrar_todo -> {
                confirmarBorrado()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun confirmarBorrado() {
        val builder = android.app.AlertDialog.Builder(this)
            .setTitle("¿Borrar clientes?")
            .setMessage("¿Estás seguro de que quieres borrar todos los clientes?")
            .setPositiveButton("ACEPTAR") { _, _ ->
                CrudClientes().borrarTodo()
                setRecycler()
            }
            .setNegativeButton("CANCELAR") { dialog, _ ->
                dialog.dismiss()

            }
            .create()
            .show()
    }
}