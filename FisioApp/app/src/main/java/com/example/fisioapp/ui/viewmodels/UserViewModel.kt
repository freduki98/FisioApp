package com.example.fisioapp.ui.viewmodels

import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.fisioapp.R
import com.example.fisioapp.domain.models.UserModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserViewModel : ViewModel() {

    // Variables de autenticación y base de datos
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // LiveData para mostrar el mensaje del Toast
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    // LiveData para manejar los datos del fisio
    private val _fisio = MutableLiveData<UserModel>()
    val fisio: LiveData<UserModel> get() = _fisio

    fun traerFisio() {
        // Inicializamos las variables de autenticación y base de datos
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Obtenemos los datos del usuario de FireStore utilizando corrutinas
                var resultado =
                    db.collection("users").document(auth.currentUser?.email.toString()).get()
                        .await()

                var correo = auth.currentUser?.email.toString()
                var nombre = resultado.get("name").toString()
                var apellidos = resultado.get("lastname").toString()
                var fechaNac = resultado.get("birth-date").toString()
                var especialidad = resultado.get("especialidad").toString()

                if(nombre == "null"){
                    nombre = ""
                }
                if(apellidos == "null"){
                    apellidos = ""
                }


                val fisio = UserModel(correo, nombre, apellidos, fechaNac, especialidad)
                _fisio.postValue(fisio)

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // ❌ No hubo respuesta en 10 segundos
                    withContext(Dispatchers.Main) {
                        _toastMessage.postValue("Error al obtener los datos del fisio.")
                    }
                }
            }


        }
    }

    fun actualizarDatosFisio(fisio: UserModel) {

        // Actualizamos los datos en Firebase con corrutinas
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDocRef =
                    db.collection("users").document(fisio.correo)

                val datosActualizados = mapOf(
                    "birth-date" to fisio.fechaNac,
                    "name" to fisio.nombre,
                    "lastname" to fisio.apellidos,
                    "especialidad" to fisio.especialidad
                )

                userDocRef.update(datosActualizados).await()

                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Datos actualizados correctamente")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error al actualizar datos: ${e.message}")
                }
            }
        }

        traerFisio()

    }

}