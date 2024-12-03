package com.example.fisioapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.databinding.ActivityAddBinding
import com.example.fisioapp.models.ClienteModel
import com.example.fisioapp.providers.db.CrudClientes

class AddActivity : AppCompatActivity() {

    val focusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
        if (!hasFocus) {
            comprobarProgreso()

        }
    }

    private lateinit var binding: ActivityAddBinding
    private var id = 0
    private var nombre = ""
    private var direccion = ""
    private var dni = ""
    private var lesion = ""
    private var tratamiento = ""
    private var nombreAddProgress = false
    private var apellidosAddProgress = false
    private var dniAddProgress = false
    private var lesionAddProgress = false
    private var tratamientoAddProgress = false
    var api = ""
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

        api = getString(R.string.noticias_api)
        recogerClientes()
        setListener()

        if(isUpdate){
            binding.etTitle2.text = "Editar cliente"
            binding.btn2Enviar.text = "Editar"
        }



    }

    private fun recogerClientes() {
        val datos = intent.extras
        if (datos != null) {
            val cliente = intent.getSerializableExtra("CLIENTE") as ClienteModel
            isUpdate = true

            id = cliente.id
            dni = cliente.dni
            nombre = cliente.nombre
            direccion = cliente.direccion
            lesion = cliente.lesion
            tratamiento = cliente.tratamiento

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
        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btn2Reset.setOnClickListener {
            limpiar()
        }
        binding.btn2Enviar.setOnClickListener {
            if(datosCorrectos()){

                    val cliente = ClienteModel(id, dni, nombre, direccion, lesion, tratamiento)

                    if (!isUpdate) {
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
                }
            }

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


        if (nombre.isEmpty() || direccion.isEmpty() || dni.trim().isEmpty() || lesion.isEmpty() || tratamiento.isEmpty()) {
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

    private fun limpiar() {
        binding.etNombre.setText("")
        binding.etDireccion.setText("")
        binding.etDni.setText("")
        binding.etLesion.setText("")
        binding.etTratamiento.setText("")

    }

    private fun comprobarProgreso() {
        binding.progressBar.visibility = View.VISIBLE

        dni = binding.etDni.text.toString().trim()
        nombre = binding.etNombre.text.toString().trim()
        direccion = binding.etDireccion.text.toString().trim()
        lesion = binding.etLesion.text.toString().trim()
        tratamiento = binding.etTratamiento.text.toString().trim()

        if(!nombre.isEmpty() && nombre.length >= 5 && !nombreAddProgress){
            binding.progressBar.progress += 20
            nombreAddProgress = true
        } else if(nombre.isEmpty() && nombre.length < 5 && nombreAddProgress){
            binding.progressBar.progress -= 20
        }
        if(!direccion.isEmpty() && direccion.length >= 10 && direccion.length <= 40 && !apellidosAddProgress){
            binding.progressBar.progress += 20
            apellidosAddProgress = true
        } else if(direccion.isEmpty() && (direccion.length < 10 || direccion.length > 40) && apellidosAddProgress){
            binding.progressBar.progress -= 20
            apellidosAddProgress = false
        }
        if(dni.isNotEmpty() && dni.matches(Regex("[0-9]{8}[A-Z]")) && !dniAddProgress){
            binding.progressBar.progress += 20
            dniAddProgress = true
        } else if(dni.isEmpty() && !dni.matches(Regex("[0-9]{8}[A-Z]")) && dniAddProgress){
            binding.progressBar.progress -= 20
            dniAddProgress = false
        }
        if(!lesion.isEmpty() && !lesionAddProgress){
            binding.progressBar.progress += 20
            dniAddProgress = true
        } else if(lesion.isEmpty() && dniAddProgress){
            binding.progressBar.progress -= 20
            dniAddProgress = false
        }
        if(!tratamiento.isEmpty() && !tratamientoAddProgress){
            binding.progressBar.progress += 20
            tratamientoAddProgress = true
        } else if(tratamiento.isEmpty() && tratamientoAddProgress){
            binding.progressBar.progress -= 20
            tratamientoAddProgress = false
        }

    }
}


