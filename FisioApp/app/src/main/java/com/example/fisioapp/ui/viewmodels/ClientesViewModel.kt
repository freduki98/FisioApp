package com.example.fisioapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fisioapp.data.repository.ClientesRepository
import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.domain.models.DiagnosticoModel
import com.example.fisioapp.domain.models.DiagnosticoPaciente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

class ClientesViewModel : ViewModel() {
    private val repository = ClientesRepository()

    // Clientes //

    private val _listadoClientes = MutableLiveData<List<ClienteModel>?>()
    val listadoClientes: MutableLiveData<List<ClienteModel>?> = _listadoClientes

    private val _clienteInsertado = MutableLiveData<Boolean>()
    val clienteInsertado: LiveData<Boolean> = _clienteInsertado

    private val _clienteEditado = MutableLiveData<Boolean>()
    val clienteEditado: LiveData<Boolean> = _clienteEditado

    private val _clienteEliminado = MutableLiveData<Boolean>()
    val clienteEliminado: LiveData<Boolean> = _clienteEliminado

    // Diagnosticos //

    private val _diagnosticoInsertado = MutableLiveData<Boolean>()
    val diagnosticoInsertado: LiveData<Boolean> = _diagnosticoInsertado


    private val _diagnosticoEditado = MutableLiveData<Boolean>()
    val diagnosticoEditado: LiveData<Boolean> = _diagnosticoEditado


    private val _diagnosticoEliminado = MutableLiveData<Boolean>()
    val diagnosticoEliminado: LiveData<Boolean> = _diagnosticoEliminado

    private val _historial = MutableLiveData<List<DiagnosticoModel>?>()
    val historial: MutableLiveData<List<DiagnosticoModel>?> = _historial

    private val _listadoDiagnosticos = MutableLiveData<List<DiagnosticoModel>?>()
    val listadoDiagnosticos: MutableLiveData<List<DiagnosticoModel>?> = _listadoDiagnosticos

    private val _diagnosticoPaciente = MutableLiveData<DiagnosticoPaciente?>()
    val diagnosticoPaciente: MutableLiveData<DiagnosticoPaciente?> = _diagnosticoPaciente

    // LiveData para mostrar el mensaje del Toast //

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun traerClientes(fisio_id: String) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                // Establecer un tiempo de espera de 5 segundos
                val resultado = withTimeout(5000) {
                    val datos = repository.getClientesApi(fisio_id)
                    _listadoClientes.postValue(datos)
                }
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en 5 segundos
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ Problemas de red (por ejemplo, no hay conexión)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, respuesta 500, 404, etc.)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Captura cualquier otro error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }


    fun traerCliente(nombre: String, fisio_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Realiza la solicitud a la API
                val datos = repository.getClienteApi(nombre, fisio_id)
                _listadoClientes.postValue(datos)
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }

    fun addCliente(c: ClienteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val exito = repository.addClienteApi(c)

                if (exito) {
                    // Añadir el cliente a la lista local inmediatamente
                    val listaActualizada = _listadoClientes.value?.toMutableList()
                    listaActualizada?.add(c)

                    // Publicar la lista actualizada
                    _listadoClientes.postValue(listaActualizada)
                }

                // Notificar si la inserción fue exitosa
                _clienteInsertado.postValue(exito)
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }


    fun editCliente(c: ClienteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val exito = repository.editPacienteApi(c)

                if (exito) {
                    // Actualizar el cliente en la lista local
                    val listaActualizada = _listadoClientes.value?.toMutableList()

                    // Encontrar el cliente y actualizar sus datos
                    val index = listaActualizada?.indexOfFirst { it.paciente_id == c.paciente_id }
                    if (index != null && index >= 0) {
                        listaActualizada[index] = c
                    }

                    // Publicar la lista actualizada
                    _listadoClientes.postValue(listaActualizada)
                }

                // Notificar si la edición fue exitosa
                _clienteEditado.postValue(exito)
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }

    fun deleteCliente(c: ClienteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val exito = repository.deletePacienteApi(c.paciente_id, c.fisio_id)

                if (exito) {
                    // Eliminar el cliente de la lista local inmediatamente
                    val listaActualizada = _listadoClientes.value?.toMutableList()
                    listaActualizada?.remove(c)

                    // Publicar la lista actualizada
                    _listadoClientes.postValue(listaActualizada)
                }

                // Notificar si la eliminación fue exitosa
                _clienteEliminado.postValue(exito)
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }

    fun getHistorial(paciente_id: String, fisio_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val datos = repository.getHistorialApi(paciente_id, fisio_id)
                _historial.postValue(datos)
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }


    fun getDiagnosticos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val datos = repository.getDiagnosticosApi()
                _listadoDiagnosticos.postValue(datos)
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }

    fun traerDiagnostico(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val datos = repository.getDiagnosticoApi(id)
                _listadoDiagnosticos.postValue(datos)
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }

    fun addDiagnosticoPaciente(diagnosticoPaciente: DiagnosticoPaciente) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val exito = repository.addDiagnosticoPacienteApi(diagnosticoPaciente)

                // Notificar si la inserción fue exitosa
                _diagnosticoInsertado.postValue(exito)
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }


    fun getDiagnosticoPaciente(paciente_id: String, fisio_id: String, diagnostico_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val datos = repository.getDiagnosticoPacienteApi(paciente_id, fisio_id, diagnostico_id)
                diagnosticoPaciente.postValue(datos)
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }

    fun editDiagnosticoPaciente(diagnosticoPaciente: DiagnosticoPaciente) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val exito = repository.editDiagnosticoPacienteApi(diagnosticoPaciente)

                // Notificar si la edición fue exitosa
                _diagnosticoEditado.postValue(exito)
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }

    fun deleteDiagnosticoPaciente(paciente_id: String, fisio_id: String, diagnostico_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val exito = repository.deleteDiagnosticoPacienteApi(paciente_id, fisio_id, diagnostico_id)

                if (exito) {
                    // Elimina el diagnóstico de la lista local inmediatamente
                    val listaActualizada = _listadoDiagnosticos.value?.toMutableList()
                    listaActualizada?.removeIf { it.id == diagnostico_id }
                    _listadoDiagnosticos.postValue(listaActualizada)
                }

                // Notificar si la eliminación fue exitosa
                _diagnosticoEliminado.postValue(exito)
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }


    fun getUltimoDiagnosticoPaciente(paciente_id: String, fisio_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val datos = repository.getUltimoDiagnosticoPacienteApi(paciente_id, fisio_id)

                // Validar si los datos no están vacíos (puedes ajustar según tus campos)
                if (datos != null && !datos.sistema_lesionado.isNullOrBlank() && !datos.zona_afectada.isNullOrBlank()) {
                    listadoDiagnosticos.postValue(listOf(datos))
                } else {
                    listadoDiagnosticos.postValue(emptyList())
                }
            } catch (e: TimeoutCancellationException) {
                // ❌ No hubo respuesta en el tiempo estipulado
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("La solicitud tardó demasiado. Intenta nuevamente.")
                }
            } catch (e: IOException) {
                // ❌ No hay conexión a la red
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("No hay conexión a la red. Verifica tu conexión a internet.")
                }
            } catch (e: HttpException) {
                // ❌ Error del servidor (por ejemplo, 500, 404)
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Error en el servidor. Intenta más tarde.")
                }
            } catch (e: Exception) {
                // ❌ Error general
                withContext(Dispatchers.Main) {
                    _toastMessage.postValue("Ocurrió un error inesperado. Intenta nuevamente.")
                }
            }
        }
    }



}
