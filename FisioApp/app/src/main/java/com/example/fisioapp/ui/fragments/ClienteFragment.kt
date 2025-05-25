package com.example.fisioapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fisioapp.R
import com.example.fisioapp.databinding.FragmentClienteBinding
import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.ui.activities.AddClienteActivity
import com.example.fisioapp.ui.activities.InfoClienteActivity
import com.example.fisioapp.ui.adapters.ClienteAdapter
import com.example.fisioapp.ui.viewmodels.ClientesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ClienteFragment : Fragment(R.layout.fragment_cliente) {

    private lateinit var binding: FragmentClienteBinding

    // Adapter
    private var lista = mutableListOf<ClienteModel>()
    lateinit var adapter: ClienteAdapter

    private val viewModel: ClientesViewModel by viewModels()

    // Variables de autenticación y base de datos
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var fisio_id = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClienteBinding.inflate(layoutInflater)
        setViewModel()

        // Inicializamos las variables de autenticación y base de datos
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        recogerFisio()
        inhabilitarComponentes()
        setAdapter()
        setViewModel()
        setListener()
        setRecycler()

        return binding.root
    }

    private fun inhabilitarComponentes() {
        binding.fabAdd.isEnabled = false
        binding.searchView.isEnabled = false
    }

    private fun recogerFisio() {
        fisio_id = auth.currentUser?.email.toString()
    }

    private fun setViewModel() {
        viewModel.listadoClientes.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.actualizarAdapter(it.sortedBy { it.nombre })
                binding.fabAdd.isEnabled = true
                binding.searchView.isEnabled = true

                if (it.isEmpty()) {
                    binding.tvNoClientes.visibility = View.VISIBLE
                } else {
                    binding.tvNoClientes.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.clienteEliminado.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                Toast.makeText(requireContext(), "Cliente eliminado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "No se ha podido eliminar el cliente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.exito.observe(viewLifecycleOwner) {
            if (it) {
                binding.ivInternet.visibility = View.INVISIBLE
            } else {
                binding.ivInternet.visibility = View.VISIBLE
                binding.fabAdd.isEnabled = false
                binding.searchView.isEnabled = false
            }
        }

        // Observa el LiveData para los Toasts de error de conexión
        viewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

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
            startActivity(Intent(requireContext(), AddClienteActivity::class.java))
        }
        // listener para el searchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (binding.searchView.isEnabled) {
                    val searchQuery = query?.trim()?.lowercase() ?: ""
                    if (searchQuery.isNotEmpty()) {
                        viewModel.traerCliente(searchQuery, fisio_id)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = newText?.trim()?.lowercase() ?: ""

                if (binding.searchView.isEnabled) {
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
        binding.recyclerViewClientes.layoutManager = LinearLayoutManager(requireContext())
        traerClientes()
    }

    private fun onClickCliente(cliente: ClienteModel) {
        val i = Intent(requireContext(), InfoClienteActivity::class.java)
        i.putExtra("CLIENTE", cliente)
        startActivity(i)
    }


    // ------------------------------------- CRUD CLIENTES -----------------------------------------
    private fun traerClientes() {
        viewModel.traerClientes(fisio_id)
    }


    private fun borrarCliente(c: ClienteModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmar acción")
            .setMessage("¿Está seguro de que desea eliminar al cliente?")
            .setPositiveButton("Aceptar") { dialog, _ ->
                viewModel.deleteCliente(c)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                Toast.makeText(requireContext(), "Cliente no eliminado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .show()
    }

    private fun actualizarCliente(c: ClienteModel) {
        val i = Intent(requireContext(), AddClienteActivity::class.java).apply {
            putExtra("CLIENTE", c)
        }
        startActivity(i)
    }

    // ---------------------------------------------------------------------------------------------


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        traerClientes()
    }

    override fun onResume() {
        super.onResume()
        traerClientes()
    }


}