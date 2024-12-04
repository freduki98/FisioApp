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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AppActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAppBinding
    private lateinit var auth : FirebaseAuth
    var fragment = MenuFragment()

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

        auth = Firebase.auth
        binding.tvEmail.text = auth.currentUser?.email.toString()
        setListeners()
        cargarFragment(fragment)
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

    private fun cargarFragment(fg: Fragment) {
        val fgMenu= MenuFragment()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcv_menu, fgMenu)
        }
    }


}