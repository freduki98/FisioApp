package com.example.fisioapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ActivityClientesBinding
import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.ui.adapters.ClienteAdapter
import com.example.fisioapp.ui.viewmodels.ClientesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ClientesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientesBinding

    // Adapter
    private var lista = mutableListOf<ClienteModel>()
    lateinit var adapter: ClienteAdapter

    private val viewModel : ClientesViewModel by viewModels()

    // Variables de autenticación y base de datos
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var fisio_id = ""

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

        // Inicializamos las variables de autenticación y base de datos
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        recogerFisio()
        inhabilitarComponentes()
        setAdapter()
        setViewModel()
        setListener()
        setRecycler()
    }

    private fun inhabilitarComponentes() {
        binding.fabAdd.isEnabled = false
        binding.searchView.isEnabled = false
    }

    private fun recogerFisio() {
        fisio_id = auth.currentUser?.email.toString()
    }

    private fun setViewModel() {
        viewModel.listadoClientes.observe(this){
            if (it != null) {
                adapter.actualizarAdapter(it.sortedBy {it.nombre})
                binding.fabAdd.isEnabled = true
                binding.searchView.isEnabled = true
            }
        }

        viewModel.clienteEliminado.observe(this){ exito ->
            if(exito){
                Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "No se ha podido eliminar el cliente", Toast.LENGTH_SHORT).show()
            }
        }

        // Observa el LiveData para los Toasts
        viewModel.toastMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
            finish()
        })


    }

    private fun setAdapter() {
        // Iniciamos el adapter con la lista de clientes
        adapter = ClienteAdapter(
            lista,
            { cliente -> borrarCliente(cliente) },
            { cliente -> actualizarCliente(cliente) },
            { cliente -> onClickCliente(cliente) })
        binding.recyclerViewClientes.adapter = adapter
    }

    private fun setListener() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddClienteActivity::class.java))
        }
        // listener para el searchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(binding.searchView.isEnabled){
                    val searchQuery = query?.trim()?.lowercase() ?: ""
                    if (searchQuery.isNotEmpty()) {
                        viewModel.traerCliente(searchQuery, fisio_id)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = newText?.trim()?.lowercase() ?: ""

                if(binding.searchView.isEnabled){
                    if (searchQuery.isNotEmpty()) {
                        viewModel.traerCliente(searchQuery, fisio_id)
                    } else {
                        viewModel.traerClientes(fisio_id)
                    }
                }

                return true
            }
        })
    }

    private fun setRecycler() {
        binding.recyclerViewClientes.layoutManager = LinearLayoutManager(this)
        traerClientes()
    }

    private fun onClickCliente(cliente: ClienteModel) {
        val i = Intent(this, InfoClienteActivity::class.java)
        i.putExtra("CLIENTE", cliente)
        startActivity(i)
    }


    // ------------------------------------- CRUD CLIENTES -----------------------------------------
    private fun traerClientes() {
        viewModel.traerClientes(fisio_id)
    }


    private fun borrarCliente(c : ClienteModel) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Confirmar acción")
            .setMessage("¿Está seguro de que desea eliminar al cliente?")
            .setPositiveButton("Aceptar") { dialog, _ ->
                viewModel.deleteCliente(c)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                Toast.makeText(this, "Cliente no eliminado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .show()
    }

    private fun actualizarCliente(c: ClienteModel) {
        val i = Intent(this, AddClienteActivity::class.java).apply {
            putExtra("CLIENTE", c)
        }
        startActivity(i)
    }

    // ---------------------------------------------------------------------------------------------


    override fun onRestart() {
        super.onRestart()
        setRecycler()
    }

}