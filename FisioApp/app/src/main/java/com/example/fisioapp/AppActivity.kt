package com.example.fisioapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.fisioapp.databinding.ActivityAppBinding
import com.example.fisioapp.fragments.MenuFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AppActivity : AppCompatActivity(), OnFragmentActionListener {

    private lateinit var binding : ActivityAppBinding
    private lateinit var auth : FirebaseAuth

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
        auth = Firebase.auth
        binding.tvEmail.text = auth.currentUser?.email.toString()
        setListeners()
        cargarFragments()
    }

    private fun setListeners() {
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            finish()
        }
        binding.btnSalir.setOnClickListener {
            finishAffinity()
        }

    }

    private fun cargarFragments() {
        val fgMenu= MenuFragment()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fcv_menu, fgMenu)
        }
    }

    override fun onClickImagenMenu(btn: Int) {
        if(btn == 0) {
            val i = Intent(this, AnunciosActivity::class.java).apply {
                putExtra("BOTONPULSADO", btn)
            }
            startActivity(i)
        } else if(btn == 1){
            val i= Intent(this, GaleriaActivity::class.java).apply {
                putExtra("BOTONPULSADO", btn)
            }
            startActivity(i)
        } else if(btn == 2){
            val i= Intent(this, ClientesActivity::class.java).apply {
                putExtra("BOTONPULSADO", btn)
            }
            startActivity(i)
        } else if(btn == 3){
//            val i= Intent(this, AjustesActivity::class.java).apply {
//                putExtra("BOTONPULSADO", btn)
//            }
//            startActivity(i)
        }
    }


}