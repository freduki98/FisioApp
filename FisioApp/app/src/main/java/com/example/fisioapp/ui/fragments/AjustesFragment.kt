package com.example.fisioapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.fisioapp.R
import com.example.fisioapp.databinding.FragmentAjustesBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtenemos los argumentos que le pasan desde la activity
        arguments?.let {
            nombre = it.getString("name") ?: ""
            apellidos = it.getString("lastname") ?: ""
            fechaNac = it.getString("birth-date") ?: ""
        }

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

        binding.apply {
            etNombrePerfil.setText(nombre)
            etApellidosPerfil.setText(apellidos)
            etFechaNacRegister.setText(fechaNac)
        }


        // Configuración del Spinner con el array de especialidades
        adapterEspecialidad = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.array_especialidades)
        )
        // Asignamos el adaptador al Spinner
        binding.spinnerEspecialidad.adapter = adapterEspecialidad
        binding.spinnerEspecialidad.isEnabled = binding.swFisio.isChecked

        setListeners()
    }


    // ---------------------------------------------------------------------------------------------

    private fun setListeners() {
        // Habilitamos o deshabilitamos el Spinner según el estado del componente Switch
        binding.swFisio.setOnCheckedChangeListener { _, isChecked ->
            binding.spinnerEspecialidad.isEnabled = isChecked
        }

        // Configuramos el OnClickListener para el EditText de la fecha de nacimiento
        binding.etFechaNacRegister.setOnClickListener {
            binding.etFechaNacRegister.mostrarDatePicker(requireContext())
        }

        binding.btnGuardarDatos.setOnClickListener {
            binding.apply {
                nombre = etNombrePerfil.text.toString()
                apellidos = etApellidosPerfil.text.toString()
                fechaNac = etFechaNacRegister.text.toString()
            }

            // Actualizamos los datos en Firebase con corrutinas
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val userDocRef =
                        db.collection("users").document(auth.currentUser?.email.toString())

                    val datosActualizados = mapOf(
                        "birth-date" to fechaNac,
                        "name" to nombre,
                        "lastname" to apellidos
                    )

                    userDocRef.update(datosActualizados).await()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Datos actualizados correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Error al actualizar datos: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
