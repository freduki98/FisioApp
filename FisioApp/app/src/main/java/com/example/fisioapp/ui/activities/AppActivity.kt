package com.example.fisioapp.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ActivityAppBinding
import com.example.fisioapp.ui.fragments.AjustesFragment
import com.example.fisioapp.ui.fragments.GaleriaFragment
import com.example.fisioapp.ui.fragments.HomeFragment
import com.example.fisioapp.ui.fragments.NoticiasFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AppActivity : AppCompatActivity() , OnMapReadyCallback{

    // Variable de binding
    private lateinit var binding: ActivityAppBinding

    // Variables de autenticación y base de datos
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Variables de usuario
    private var correo = ""
    private var nombre = ""
    private var apellidos = ""
    private var fechaNac = ""

    // Variables de mapa
    private lateinit var map: GoogleMap
    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permisos ->
            if (permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true
                && permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                gestionarLocalizacion()
            } else {
                Toast.makeText(
                    this,
                    "No se han cedido los permisos de ubicación",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

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
        setMainConfig()
        setMenuLateral()
        setListeners()


    }

    private fun setMainConfig() {
        obtenerDatosUsuario()
        navegarDestino("home", "FisioApp")
        window.navigationBarColor = ContextCompat.getColor(this, R.color.navigationBar)
        binding.bottomNavigation.selectedItemId = R.id.item_home
    }

    private fun obtenerDatosUsuario() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Obtenemos los datos del usuario de FireStore utilizando corrutinas
                var resultado =
                    db.collection("users").document(auth.currentUser?.email.toString()).get()
                        .await()

                correo = auth.currentUser?.email.toString()
                nombre = resultado.get("name").toString()
                apellidos = resultado.get("lastname").toString()
                fechaNac = resultado.get("birth-date").toString()


            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AppActivity,
                        "Error al obtener los datos del usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }
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
                    binding.topAppBar.title = "Noticias"
                    true
                }

                R.id.item_galeria -> {
                    navegarDestino("galeria", "Galería")
                    binding.topAppBar.title = "Galería"
                    true
                }

                R.id.item_home -> {
                    navegarDestino("home", "FisioApp")
                    binding.topAppBar.title = "FisioApp"
                    true
                }

                R.id.item_ajustes -> {
                    navegarDestino("ajustes", "Ajustes")
                    binding.topAppBar.title = "Ajustes"
                    true
                }

                R.id.item_ubicacion -> {
                    navegarDestino("ubicacion", "Ubicación")
                    binding.topAppBar.title = "Ubicación"
                    binding.main.closeDrawer(GravityCompat.START)
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
                    navegarDestino("contactos", "FisioApp")
                    true
                }

                R.id.item_clientes -> {
                    navegarDestino("clientes", "FisioApp")
                    true
                }

                R.id.item_multimedia -> {
                    navegarDestino("multimedia", "FisioApp")
                    true
                }


                R.id.item_cerrar_sesion -> {
                    auth.signOut()
                    finish()
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
                }
            }
            else -> null
        }

        // Si es un fragmento, lo cargamos
        if (fragment != null) {
            cargarFragment(fragment)
        }

        // Si es una actividad, la lanzamos
        if (destino == "contactos") {
            startActivity(Intent(this, ContactosActivity::class.java))
        } else if (destino == "multimedia") {
            startActivity(Intent(this, MultimediaActivity::class.java))
        } else if (destino == "clientes") {
            startActivity(Intent(this, ClientesActivity::class.java))
        } else if (destino == "ubicacion") {
            irMapa()
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

    // ---------------------------------------- MAPA -----------------------------------------------
    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.uiSettings.isZoomControlsEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        gestionarLocalizacion()
    }

    private fun gestionarLocalizacion() {
        if (!::map.isInitialized) return
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        } else {
            pedirPermisos()
        }

    }

    private fun pedirPermisos() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            ||
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

        ) {
            mostrarExplicacion()

        } else {
            escogerPermisos()
        }
    }

    private fun escogerPermisos() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun mostrarExplicacion() {
        AlertDialog.Builder(this)
            .setTitle("Permisos de ubicación")
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, _ ->
                startActivity(Intent(Settings.ACTION_APPLICATION_SETTINGS))
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun irMapa() {
        val fragment = SupportMapFragment()
        fragment.getMapAsync(this)
        cargarFragment(fragment)
    }

}