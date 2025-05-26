package com.example.fisioapp.data.net

import com.example.fisioapp.utils.Constants.BASE_URL_API_REST_AZURE
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClientesProvider {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_API_REST_AZURE)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val clienteApiService = retrofit.create(InterfazClientes::class.java)
}

// Interfaz para el interceptor
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Obtenemos la solicitud original
        val originalRequest = chain.request()

        // Obtenemos el token de Firebase
        val user = FirebaseAuth.getInstance().currentUser
        val token = runBlocking {
            user?.getIdToken(false)?.await()?.token
        }

        // Si el usuario está autenticado, agregamos el token a la solicitud
        return if (token != null) {
            val modifiedRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(modifiedRequest)
        } else {
            // Si el usuario no está autenticado, pasamos la solicitud sin el token
            chain.proceed(originalRequest)
        }
    }
}