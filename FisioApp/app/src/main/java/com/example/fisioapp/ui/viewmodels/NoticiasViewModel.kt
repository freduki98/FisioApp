package com.example.fisioapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fisioapp.data.repository.NoticiasRepository
import com.example.fisioapp.domain.models.NoticiasModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

class NoticiasViewModel : ViewModel() {
    private val repository = NoticiasRepository()

    private val _listadoNoticias = MutableLiveData<List<NoticiasModel>>()
    val listadoNoticias: LiveData<List<NoticiasModel>> = _listadoNoticias

    private val _exito = MutableLiveData<Boolean>()
    val exito: LiveData<Boolean> = _exito

    // LiveData para mostrar el mensaje del Toast //

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    // Para lanzar una función suspend hay que usar viewModelScope (Corrutina)
    fun traerNoticiasFisio() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val datos = repository.getNoticiasFisio()
                _listadoNoticias.postValue(datos)
                _exito.postValue(true)

            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en 5 segundos
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
                _exito.postValue(false)
            } catch (e: IOException) {
                // ❌ Problemas de red (por ejemplo, no hay conexión)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
                _exito.postValue(false)
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, respuesta 500, 404, etc.)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
                _exito.postValue(false)
            } catch (e: Exception) {
                // ❌ Captura cualquier otro error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
                _exito.postValue(false)
            }
        }

    }

}