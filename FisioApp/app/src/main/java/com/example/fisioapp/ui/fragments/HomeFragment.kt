package com.example.fisioapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.fisioapp.R
import com.example.fisioapp.databinding.FragmentHomeBinding
import com.example.fisioapp.ui.viewmodels.ClientesViewModel
import com.example.fisioapp.ui.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(R.layout.fragment_home) {


    private lateinit var binding: FragmentHomeBinding
    private var correo: String? = null

    private val viewModel : UserViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Observa cambios en los argumentos
        arguments?.let { bundle ->
            correo = bundle.getString("correo")
        }

        viewModel.fisio.observe(this) { fisio ->
            correo = fisio.correo
            binding.txtNombreUsuario.text = correo
        }

        viewModel.traerFisio()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.txtNombreUsuario.text = correo
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtNombreUsuario.text = correo
    }



}