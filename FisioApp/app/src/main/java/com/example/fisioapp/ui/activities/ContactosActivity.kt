package com.example.fisioapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fisioapp.R
import com.example.fisioapp.ui.adapters.ContactoAdapter
import com.example.fisioapp.databinding.ActivityContactosBinding
import com.example.fisioapp.domain.models.ContactoModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class ContactosActivity : AppCompatActivity() {

    // Binding
    private lateinit var binding: ActivityContactosBinding

    // Variables
    private var add = false
    private var miLista = mutableListOf<ContactoModel>()
    private var adapter = ContactoAdapter(
        miLista,
        { user -> irChat(user) },
        { user -> eliminarContacto(user) },
        { user -> addContacto(user) },
        add
    )

    // Firebase
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityContactosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarComponentes()
        setRecycler()
        setListeners()

    }

    private fun inicializarComponentes() {
        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        recogerDatos()

        if (add) {
            binding.tvContacto.text = getString(R.string.a_adir_contactos)
            binding.fabBack.visibility = FloatingActionButton.INVISIBLE
            binding.etBuscadorContactos.visibility = EditText.VISIBLE
            binding.ivSearchContacto.visibility = ImageView.VISIBLE

        } else {
            actualizarListaContactos()
            binding.fabBack.visibility = FloatingActionButton.VISIBLE
            binding.etBuscadorContactos.visibility = EditText.GONE
            binding.ivSearchContacto.visibility = ImageView.GONE
        }
    }

    private fun recogerDatos() {
        val datos = intent.extras
        if (datos != null) {
            add = datos.getBoolean("ADD")
            adapter.add = add
            adapter.notifyDataSetChanged()
        }
    }

    private fun setListeners() {
        binding.fabBack.setOnClickListener {
            var i = Intent(this, ContactosActivity::class.java)
            i.putExtra("ADD", true)
            startActivity(i)
        }

        // Buscador de contactos
        binding.ivSearchContacto.setOnClickListener {
            val contact = binding.etBuscadorContactos.text
            if (contact.isNotEmpty()) {
                buscarContacto(contact.toString())
            }
        }
        binding.btnSalir.setOnClickListener{
            finish()
        }
    }

    private fun setRecycler() {
        binding.rvContactos.layoutManager = LinearLayoutManager(this)
        binding.rvContactos.adapter = adapter
    }

    private fun obtenerContactos() {
        var listaContactos: MutableList<String>
        // Utilizamos una corrutina para obtener los datos de Firestore
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                var resultado = db.collection("users").document(auth.currentUser?.email.toString())
                    .get()
                    .await()
                listaContactos =
                    (resultado.get("connections") as MutableList<String>?)?.toMutableList() ?: mutableListOf()
                traerDatosContactos(listaContactos)

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ContactosActivity,
                        "Error al intentar obtener los datos de los amigos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun buscarContacto (contacto: String){
        var listaContactos : MutableList<String>
        var contactoBuscado = mutableListOf<String>()
        contactoBuscado.add(contacto)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                var resultado = db.collection("users").document(auth.currentUser?.email.toString())
                    .get()
                    .await()
                listaContactos =
                    (resultado.get("connections") as MutableList<String>?)?.toMutableList()
                        ?: mutableListOf()

                // Si el contacto no está en la lista de contactos, lo mostramos
                if(!listaContactos.contains(contacto)){
                    traerDatosContactos(contactoBuscado)
                }


            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ContactosActivity,
                        "Error al intentar obtener los datos de los amigos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun traerDatosContactos(listaContactos: MutableList<String>) {
        val listaAmigos = mutableListOf<ContactoModel>()

        // Utilizamos una corrutina
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                for (contacto in listaContactos) {
                    var resultado = db.collection("users").document(contacto).get().await()
                    val amigo = ContactoModel(
                        resultado.getString("name").orEmpty(),
                        resultado.getString("lastname").orEmpty(),
                        resultado.getString("user").orEmpty(),
                        resultado.getLong("register-date") ?: 0L
                    )

                    // Añadimos el amigo a la lista
                    listaAmigos.add(amigo)
                }

                withContext(Dispatchers.Main) {
                    if (listaAmigos.isNotEmpty()) {
                        adapter.lista = listaAmigos
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            this@ContactosActivity,
                            "No se encontraron amigos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ContactosActivity,
                        "Error al intentar obtener los datos de los amigos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun eliminarContacto(user: String) {
        val email = auth.currentUser?.email.toString()
        var listaContactos : MutableList<String>

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Obtener datos de Firestore
                val result = db.collection("users").document(email).get().await()
                listaContactos =
                    (result.get("connections") as MutableList<String>?)?.toMutableList()
                        ?: mutableListOf()

                // Eliminar el contacto de la lista
                listaContactos.remove(user)

                // Actualizamos Firestore con la nueva lista
                db.collection("users").document(email).update("connections", listaContactos).await()

                // Actualizamos la lista de contactos
                actualizarListaContactos()


            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ContactosActivity,
                        "Error al intentar obtener los datos de los amigos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    }

    private fun actualizarListaContactos() {
        obtenerContactos()
    }

    private fun addContacto(user: String) {
        val email = auth.currentUser?.email.toString()

        // Corrutina para obtener los datos de Firestore
        lifecycleScope.launch(Dispatchers.IO) {

            try {
                // Obtenemos los datos de Firestore
                val result = db.collection("users").document(email).get().await()
                val listaContactos =
                    (result.get("connections") as MutableList<String>?)?.toMutableList()
                        ?: mutableListOf()

                listaContactos.add(user)

                // Actualizamos la lista de contactos en Firestore
                db.collection("users").document(email)
                    .update("connections", listaContactos).await()

                // Volvemos al contexto principal para llamar para limpiar la lista
                withContext(Dispatchers.Main) {
                    adapter.lista = mutableListOf()
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ContactosActivity,
                        "Error al intentar obtener los datos de los amigos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun irChat(user: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("USER", user)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        if (!add) {
            obtenerContactos()
        }
    }

    // Para que no se pueda volver a la pantalla anterior
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (!add) {
            val intent = Intent(this, AppActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            super.onBackPressed()
        }

    }


}