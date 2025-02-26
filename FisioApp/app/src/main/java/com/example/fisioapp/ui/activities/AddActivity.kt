package com.example.fisioapp.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ActivityAddBinding
import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.data.db.CrudClientes

class AddActivity : AppCompatActivity() {

    // Listener para el cambio de foco en los EditText
    val focusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
        if (!hasFocus) {
            comprobarProgreso()
        }
    }

    // Variable de binding
    private lateinit var binding: ActivityAddBinding

    // Variables de cliente
    private var id = 0
    private var nombre = ""
    private var direccion = ""
    private var dni = ""
    private var lesion = ""
    private var tratamiento = ""
    private var dolor = ""

    // Variables de progreso
    private var nombreAddProgress = false
    private var apellidosAddProgress = false
    private var dniAddProgress = false
    private var lesionAddProgress = false
    private var tratamientoAddProgress = false

    // Variable de actualización
    private var isUpdate = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.progressBar.visibility = View.VISIBLE

        recogerClientes()
        setListener()

        if (isUpdate) {
            binding.etTitle2.text = "Editar cliente"
            binding.btnEnviar.text = "Editar"
        }
    }

    private fun recogerClientes() {
        val datos = intent.extras

        // Comprobamos si se ha enviado un cliente para editarlo
        if (datos != null) {
            val cliente = intent.getSerializableExtra("CLIENTE") as ClienteModel
            isUpdate = true

            // Asignamos los valores del cliente a las variables
            id = cliente.id
            dni = cliente.dni
            nombre = cliente.nombre
            direccion = cliente.direccion
            lesion = cliente.lesion
            tratamiento = cliente.tratamiento

            // Mostramos los valores en los EditText
            pintarDatos()
        }
    }

    private fun pintarDatos() {
        binding.etNombre.setText(nombre)
        binding.etDireccion.setText(direccion)
        binding.etDni.setText(dni)
        binding.etLesion.setText(lesion)
        binding.etTratamiento.setText(tratamiento)
    }


    private fun setListener() {
        binding.seekBarDolor.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvDolor.text = "Dolor: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.btnCancelar.setOnClickListener {
            finish()
        }
        binding.btnEnviar.setOnClickListener {
            if (datosCorrectos()) {

                val cliente = ClienteModel(id, dni, nombre, direccion, lesion, tratamiento)

                if (!isUpdate) {
                    // Comprobamos si el cliente ya existe
                    if (CrudClientes().create(cliente) != -1L) {
                        Toast.makeText(this, "Cliente agregado", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "El cliente ya existe.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Actualizamos el cliente si se ha enviado un cliente para editarlo
                    if (CrudClientes().actualizar(cliente)) {
                        Toast.makeText(this, "Cliente actualizado", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "No se ha podido actualizar el cliente. El dni ya existe",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                // Comprobamos el progreso
                comprobarProgreso()
            }
        }


        // Asignamos los listener a los EditText
        binding.etNombre.onFocusChangeListener = focusChangeListener
        binding.etDireccion.onFocusChangeListener = focusChangeListener
        binding.etDni.onFocusChangeListener = focusChangeListener
        binding.etLesion.onFocusChangeListener = focusChangeListener
        binding.etTratamiento.onFocusChangeListener = focusChangeListener
    }


    private fun datosCorrectos(): Boolean {
        dni = binding.etDni.text.toString().trim()
        nombre = binding.etNombre.text.toString().trim()
        direccion = binding.etDireccion.text.toString().trim()
        lesion = binding.etLesion.text.toString().trim()
        tratamiento = binding.etTratamiento.text.toString().trim()
        dolor = binding.tvDolor.text.toString()


        if (nombre.isEmpty() || direccion.isEmpty() || dni.trim()
                .isEmpty() || lesion.isEmpty() || tratamiento.isEmpty()
        ) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return false
        } else if (nombre.length < 5) {
            binding.etNombre.error = "El nombre debe tener al menos 5 caracteres"
            return false
        } else if (direccion.length < 10 || direccion.length > 40) {
            binding.etDireccion.error = "La direccion no tiene la longitud adecuada"
            return false
        } else if (!dni.matches(Regex("[0-9]{8}[A-Z]"))) {
            binding.etDni.error = "El dni debe ser 8 digitos y una letra mayuscula"
            return false
        }

        return true
    }


    private fun comprobarProgreso() {
        dni = binding.etDni.text.toString().trim()
        nombre = binding.etNombre.text.toString().trim()
        direccion = binding.etDireccion.text.toString().trim()
        lesion = binding.etLesion.text.toString().trim()
        tratamiento = binding.etTratamiento.text.toString().trim()

        if (!nombre.isEmpty() && nombre.length >= 5 && !nombreAddProgress) {
            binding.progressBar.progress += 20
            nombreAddProgress = true
        }
        if ((nombre.isEmpty() || nombre.length < 5) && nombreAddProgress) {
            binding.progressBar.progress -= 20
            nombreAddProgress = false
        }
        if (!direccion.isEmpty() && direccion.length >= 10 && direccion.length <= 40 && !apellidosAddProgress) {
            binding.progressBar.progress += 20
            apellidosAddProgress = true
        }
        if ((direccion.isEmpty() || (direccion.length < 10 || direccion.length > 40)) && apellidosAddProgress) {
            binding.progressBar.progress -= 20
            apellidosAddProgress = false
        }
        if (!dni.isEmpty() && dni.matches(Regex("[0-9]{8}[A-Z]")) && !dniAddProgress) {
            binding.progressBar.progress += 20
            dniAddProgress = true
        }
        if ((dni.isEmpty() || !dni.matches(Regex("[0-9]{8}[A-Z]"))) && dniAddProgress) {
            binding.progressBar.progress -= 20
            dniAddProgress = false
        }
        if (!lesion.isEmpty() && !lesionAddProgress) {
            binding.progressBar.progress += 20
            lesionAddProgress = true
        }
        if (lesion.isEmpty() && lesionAddProgress) {
            binding.progressBar.progress -= 20
            lesionAddProgress = false
        }
        if (!tratamiento.isEmpty() && !tratamientoAddProgress) {
            binding.progressBar.progress += 20
            tratamientoAddProgress = true
        }
        if (tratamiento.isEmpty() && tratamientoAddProgress) {
            binding.progressBar.progress -= 20
            tratamientoAddProgress = false
        }

    }
}


