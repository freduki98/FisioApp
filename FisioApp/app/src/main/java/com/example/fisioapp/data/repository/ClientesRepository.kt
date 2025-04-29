package com.example.fisioapp.data.repository

import com.example.fisioapp.data.net.ClientesProvider
import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.domain.models.DiagnosticoModel
import com.example.fisioapp.domain.models.DiagnosticoPaciente

class ClientesRepository {
    private val apiService = ClientesProvider.clienteApiService

    suspend fun getClientesApi(fisio_id: String): List<ClienteModel> {
        return apiService.getClientes(fisio_id)
    }

    suspend fun getClienteApi(nombre: String, fisio_id: String): List<ClienteModel> {
        return apiService.getCliente(nombre, fisio_id)
    }

    suspend fun addClienteApi(c: ClienteModel): Boolean {
        return try {
            val response = apiService.addCliente(c)
            response.code() == 200
        } catch (e: Exception) {
            false
        }
    }

    suspend fun editPacienteApi(c: ClienteModel): Boolean {
        return try {
            val response = apiService.editPaciente(c)
            response.code() == 200
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deletePacienteApi(paciente_id: String, fisio_id: String): Boolean {
        return try {
            val response = apiService.deletePaciente(paciente_id, fisio_id)
            response.code() == 200
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getHistorialApi(paciente_id: String, fisio_id: String): List<DiagnosticoModel> {
        return apiService.getHistorial(paciente_id, fisio_id)
    }

    suspend fun getDiagnosticosApi(): List<DiagnosticoModel> {
        return apiService.getDiagnosticos()
    }

    suspend fun getDiagnosticoApi(id: String): List<DiagnosticoModel> {
        return apiService.getDiagnostico(id)
    }

    suspend fun addDiagnosticoPacienteApi(diagnosticoPaciente: DiagnosticoPaciente): Boolean {
        return try {
            val response = apiService.addDiagnosticoPaciente(diagnosticoPaciente)
            response.code() == 201
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getDiagnosticoPacienteApi(paciente_id: String, fisio_id: String, diagnostico_id: String): DiagnosticoPaciente {
        return apiService.getDiagnosticoPaciente(paciente_id, fisio_id, diagnostico_id)
    }

    suspend fun editDiagnosticoPacienteApi(diagnosticoPaciente: DiagnosticoPaciente): Boolean {
        return try {
            val response = apiService.editDiagnosticoPaciente(diagnosticoPaciente)
            response.code() == 200
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteDiagnosticoPacienteApi(paciente_id: String, fisio_id: String, diagnostico_id: String): Boolean {
        return try {
            val response = apiService.deleteDiagnosticoPaciente(paciente_id, fisio_id, diagnostico_id)
            response.code() == 200
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUltimoDiagnosticoPacienteApi(paciente_id: String, fisio_id: String): DiagnosticoModel {
        return apiService.getUltimoDiagnosticoPaciente(paciente_id, fisio_id)
    }
}

