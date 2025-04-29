package com.example.fisioapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fisioapp.domain.models.UserModel
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

    // LiveData para mostrar el mensaje del Toast
    private val _fisio = MutableLiveData<UserModel>()
    val fisio: LiveData<UserModel> get() = _fisio

    fun traerFisio(){
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
}