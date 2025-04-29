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
import com.example.fisioapp.databinding.ActivityClienteInfoBinding
import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.domain.models.DiagnosticoModel
import com.example.fisioapp.ui.adapters.DiagnosticoAdapter
import com.example.fisioapp.ui.viewmodels.ClientesViewModel
import com.example.fisioapp.utils.convertirFechaDesdeBaseDeDatos

class InfoClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClienteInfoBinding
    private lateinit var c: ClienteModel

    // Adapter
    private var lista = mutableListOf<DiagnosticoModel>()
    lateinit var diagnosticoAdapter: DiagnosticoAdapter

    private val viewModel: ClientesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityClienteInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recogerCliente()
        pintarDatosCliente()
        setViewModel()
        setAdapter()
        setRecycler()
        setListener()
    }

    private fun setViewModel() {
        viewModel.listadoDiagnosticos.observe(this) {
            if (it != null) {
                diagnosticoAdapter.actualizarAdapter(it)

            }
        }
        viewModel.historial.observe(this) {
            if (it != null) {
                viewModel.getUltimoDiagnosticoPaciente(c.paciente_id, c.fisio_id)
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
        binding.recyclerUltimoDiagnostico.layoutManager = LinearLayoutManager(this)
        viewModel.getHistorial(c.paciente_id, c.fisio_id)
    }


    private fun setAdapter() {
        // Iniciamos el adapter con la lista de clientes
        diagnosticoAdapter =
            DiagnosticoAdapter(lista, { diagnostico -> onClickDiagnostico(diagnostico) })
        binding.recyclerUltimoDiagnostico.adapter = diagnosticoAdapter
    }

    private fun onClickDiagnostico(diagnostico: DiagnosticoModel) {
        val i = Intent(this, InfoDiagnosticoActivity::class.java)
        i.putExtra("CLIENTE", c)
        i.putExtra("DIAGNOSTICO", diagnostico)
        i.putExtra("UPDATE", true)
        startActivity(i)
    }

    private fun setListener() {
        binding.btnHistorialTratamientos.setOnClickListener {
            irHistorial()
        }


    }

    private fun irHistorial() {
        var i = Intent(this, HistorialClienteActivity::class.java)
        i.putExtra("CLIENTE", c)
        startActivity(i)
    }

    private fun pintarDatosCliente() {
        binding.apply {
            tvNombreCompleto.text = c.nombre + " " + c.apellidos
            tvDireccion.text = "Dirección: " + c.direccion
            tvCorreo.text = "Correo electrónico: " + c.paciente_id
            tvTelefono.text = "Teléfono: " + c.telefono
            tvFechaNacimiento.text = "Fecha de nacimiento: " + c.fecha_nacimiento.convertirFechaDesdeBaseDeDatos()
        }
    }

    private fun recogerCliente() {
        c = intent.extras?.getSerializable("CLIENTE") as ClienteModel
    }

    override fun onStart() {
        super.onStart()
        setRecycler()
    }
}