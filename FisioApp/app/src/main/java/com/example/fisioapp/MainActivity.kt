package com.example.fisioapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Para iniciar sesion con google
    private var responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val datos = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try{
                val cuenta = datos.getResult(ApiException::class.java)
                if(cuenta != null){
                    val credenciales = GoogleAuthProvider.getCredential(cuenta.idToken, null)

                    // Credenciales para autenticar al usuario
                    FirebaseAuth.getInstance().signInWithCredential(credenciales)
                        .addOnCompleteListener{
                            if(it.isSuccessful){
                                irActivityApp()
                            }
                        }
                        .addOnFailureListener{
                            Toast.makeText(this, "Hubo un error al iniciar sesión con Google", Toast.LENGTH_SHORT).show()
                        }
                }
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
            } catch (e: ApiException){
                Toast.makeText(this, "Hubo un error al iniciar sesión con Google", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var email : String
    private lateinit var pass : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        setListeners()

    }

    private fun setListeners() {
        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.btnRegister.setOnClickListener {
            register()
        }

        binding.buttonGoogle.setOnClickListener {
            loginGoogle()
        }
    }

    private fun loginGoogle() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_client_id))
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleConf)

        // Para que se pueda cerrar la sesion
        googleClient.signOut()

        // Para iniciar sesion con google
        responseLauncher.launch(googleClient.signInIntent)
    }

    private fun register() {
        if(!datosCorrectos()) {
            return
        }
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Usuario creado con exito", Toast.LENGTH_SHORT).show()
                    login()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Hubo algún error al crear el usuario.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun datosCorrectos(): Boolean {

        email = binding.etEmail.text.toString().trim()
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.error = "Email con formato no valido"
            return false
        }

        pass = binding.etPass.text.toString().trim()
        if(pass.length < 8){
            binding.etPass.error = "La contraseña debe tener al menos 8 caracteres"
            return false
        }
        return true
    }

    private fun login() {
        if(!datosCorrectos()) {
            return
        }
        // Logeamos al usuario
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    irActivityApp()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "No se pudo realizar el inicio de sesión.", Toast.LENGTH_SHORT).show()

            }

    }

    private fun irActivityApp(){
        startActivity(Intent(this, AppActivity::class.java))
    }

    override fun onStart(){

        // Para iniciar la sesión directamente si ya hay un usuario logueado
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            irActivityApp()
        }
    }

    // Para que no se pueda volver a la pantalla anterior
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


}