package com.example.fisioapp.data.net

import com.example.fisioapp.domain.models.ClienteModel
import com.example.fisioapp.domain.models.DiagnosticoModel
import com.example.fisioapp.domain.models.DiagnosticoPaciente
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface InterfazClientes {

    @GET("pacientes")
    suspend fun getClientes(@Query("fisio_id") fisio_id: String): List<ClienteModel>

    @GET("paciente")
    suspend fun getCliente(
        @Query("nombre") nombre: String,
        @Query("fisio_id") fisio_id: String
    ): List<ClienteModel>

    @POST("new_paciente")
    suspend fun addCliente(@Body cliente: ClienteModel): Response<Unit>

    @PUT("edit_paciente")
    suspend fun editPaciente(@Body cliente: ClienteModel): Response<Unit>

    @DELETE("delete_paciente")
    suspend fun deletePaciente(
        @Query("paciente_id") paciente_id: String,
        @Query("fisio_id") fisio_id: String
    ): Response<Unit>

    @GET("historialPaciente")
    suspend fun getHistorial(
        @Query("paciente_id") paciente_id: String,
        @Query("fisio_id") fisio_id: String
    ): List<DiagnosticoModel>

    @GET("diagnosticosDisponibles")
    suspend fun getDiagnosticos(): List<DiagnosticoModel>

    @GET("diagnosticoById")
    suspend fun getDiagnostico(@Query("id") id: String): List<DiagnosticoModel>

    @POST("new_diagnostico_paciente")
    suspend fun addDiagnosticoPaciente(@Body diagnosticoPaciente: DiagnosticoPaciente): Response<Unit>

    @DELETE("delete_diagnostico_paciente")
    suspend fun deleteDiagnosticoPaciente(
        @Query("paciente_id") paciente_id: String,
        @Query("fisio_id") fisio_id: String,
        @Query("diagnostico_id") diagnostico_id: String
    ): Response<Unit>

    @GET("diagnostico_paciente")
    suspend fun getDiagnosticoPaciente(
        @Query("paciente_id") paciente_id: String,
        @Query("fisio_id") fisio_id: String,
        @Query("diagnostico_id") diagnostico_id: String
    ): DiagnosticoPaciente

    @PUT("edit_diagnostico_paciente")
    suspend fun editDiagnosticoPaciente(@Body diagnosticoPaciente: DiagnosticoPaciente): Response<Unit>

    @GET("ultimo_diagnostico_paciente")
    suspend fun getUltimoDiagnosticoPaciente(
        @Query("paciente_id") paciente_id: String,
        @Query("fisio_id") fisio_id: String
    ): DiagnosticoModel
}