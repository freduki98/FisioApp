package com.example.fisioapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

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
            binding.etCorreoRegister.setText(email)
            binding.etPassRegister.setText(pass)
        }
    }

    private fun setListeners() {
        binding.btnGuardarRegistro.setOnClickListener {
            if (datosCorrectos()) {

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