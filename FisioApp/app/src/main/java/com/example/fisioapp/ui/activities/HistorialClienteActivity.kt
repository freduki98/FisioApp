package com.example.fisioapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ActivityClienteHistorialBinding
import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.domain.models.DiagnosticoModel
import com.example.fisioapp.ui.adapters.DiagnosticoAdapter
import com.example.fisioapp.ui.viewmodels.ClientesViewModel

class HistorialClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClienteHistorialBinding

    // Adapter
    private var lista = mutableListOf<DiagnosticoModel>()
    lateinit var diagnosticoAdapter: DiagnosticoAdapter

    private lateinit var cliente : ClienteModel

    private val viewModel : ClientesViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializa el binding
        binding = ActivityClienteHistorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_historial)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setAdapter()
        setViewModel()
        recogerCliente()
        setRecycler()
        traerDiagnosticos()
        setListener()

    }

    private fun setListener() {
        binding.fabAddDiagnostico.setOnClickListener {
            var i = Intent(this, AddDiagnosticoActivity::class.java)
            i.putExtra("CLIENTE", cliente)
            startActivity(i)
        }
    }

    private fun recogerCliente() {
        cliente = intent.extras?.getSerializable("CLIENTE") as ClienteModel
    }

    private fun setViewModel() {
        viewModel.historial.observe(this){
            if (it != null) {
                diagnosticoAdapter.actualizarAdapter(it)
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

    private fun setRecycler() {
        binding.recyclerHistorial.layoutManager = LinearLayoutManager(this)
    }


    private fun setAdapter() {
        // Iniciamos el adapter con la lista de clientes
        diagnosticoAdapter = DiagnosticoAdapter(lista, {diagnostico -> onClickDiagnostico(diagnostico)})
        binding.recyclerHistorial.adapter = diagnosticoAdapter
    }

    private fun onClickDiagnostico(diagnostico: DiagnosticoModel) {
        val i = Intent(this, InfoDiagnosticoActivity::class.java)
        i.putExtra("CLIENTE", cliente)
        i.putExtra("DIAGNOSTICO", diagnostico)
        i.putExtra("UPDATE", true)
        startActivity(i)
    }

    private fun traerDiagnosticos() {
        viewModel.getHistorial(cliente.paciente_id, cliente.fisio_id)
    }

    override fun onResume() {
        super.onResume()
        traerDiagnosticos()
    }
}