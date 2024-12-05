package com.example.fisioapp

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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.fisioapp.databinding.ActivityMapaBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var binding: ActivityMapaBinding

    private lateinit var map: GoogleMap
    private val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permisos ->
        if(permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true
            && permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true){
            gestionarLocalizacion()
        } else {
            Toast.makeText(this, "No se han cedido los permisos de ubicación", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_mapa)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        iniciarFragment()


    }

    private fun recogerDatos() {
        val datos = intent.extras

        if(datos != null){
            val latitud = datos.getFloat("LATITUD")
            val longitud = datos.getFloat("LONGITUD")
            colocarMarcador(LatLng(latitud.toDouble(), longitud.toDouble()))
        }
    }


    private fun iniciarFragment() {
        val fragment = SupportMapFragment()
        fragment.getMapAsync(this)
        supportFragmentManager.commit{
            setReorderingAllowed(true)
            add(R.id.fcv_maps, fragment)
        }

    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.uiSettings.isZoomControlsEnabled = true
        map.mapType= GoogleMap.MAP_TYPE_HYBRID
        recogerDatos()
        gestionarLocalizacion()


    }

    private fun gestionarLocalizacion() {
        if(!::map.isInitialized) return
        if(
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        } else {
            pedirPermisos()
        }

    }

    private fun pedirPermisos() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
            ||
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        ){
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
            .setPositiveButton("Aceptar"){
                    dialog,_,-> startActivity(Intent(Settings.ACTION_APPLICATION_SETTINGS))
                dialog.dismiss()
            }
            .create()
            .show()

    }

    private fun colocarMarcador(coordenadas: LatLng) {
        val marker = MarkerOptions().position(coordenadas)
        map.addMarker(marker)
        showAnimacion(coordenadas, 10f)
    }

    private fun showAnimacion(coordenadas: LatLng, zoom: Float) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordenadas, zoom),
            3500,
            null
        )
    }
}