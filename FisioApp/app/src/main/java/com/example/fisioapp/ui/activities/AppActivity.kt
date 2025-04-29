package com.example.fisioapp.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ActivityAppBinding
import com.example.fisioapp.domain.models.UserModel
import com.example.fisioapp.ui.fragments.AjustesFragment
import com.example.fisioapp.ui.fragments.GaleriaFragment
import com.example.fisioapp.ui.fragments.HomeFragment
import com.example.fisioapp.ui.fragments.NoticiasFragment
import com.example.fisioapp.ui.viewmodels.ClientesViewModel
import com.example.fisioapp.ui.viewmodels.UserViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AppActivity : AppCompatActivity(){

    // Variable de binding
    private lateinit var binding: ActivityAppBinding

    // Variables de autenticación y base de datos
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Variables de fisio
    private var correo = ""
    private var nombre = ""
    private var apellidos = ""
    private var fechaNac = ""
    private var especialidad = ""

    private val viewModel : UserViewModel by viewModels()

    private lateinit var fisio : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializamos las variables de autenticación y base de datos
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Cargamos la configuración principal de la activity, el menú lateral y los listeners
        setViewModel()
        setMainConfig()
        setMenuLateral()
        setListeners()
    }

    private fun setViewModel() {
        viewModel.fisio.observe(this) {
            fisio = it
            correo = fisio.correo
            nombre = fisio.nombre
            apellidos = fisio.apellidos
            fechaNac = fisio.fechaNac
            especialidad = fisio.especialidad

            val headerView = binding.navigationView.getHeaderView(0)
            headerView.findViewById<TextView>(R.id.header_title).setText(fisio.nombre + " " + fisio.apellidos)
            headerView.findViewById<TextView>(R.id.header_email).setText(fisio.correo)
        }

        viewModel.traerFisio()
    }

    private fun setMainConfig() {
        navegarDestino("home", "FisioApp")
        window.navigationBarColor = ContextCompat.getColor(this, R.color.background_default)
        binding.bottomNavigation.selectedItemId = R.id.item_home
    }


    private fun setListeners() {
        // Hacemos que el topAppBar muestre el menú lateral
        binding.topAppBar.setNavigationOnClickListener {
            binding.main.openDrawer(binding.navigationView)
        }

        // Hacemos que el bottomNavigation muestre los fragmentos correspondientes
        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.item_noticias -> {
                    navegarDestino("noticias", "Noticias")
                    true
                }

                R.id.item_galeria -> {
                    navegarDestino("galeria", "Galería")
                    true
                }

                R.id.item_home -> {
                    navegarDestino("home", "FisioApp")
                    true
                }

                R.id.item_ajustes -> {
                    navegarDestino("ajustes", "Ajustes")
                    true
                }

                else -> false
            }
        }
    }


    private fun setMenuLateral() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {

                R.id.item_chat -> {
                    navegarDestino("chat", "FisioApp")
                    true
                }

                R.id.item_clientes -> {
                    navegarDestino("clientes", "FisioApp")
                    true
                }


                R.id.item_cerrar_sesion -> {
                    auth.signOut()
                    finish()
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }

                R.id.item_salir -> {
                    finishAffinity()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }


    private fun navegarDestino(destino: String, titulo: String) {
        val fragment = when (destino) {

            // Fragmentos
            "home" -> HomeFragment().apply {
                arguments = Bundle().apply {
                    putString("correo", correo)
                }
            }

            "noticias" -> NoticiasFragment()
            "galeria" -> GaleriaFragment()
            "ajustes" -> AjustesFragment().apply {
                arguments = Bundle().apply {
                    putString("name", nombre)
                    putString("lastname", apellidos)
                    putString("birth-date", fechaNac)
                    putString("especialidad", especialidad)
                }
            }
            else -> null
        }

        // Si es un fragmento, lo cargamos
        if (fragment != null) {
            cargarFragment(fragment)
        }

        // Si es una actividad, la lanzamos
        if (destino == "chat") {
            startActivity(Intent(this, ChatActivity::class.java))
            binding.main.closeDrawer(GravityCompat.START)
        } else if (destino == "clientes") {
            startActivity(Intent(this, ClientesActivity::class.java))
            binding.main.closeDrawer(GravityCompat.START)
        }

        // Le ponemos el título al topAppBar
        binding.topAppBar.title = titulo
    }

    // Cargamos el fragment
    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcv, fragment)
        }
    }


}
