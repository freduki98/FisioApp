package com.example.fisioapp

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.fisioapp.databinding.ActivityAjustesBinding
import com.example.fisioapp.fragments.MenuFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AjustesActivity : AppCompatActivity(),  SeekBar.OnSeekBarChangeListener {

    private lateinit var binding : ActivityAjustesBinding
    private lateinit var preferences : Preferences
    private lateinit var auth : FirebaseAuth

    private lateinit var fragment : MenuFragment
    private val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAjustesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.perfil_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        preferences = Preferences(this)
        comprobarDatos()

        recogerBotonPulsado()
        inicializarFragment()
        cargarFragment(fragment)

        setListeners()
    }

    private fun comprobarDatos() {
        binding.etNombrePerfil.setText(preferences.getName())
        if(preferences.getColor() == "Amarillo"){
            binding.rbColorAmarillo.isChecked = true
            binding.perfilLayout.setBackgroundColor(getColor(R.color.amarillo))
        }else if(preferences.getColor() == "Verde"){
            binding.rbColorVerde.isChecked = true
            binding.perfilLayout.setBackgroundColor(getColor(R.color.verde))

        }
        binding.sbTamanoTitulo.progress = preferences.getTamano()
        binding.tvTituloPerfil .textSize = preferences.getTamano().toFloat()
        binding.etLatitud.setText(preferences.getLatitud().toString())
        binding.etLongitud.setText(preferences.getLongitud().toString())

    }

    private fun setListeners() {
        binding.btnLogoutAjustes.setOnClickListener {
            auth.signOut()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.btnUbicacion.setOnClickListener {
            if(coordenadasCorrectas()){
                val intent = Intent(this, MapaActivity::class.java)
                val bundleMap = Bundle().apply {
                    putFloat("LATITUD", preferences.getLatitud())
                    putFloat("LONGITUD", preferences.getLongitud())
                }
                intent.putExtras(bundleMap)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Las coordenadas no son correctas", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnGuardarDatos.setOnClickListener {
            if(!coordenadasCorrectas()){
                Toast.makeText(this, "Las coordenadas no son correctas", Toast.LENGTH_SHORT).show()
            } else {
                recogerDatos()
            }

        }
        binding.rbColorVerde.setOnClickListener{
            binding.perfilLayout.setBackgroundColor(getColor(R.color.verde))
        }
        binding.rbColorAmarillo.setOnClickListener{
            binding.perfilLayout.setBackgroundColor(getColor(R.color.amarillo))
        }
        binding.sbTamanoTitulo.setOnSeekBarChangeListener(this)


    }

    private fun coordenadasCorrectas(): Boolean {

        try{
            binding.etLatitud.text.toString().trim().toFloat()
            binding.etLongitud.text.toString().trim().toFloat()
        } catch (e: Exception){
            return false
        }

        return true

    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.sb_tamanoTitulo -> {
                binding.tvTituloPerfil.textSize = progress.toFloat()
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }


    private fun recogerDatos() {
        preferences.setName(binding.etNombrePerfil.text.toString())
        if(binding.rbColorAmarillo.isChecked){
            preferences.setColor("Amarillo")
            binding.perfilLayout.setBackgroundColor(getColor(R.color.amarillo))
        } else if(binding.rbColorVerde.isChecked){
            preferences.setColor("Verde")
            binding.perfilLayout.setBackgroundColor(getColor(R.color.verde))
        }
        preferences.setTamano(binding.sbTamanoTitulo.progress)

        if(coordenadasCorrectas()){
            preferences.setLatitud(binding.etLatitud.text.toString().trim().toFloat())
            preferences.setLongitud(binding.etLongitud.text.toString().trim().toFloat())
        } else {
            preferences.setLatitud(0f)
            preferences.setLongitud(0f)
        }
    }

    private fun inicializarFragment() {
        fragment = MenuFragment().apply {
            arguments = bundle
        }
    }

    private fun recogerBotonPulsado() {
        val datos = intent.extras
        if(datos != null){
            val botonPulsado = datos.getInt("BOTONPULSADO")
            bundle.putInt("BOTONPULSADO", botonPulsado)
        }
    }

    private fun cargarFragment(fg: MenuFragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcv_menu_ajustes, fg)
        }
    }


}