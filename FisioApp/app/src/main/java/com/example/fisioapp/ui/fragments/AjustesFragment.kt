package com.example.fisioapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fisioapp.R
import com.example.fisioapp.databinding.FragmentAjustesBinding
import com.example.fisioapp.domain.models.UserModel
import com.example.fisioapp.ui.viewmodels.ClientesViewModel
import com.example.fisioapp.ui.viewmodels.UserViewModel
import com.example.fisioapp.utils.mostrarDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AjustesFragment : Fragment(R.layout.fragment_ajustes) {

    private lateinit var binding: FragmentAjustesBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var adapterEspecialidad: ArrayAdapter<String>

    private var nombre = ""
    private var apellidos = ""
    private var fechaNac = ""
    private var especialidad = ""

    private val viewModel: UserViewModel by viewModels()

    private lateinit var fisio: UserModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de Firebase y Preferences
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAjustesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapters()
        setViewModel()
        viewModel.traerFisio()
        setListeners()
    }

    private fun setAdapters() {
        // Configuración del Spinner con el array de especialidades
        adapterEspecialidad = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.array_especialidades)
        )
        // Asignamos el adaptador al Spinner
        binding.spinnerEspecialidad.adapter = adapterEspecialidad
    }

    private fun setViewModel() {
        viewModel.fisio.observe(viewLifecycleOwner) {
            fisio = it
            binding.apply {
                etNombrePerfil.setText(fisio.nombre)
                etApellidosPerfil.setText(fisio.apellidos)
                etFechaNacRegister.setText(fisio.fechaNac)
                val position =
                    (0 until adapterEspecialidad.count).find { adapterEspecialidad.getItem(it) == fisio.especialidad }
                        ?: -1
                if (position != -1) {
                    spinnerEspecialidad.setSelection(position)
                }
            }

        }

        viewModel.toastMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }


    // ---------------------------------------------------------------------------------------------

    private fun setListeners() {

        // Configuramos el OnClickListener para el EditText de la fecha de nacimiento
        binding.etFechaNacRegister.setOnClickListener {
            binding.etFechaNacRegister.mostrarDatePicker(requireContext())
        }

        binding.btnGuardarDatos.setOnClickListener {
            binding.apply {
                nombre = etNombrePerfil.text.toString()
                apellidos = etApellidosPerfil.text.toString()
                fechaNac = etFechaNacRegister.text.toString()
                especialidad = binding.spinnerEspecialidad.selectedItem.toString()
            }

            viewModel.actualizarDatosFisio(
                UserModel(
                    auth.currentUser?.email.toString(),
                    nombre,
                    apellidos,
                    fechaNac,
                    especialidad
                )
            )
        }

    }
}
