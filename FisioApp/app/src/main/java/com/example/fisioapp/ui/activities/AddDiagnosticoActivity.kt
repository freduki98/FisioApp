package com.example.fisioapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.example.fisioapp.databinding.ActivityDiagnosticoAddBinding
import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.domain.models.DiagnosticoModel
import com.example.fisioapp.ui.adapters.DiagnosticoAdapter
import com.example.fisioapp.ui.viewmodels.ClientesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddDiagnosticoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiagnosticoAddBinding

    // Adapter
    private var lista = mutableListOf<DiagnosticoModel>()
    lateinit var adapter: DiagnosticoAdapter

    private val viewModel : ClientesViewModel by viewModels()

    // Variables de autenticación y base de datos
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var cliente : ClienteModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDiagnosticoAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializamos las variables de autenticación y base de datos
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        recogeCliente()
        setAdapter()
        setViewModel()
        setListener()
        setRecycler()
    }

    private fun recogeCliente() {
        val bundle = intent.extras
        if (bundle != null) {
            cliente = bundle.getSerializable("CLIENTE") as ClienteModel
        }
    }

    private fun setViewModel() {
        viewModel.listadoDiagnosticos.observe(this){
            if (it != null) {
                adapter.actualizarAdapter(it)
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
        adapter = DiagnosticoAdapter(lista, {diagnostico -> onClickDiagnostico(diagnostico)})
        binding.recyclerViewDiagnosticos.adapter = adapter
    }

    private fun setListener() {

        // listener para el searchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchQuery = query?.trim()?.lowercase() ?: ""
                if (searchQuery.isNotEmpty()) {
                    viewModel.traerDiagnostico(searchQuery)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = newText?.trim()?.lowercase() ?: ""
                if (searchQuery.isNotEmpty()) {
                    viewModel.traerDiagnostico(searchQuery)
                } else {
                    traerDiagnosticos()
                }
                return true
            }
        })
    }

    private fun setRecycler() {
        binding.recyclerViewDiagnosticos.layoutManager = LinearLayoutManager(this)
        traerDiagnosticos()
    }

    private fun onClickDiagnostico(diagnostico: DiagnosticoModel) {
        val i = Intent(this, InfoDiagnosticoActivity::class.java)
        i.putExtra("CLIENTE", cliente)
        i.putExtra("DIAGNOSTICO", diagnostico)
        startActivity(i)
    }

    private fun traerDiagnosticos() {
        viewModel.getDiagnosticos()
    }


    // ---------------------------------------------------------------------------------------------


    override fun onRestart() {
        super.onRestart()
        setRecycler()
    }

}