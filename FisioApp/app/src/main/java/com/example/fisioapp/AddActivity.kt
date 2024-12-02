package com.example.fisioapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.databinding.ActivityAddBinding
import com.example.fisioapp.models.ClienteModel
import com.example.fisioapp.providers.db.CrudClientes

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private var nombre = ""
    private var direccion = ""
    private var dni = ""
    private var id = -1
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

        api = getString(R.string.news_api)
        recogerArticulos()
        setListener()

        if(isUpdate){
            binding.etTitle2.text = "Editar cliente"
            binding.btn2Enviar.text = "Editar"
        }
    }

    private fun recogerArticulos() {
        val datos = intent.extras
        if (datos != null) {
            val cliente = intent.getSerializableExtra("cliente") as ClienteModel
            isUpdate = true

            id = cliente.id
            nombre = cliente.nombre
            direccion = cliente.direccion
            dni = cliente.dni

            pintarDatos()
        }
    }

    private fun pintarDatos() {
        binding.etNombre.setText(nombre)
        binding.etDireccion.setText(direccion)
        binding.etDni.setText(dni)
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

                    val articulo = ClienteModel(id, nombre, direccion, dni)

                    if (!isUpdate) {
                        if (CrudClientes().create(articulo) != -1L) {
                            Toast.makeText(this, "Cliente agregado", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "No se ha podido agregar el articulo",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        if (CrudClientes().actualizar(articulo)) {
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
        }





    private fun datosCorrectos(): Boolean {
        nombre = binding.etNombre.text.toString().trim()
        direccion = binding.etDireccion.text.toString().trim()

        if(binding.etDni.text.toString().trim().isEmpty()){
            dni = ""
        } else {
            dni = binding.etDni.text.toString().trim()
        }

        if (nombre.isEmpty() || direccion.isEmpty() || dni.toString().trim().isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return false
        } else if (nombre.length < 5) {
            binding.etNombre.error = "El nombre debe tener al menos 5 caracteres"
            return false
        } else if (direccion.length < 10 || direccion.length > 40) {
            binding.etDireccion.error = "La direccion no tiene una longitud adecuada"
            return false
        } else if (dni.length!= 9) {
            binding.etDni.error = "El dni debe ser 8 digitos y una letra mayuscula"
            return false
        }

        return true
    }

    private fun limpiar() {
        binding.etNombre.setText("")
        binding.etDireccion.setText("")
        binding.etDni.setText("")

    }
}