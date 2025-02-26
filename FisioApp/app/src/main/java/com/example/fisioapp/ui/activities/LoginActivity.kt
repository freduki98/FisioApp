package com.example.fisioapp.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    // Para iniciar sesion con google
    private var responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val datos = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val cuentaGoogle = datos.getResult(ApiException::class.java)
                    if (cuentaGoogle != null) {
                        val credenciales = GoogleAuthProvider.getCredential(cuentaGoogle.idToken, null)
                        // Iniciamos sesion con google
                        auth.signInWithCredential(credenciales)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    crearUsuarioFireStore()
                                    irActivityApp()
                                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()

                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Hubo un error al iniciar sesión con Google",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }

                } catch (e: ApiException) {
                    Toast.makeText(
                        this,
                        "Hubo un error al iniciar sesión con Google",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private fun crearUsuarioFireStore() {
        email = auth.currentUser?.email.toString()

        // Verificamos si el usuario ya existe
        val userRef = db.collection("users").document(email)
        userRef.get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    // Si el usuario no existe, creamos un nuevo documento en la colección "users" de FireStore
                    userRef.set(
                        mapOf(
                            "user" to email,
                            "register-date" to System.currentTimeMillis(),
                            "name" to nombre,
                            "lastname" to apellidos,
                            "birth-date" to fechaNac,
                            "connections" to mutableListOf<String>()
                        )
                    )

                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Error al guardar el usuario: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al verificar usuario: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    // Registrar el launcher para el registro de cuenta normal
    private val launcherAccount =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                email = data?.getStringExtra("email").toString()
                pass = data?.getStringExtra("pass").toString()
                nombre = data?.getStringExtra("nombre").toString()
                apellidos = data?.getStringExtra("apellidos").toString()
                fechaNac = data?.getStringExtra("fechaNac").toString()

                // Registramos al usuario
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show()
                            // Creamos el usuario en Firestore
                            crearUsuarioFireStore()
                            auth.signInWithEmailAndPassword(email, pass)
                                .addOnCompleteListener {
                                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                                    if (it.isSuccessful) {
                                        irActivityApp()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "No se pudo realizar el inicio de sesión.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "No se pudo crear el usuario", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }


    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    private lateinit var email: String
    private lateinit var pass: String
    private var nombre = ""
    private var apellidos = ""
    private var fechaNac = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        iniciarComponentes()
        setListeners()

    }

    private fun iniciarComponentes() {
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
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
        // Ir a la pantalla de registro con los datos del usuario si son correctos
        val i = Intent(this, RegisterActivity::class.java)
        if (datosCorrectos()) {
            i.apply {
                putExtra("email", email)
                putExtra("pass", pass)
            }
        }
        launcherAccount.launch(i)
    }

    private fun datosCorrectos(): Boolean {

        email = binding.etEmail.text.toString().trim()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Email con formato no valido"
            return false
        }

        pass = binding.etPass.text.toString().trim()
        if (pass.length < 8) {
            binding.etPass.error = "La contraseña debe tener al menos 8 caracteres"
            return false
        }
        return true
    }

    private fun login() {
        if (!datosCorrectos()) {
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
                Toast.makeText(this, "No se pudo realizar el inicio de sesión.", Toast.LENGTH_SHORT)
                    .show()

            }

    }

    private fun irActivityApp() {
        startActivity(Intent(this, AppActivity::class.java))
    }

    override fun onStart() {
        // Para iniciar la sesión directamente si ya hay un usuario logueado
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            irActivityApp()
        }
    }

    // Para que no se pueda volver a la pantalla anterior
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


}