package com.example.fisioapp

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

        recogerBotonPulsado()
        inicializarFragment()
        cargarFragment(fragment)
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

    private fun inicializarFragment() {
        fragment = MenuFragment().apply {
            arguments = bundle
        }
    }

    private fun cargarFragment(fg: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcv_menu, fg)
        }
    }


}