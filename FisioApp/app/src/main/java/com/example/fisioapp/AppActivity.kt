package com.example.fisioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.fisioapp.databinding.ActivityAppBinding
import com.example.fisioapp.fragments.MenuFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppActivity : AppCompatActivity() {


    private lateinit var binding : ActivityAppBinding
    var fragment = MenuFragment()
    private val bundle = Bundle()


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



        Toast.makeText(this, "HOME", Toast.LENGTH_SHORT).show()

        setFragment()
        setMenuLateral()
    }

    private fun setFragment() {
        inicializarFragment()
        cargarFragment(fragment)
    }

    private fun setMenuLateral() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_clientes -> {
                    irActivityClientes()
                    true
                }

                R.id.item_contactos -> {
                    irActivityContactos()
                    true
                }

                R.id.item_noticias -> {
                    irActivityNoticias()
                    true
                }

                R.id.item_galeria -> {
                    irActivityGaleria()
                    true
                }

                R.id.item_posts -> {
                    irActivityPosts()
                    true
                }

                R.id.item_ajustes -> {
                    irActivityAjustes()
                    true
                }

                else -> {false}
            }
        }
    }


    private fun irActivityContactos() {
        startActivity(Intent(this, ContactosActivity::class.java))
    }

    private fun irActivityPosts() {
        startActivity(Intent(this, CrearPostActivity::class.java))
    }

    private fun irActivityClientes() {
        startActivity(Intent(this, ClientesActivity::class.java))
    }

    private fun irActivityNoticias() {
        startActivity(Intent(this, NoticiasActivity::class.java))
    }

    private fun irActivityGaleria() {
        startActivity(Intent(this, GaleriaActivity::class.java))
    }

    private fun irActivityAjustes() {
        startActivity(Intent(this, AjustesActivity::class.java))
    }


    private fun inicializarFragment() {
        recogerBotonPulsado()
        fragment = MenuFragment().apply {
            arguments = bundle
        }
    }

    private fun recogerBotonPulsado() {
        val datos = intent.extras
        if(datos != null){
            val botonPulsado = datos.getInt("BOTONPULSADO")
            bundle.putInt("BOTONPULSADO", botonPulsado)
        } else {
            bundle.putInt("BOTONPULSADO", 2)
        }
    }

    private fun cargarFragment(fg: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcv_menu, fg)
        }
    }


}