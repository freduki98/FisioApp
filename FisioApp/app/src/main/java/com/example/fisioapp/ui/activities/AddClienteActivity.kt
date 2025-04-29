package com.example.fisioapp.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ActivityClienteAddBinding
import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.ui.viewmodels.ClientesViewModel
import com.example.fisioapp.utils.convertirFecha
import com.example.fisioapp.utils.convertirFechaDesdeBaseDeDatos
import com.example.fisioapp.utils.mostrarDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddClienteActivity : AppCompatActivity() {

    // Variable de binding
    private lateinit var binding: ActivityClienteAddBinding

    // Variables de autenticación y base de datos
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Variables de cliente
    private var nombre = ""
    private var apellidos = ""
    private var direccion = ""
    private var telefono = ""
    private var fisio_id = ""
    private var correo_electronico = ""
    private var fecha_nacimiento = ""

    // Variable de actualización
    private var isUpdate = false

    // Variable de ViewModel
    private val viewModel : ClientesViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityClienteAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializamos las variables de autenticación y base de datos
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setViewModel()
        recogerFisio()
        recogerCliente()
        setListener()

        if (isUpdate) {
            binding.tvTitle.text = "Editar cliente"
            binding.btnEnviar.text = "Editar"
            binding.btnEnviar.setBackgroundColor(ContextCompat.getColor(this, R.color.edit))
        }
    }

    private fun setViewModel() {
        viewModel.clienteInsertado.observe(this) { exito ->
            if (exito) {
                Toast.makeText(this, "Cliente insertado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "No se ha podido insertar el cliente", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.clienteEditado.observe(this) { exito ->
            if (exito) {
                Toast.makeText(this, "Cliente editado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "No se ha podido editar el cliente", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun recogerFisio() {
        fisio_id = auth.currentUser?.email.toString()
    }

    private fun recogerCliente() {
        val datos = intent.extras

        // Comprobamos si se ha enviado un cliente para editarlo
        if (datos != null) {
            val cliente = intent.getSerializableExtra("CLIENTE") as ClienteModel
            isUpdate = true

            // Asignamos los valores del cliente a las variables
            nombre = cliente.nombre
            apellidos = cliente.apellidos
            direccion = cliente.direccion
            correo_electronico = cliente.paciente_id
            fecha_nacimiento = cliente.fecha_nacimiento.convertirFechaDesdeBaseDeDatos()
            telefono = cliente.telefono

            // Mostramos los valores en los EditText
            pintarDatos()
        }
    }

    private fun pintarDatos() {
        binding.etNombre.setText(nombre)
        binding.etApellidos.setText(apellidos)
        binding.etDireccion.setText(direccion)
        binding.etTelefono.setText(telefono)
        binding.etCorreoElectronico.setText(correo_electronico)
        binding.etFechaNacRegister.setText(fecha_nacimiento)

    }


    private fun setListener() {

        binding.btnCancelar.setOnClickListener {
            finish()
        }
        binding.btnEnviar.setOnClickListener {
            if (datosCorrectos()) {

                fecha_nacimiento = binding.etFechaNacRegister.text.toString().convertirFecha().toString()

                val cliente = ClienteModel(correo_electronico, nombre, apellidos, direccion, telefono, fecha_nacimiento, fisio_id)

                if (!isUpdate) {
                    viewModel.addCliente(cliente)

                } else {
                    viewModel.editCliente(cliente)
                }
            }
        }

        // Mostrar el DatePickerDialog al hacer clic en el EditText
        binding.etFechaNacRegister.setOnClickListener {
            binding.etFechaNacRegister.mostrarDatePicker(this)
        }
    }


    private fun datosCorrectos(): Boolean {
        nombre = binding.etNombre.text.toString().trim()
        apellidos = binding.etApellidos.text.toString().trim()
        direccion = binding.etDireccion.text.toString().trim()
        correo_electronico = binding.etCorreoElectronico.text.toString().trim()
        fecha_nacimiento = binding.etFechaNacRegister.text.toString()
        telefono = binding.etTelefono.text.toString().trim()


        if (nombre.isEmpty() || direccion.isEmpty() || apellidos.isEmpty()
            || correo_electronico.isEmpty() || fecha_nacimiento.isEmpty() || telefono.isEmpty()
        ) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return false
        } else if (nombre.length < 5) {
            binding.etNombre.error = "El nombre debe tener al menos 5 caracteres"
            return false
        } else if (apellidos.length < 5) {
            binding.etApellidos.error = "El apellido debe tener al menos 5 caracteres"
            return false
        } else if (direccion.length < 10 || direccion.length > 40) {
            binding.etDireccion.error = "La dirección debe tener entre 10 y 40 caracteres"
            return false
        } else if (!correo_electronico.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
            binding.etCorreoElectronico.error = "El correo electrónico no tiene un formato válido"
            return false
        }  else if (!telefono.matches(Regex("[0-9]{9}"))) {
            binding.etTelefono.error = "El teléfono debe tener 9 dígitos"
            return false
        }

        return true
    }

}


