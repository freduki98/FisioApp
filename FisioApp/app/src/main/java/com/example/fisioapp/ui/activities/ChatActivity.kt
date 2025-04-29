package com.example.fisioapp.ui.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fisioapp.R
import com.example.fisioapp.ui.adapters.ChatAdapter
import com.example.fisioapp.databinding.ActivityChatBinding
import com.example.fisioapp.domain.models.MensajeModel
import com.example.fisioapp.utils.encodeNode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private var listaChat = mutableListOf<MensajeModel>()
    private lateinit var adapter: ChatAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

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

        inicializarComponentes()
        setListeners()
        setRecycler()
    }

    private fun inicializarComponentes() {
        auth = FirebaseAuth.getInstance()
        // 🔁 NODO FIJO GLOBAL
        database = FirebaseDatabase.getInstance().getReference("chat").child("global")
        adapter = ChatAdapter(listaChat, auth.currentUser?.email.toString().encodeNode())
        binding.tvContacto.text = "CHAT DE FISIOTERAPEUTAS"
    }

    private fun setRecycler() {
        binding.rvChat.layoutManager = LinearLayoutManager(this)
        binding.rvChat.adapter = adapter
    }

    private fun setListeners() {
        binding.btnEnviarChat.setOnClickListener {
            enviarMensaje()
        }

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaChat.clear()
                for (nodo in snapshot.children) {
                    val chat = nodo.getValue(MensajeModel::class.java)
                    listaChat.add(chat!!)
                }
                listaChat.sortBy { it.fecha }
                adapter.updateAdapter(listaChat)
                binding.rvChat.scrollToPosition(listaChat.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.etMsnChat.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                enviarMensaje()
                ocultarTeclado()
                true
            } else {
                false
            }
        }
    }

    private fun ocultarTeclado() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun enviarMensaje() {
        val mensajeTexto = binding.etMsnChat.text.toString()
        if (mensajeTexto.isNotEmpty()) {
            val mensajeModel = MensajeModel(
                user = auth.currentUser?.email.toString().encodeNode(),
                mensaje = mensajeTexto,
                fecha = System.currentTimeMillis()
            )

            database.push().setValue(mensajeModel).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.etMsnChat.text?.clear()
                } else {
                    Toast.makeText(this, "Error al enviar mensaje", Toast.LENGTH_SHORT).show()
                    Log.d("error", task.exception.toString())
                }
            }
        } else {
            Toast.makeText(this, "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show()
        }
    }
}
