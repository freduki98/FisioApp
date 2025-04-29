package com.example.fisioapp.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ActivityDiagnosticoInfoBinding
import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.domain.models.DiagnosticoModel
import com.example.fisioapp.domain.models.DiagnosticoPaciente
import com.example.fisioapp.ui.viewmodels.ClientesViewModel
import com.example.fisioapp.utils.convertirFecha
import com.example.fisioapp.utils.convertirFechaDesdeBaseDeDatos
import com.example.fisioapp.utils.mostrarDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InfoDiagnosticoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiagnosticoInfoBinding

    private lateinit var cliente: ClienteModel
    private lateinit var diagnostico: DiagnosticoModel

    private var update = false

    private val viewModel: ClientesViewModel by viewModels()

    private var fecha_diagnostico: String? = null
    private var fecha_inicio_tratamiento: String? = null
    private var fecha_fin_tratamiento: String? = null
    private lateinit var sintomas: String
    private lateinit var medicamentos: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDiagnosticoInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recogerInfoClienteDiagnostico()


        binding.tvNombreDiagnostico.text = diagnostico?.id
        if (update) {
            binding.btnActualizar.text = getString(R.string.actualizar)
            binding.btnActualizar.setBackgroundColor(ContextCompat.getColor(this, R.color.edit))
            viewModel.getDiagnosticoPaciente(
                cliente!!.paciente_id,
                cliente!!.fisio_id,
                diagnostico!!.id
            )
        } else {
            binding.btnActualizar.text = getString(R.string.a_adir)
            binding.btnActualizar.setBackgroundColor(ContextCompat.getColor(this, R.color.background_default))
            binding.btnEliminar.visibility = android.view.View.GONE
        }

        setViewModel()
        setListener()
    }

    private fun setViewModel() {
        viewModel.diagnosticoInsertado.observe(this) { exito ->
            if (exito) {
                Toast.makeText(this, "Diagnóstico insertado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    "No se ha podido insertar el diagnóstico al paciente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.diagnosticoPaciente.observe(this) {
            if (it != null) {
                binding.etFechaDiagnostico.setText(it.fecha_diagnostico?.convertirFechaDesdeBaseDeDatos())
                binding.etFechaInicioTratamiento.setText(it.fecha_inicio_tratamiento?.convertirFechaDesdeBaseDeDatos())
                binding.etFechaFinTratamiento.setText(it.fecha_fin_tratamiento?.convertirFechaDesdeBaseDeDatos())
                binding.etObservaciones.setText(it.sintomas)
                binding.etMedicamentos.setText(it.medicamentos)

            }
        }
        viewModel.diagnosticoEditado.observe(this){
            if (it) {
                Toast.makeText(this, "Diagnóstico editado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        viewModel.diagnosticoEliminado.observe(this) {
            if (it) {
                Toast.makeText(this, "Diagnóstico eliminado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "No se ha podido eliminar el diagnóstico", Toast.LENGTH_SHORT)
                    .show()
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

    private fun setListener() {
        binding.btnActualizar.setOnClickListener {
            if (!update && comprobarCampos()) {
                val clienteActual = cliente
                val diagnosticoActual = diagnostico

                if (clienteActual != null && diagnosticoActual != null) {
                    val nuevoDiagnostico = DiagnosticoPaciente(
                        paciente_id = clienteActual.paciente_id,
                        fisio_id = clienteActual.fisio_id,
                        diagnostico_id = diagnosticoActual.id,
                        fecha_diagnostico = fecha_diagnostico,
                        fecha_inicio_tratamiento = fecha_inicio_tratamiento,
                        fecha_fin_tratamiento = fecha_fin_tratamiento,
                        sintomas = sintomas,
                        medicamentos = medicamentos
                    )

                    viewModel.addDiagnosticoPaciente(nuevoDiagnostico)
                }
            } else if (update && comprobarCampos()) {
                val clienteActual = cliente
                val diagnosticoActual = diagnostico

                if (clienteActual != null && diagnosticoActual != null) {
                    val nuevoDiagnostico = DiagnosticoPaciente(
                        paciente_id = clienteActual.paciente_id,
                        fisio_id = clienteActual.fisio_id,
                        diagnostico_id = diagnosticoActual.id,
                        fecha_diagnostico = fecha_diagnostico,
                        fecha_inicio_tratamiento = fecha_inicio_tratamiento,
                        fecha_fin_tratamiento = fecha_fin_tratamiento,
                        sintomas = sintomas,
                        medicamentos = medicamentos
                    )
                    viewModel.editDiagnosticoPaciente(nuevoDiagnostico)

                }
            }
        }
        binding.btnEliminar.setOnClickListener {
            eliminarDiagnostico()
        }

        // Mostrar el DatePickerDialog al hacer clic en el EditText
        binding.etFechaDiagnostico.setOnClickListener {
            binding.etFechaDiagnostico.mostrarDatePicker(this)
        }

        binding.etFechaInicioTratamiento.setOnClickListener {
            binding.etFechaInicioTratamiento.mostrarDatePicker(this)
        }

        binding.etFechaFinTratamiento.setOnClickListener {
            binding.etFechaFinTratamiento.mostrarDatePicker(this)
        }
    }

    private fun eliminarDiagnostico() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Confirmar acción")
            .setMessage("¿Está seguro de que desea eliminar el diagnóstico del paciente?")
            .setPositiveButton("Aceptar") { dialog, _ ->
                viewModel.deleteDiagnosticoPaciente(
                    cliente.paciente_id,
                    cliente.fisio_id,
                    diagnostico.id
                )
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                Toast.makeText(this, "Diagnóstico no eliminado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .show()
    }

    private fun comprobarCampos(): Boolean {
        fecha_diagnostico = binding.etFechaDiagnostico.text.toString().convertirFecha()
        fecha_inicio_tratamiento = binding.etFechaInicioTratamiento.text.toString().convertirFecha()
        fecha_fin_tratamiento = binding.etFechaFinTratamiento.text.toString().convertirFecha()
        sintomas = binding.etObservaciones.text.toString()
        medicamentos = binding.etMedicamentos.text.toString()

        if (fecha_diagnostico == null) {
            Toast.makeText(
                this,
                "La fecha del diagnóstico no puede estar vacía",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (sintomas == "") {
            Toast.makeText(this, "Los síntomas no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun recogerInfoClienteDiagnostico() {
        val bundle = intent.extras
        cliente = bundle?.getSerializable("CLIENTE") as ClienteModel
        diagnostico = bundle?.getSerializable("DIAGNOSTICO") as DiagnosticoModel
        update = bundle.getBoolean("UPDATE")
    }
}