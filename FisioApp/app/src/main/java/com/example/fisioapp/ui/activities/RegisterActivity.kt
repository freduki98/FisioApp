package com.example.fisioapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ActivityRegisterBinding
import com.example.fisioapp.utils.mostrarDatePicker

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    // Datos del usuario
    private var nombre = ""
    private var apellidos = ""
    private var fechaNac = ""
    private var email = ""
    private var pass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recogerDatos()
        setListeners()

    }

    private fun recogerDatos() {
        if (intent.extras != null) {
            email = intent.getStringExtra("email").toString()
            pass = intent.getStringExtra("pass").toString()

            if (email.equals("null")) {
                email = ""
            }
            if (pass.equals("null")) {
                pass = ""
            }

            binding.etCorreoRegister.setText(email)
            binding.etPassRegister.setText(pass)
        }
    }

    private fun setListeners() {
        binding.btnGuardarRegistro.setOnClickListener {
            if (datosCorrectos()) {

                // Si los datos son correctos, devolvemos los datos al login
                val resultIntent = Intent().apply {
                    putExtra("email", email)
                    putExtra("pass", pass)
                    putExtra("nombre", nombre)
                    putExtra("apellidos", apellidos)
                    putExtra("fechaNac", fechaNac)
                }

                setResult(Activity.RESULT_OK, resultIntent)
                finish()

            }
        }
        // Mostrar el DatePickerDialog al hacer clic en el EditText
        binding.etFechaNacRegister.setOnClickListener {
            binding.etFechaNacRegister.mostrarDatePicker(this, true)
        }
    }

    private fun datosCorrectos(): Boolean {
        nombre = binding.etnombreRegister.text.toString().trim()
        if (nombre.isEmpty()) {
            binding.etnombreRegister.error = "El nombre es obligatorio"
            return false
        }

        apellidos = binding.etApellidosRegister.text.toString().trim()
        if (apellidos.isEmpty()) {
            binding.etApellidosRegister.error = "Los apellidos son obligatorios"
            return false
        }

        fechaNac = binding.etFechaNacRegister.text.toString().trim()
        if (fechaNac.isEmpty()) {
            binding.etFechaNacRegister.error = "La fecha de nacimiento es obligatoria"
            return false
        }

        email = binding.etCorreoRegister.text.toString().trim()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etCorreoRegister.error = "Email con formato no valido"
            return false

        }

        pass = binding.etPassRegister.text.toString().trim()
        if (pass.length < 8) {
            binding.etPassRegister.error = "La contraseña debe tener al menos 8 caracteres"
            return false
        }
        return true
    }
}