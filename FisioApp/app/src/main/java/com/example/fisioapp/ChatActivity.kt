package com.example.fisioapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fisioapp.adapters.ChatAdapter
import com.example.fisioapp.databinding.ActivityChatBinding
import com.example.fisioapp.models.MensajeModel
import com.example.fisioapp.providers.MensajesProvider
import com.example.fisioapp.utils.encodeNode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter : ChatAdapter
    private var amigo = ""

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private var nodo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        adapter = ChatAdapter(auth.currentUser?.email.toString().encodeNode(), mutableListOf())


        obtenerNodo()

        database = FirebaseDatabase.getInstance().getReference("chat").child(nodo)

        setListeners()
        setRecycler()

    }

    private fun obtenerNodo() {
        if (intent.extras != null) {
            amigo = intent.getStringExtra("USER").toString()
            Log.d("amigo", amigo)
        }

        val user = auth.currentUser?.email.toString()
        val sortedUsers = listOf(user, amigo).sorted()

        // Generar el ID del chat
        val chatId = "chat-${sortedUsers[0]}-${sortedUsers[1]}".encodeNode()

        nodo = chatId
    }



    private fun setListeners() {
        binding.btnEnviarChat.setOnClickListener {
            enviarMensaje()
        }

    }


    private fun enviarMensaje() {
        val mensajeTexto = binding.etMsnChat.text.toString()
        if (mensajeTexto.isNotEmpty()) {
            val mensajeModel = MensajeModel(
                user = auth.currentUser?.email.toString().encodeNode(),
                fecha = Date().toString(),
                mensaje = mensajeTexto
            )

            database.push().setValue(mensajeModel)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show()
                        binding.etMsnChat.text.clear()
                    } else {
                        Toast.makeText(this, "Error al enviar mensaje", Toast.LENGTH_SHORT).show()
                        Log.d("error", task.exception.toString())
                    }
                }
        } else {
            Toast.makeText(this, "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show()
        }

        recuperarMensajesChat()
    }


    private fun recuperarMensajesChat() {
        val chatProvider = MensajesProvider()
        chatProvider.getMensajes(nodo) { todosLosRegistros ->
            binding.imageView.visibility = if (todosLosRegistros.isEmpty()) View.VISIBLE else View.INVISIBLE
            adapter.lista = todosLosRegistros
            adapter.notifyDataSetChanged()
        }
    }


    private fun setRecycler() {
        var layoutManager = LinearLayoutManager(this)
        binding.rvChat.layoutManager = layoutManager
        binding.rvChat.adapter = adapter

        recuperarMensajesChat()


    }

    override fun onResume() {
        super.onResume()
        recuperarMensajesChat()
    }
}