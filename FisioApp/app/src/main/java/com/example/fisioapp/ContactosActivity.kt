package com.example.fisioapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fisioapp.adapters.ContactoAdapter
import com.example.fisioapp.databinding.ActivityContactosBinding
import com.example.fisioapp.models.AmigoModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class ContactosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactosBinding

    private var add = false

    private var miLista= mutableListOf<AmigoModel>()
    private var adapter = ContactoAdapter(miLista, {user -> irChat(user)}, { user -> eliminarUser(user)}, { user -> addUser(user)}, add)

    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth





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

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        recogerDatos()

        if(add){
            binding.tvAmigos.text = "AÑADIR AMIGOS"
            binding.svAmigos.visibility = SearchView.VISIBLE

        } else {
            binding.svAmigos.visibility = SearchView.INVISIBLE
        }

        setRecycler()
        setListeners()
        if(!add){
            actualizarListaContactos()
        }

    }

    private fun recogerDatos() {
        val datos = intent.extras
        if(datos != null){
            add = datos.getBoolean("ADD")
            adapter.add = add
            adapter.notifyDataSetChanged()
        }
    }

    private fun setListeners() {
        binding.fabAmigos.setOnClickListener {
            var i = Intent(this, ContactosActivity::class.java)
            i.putExtra("ADD", true)
            startActivity(i)
        }
        binding.svAmigos.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?) : Boolean {
                var buscado = query.toString().trim().lowercase()
                if(buscado.isNotEmpty()){
                    obtenerContactos(buscado)

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun obtenerContactos(buscado: String){


        var buscador = buscado

        if(buscador == "" || !add){
            buscador = auth.currentUser?.email.toString()
        }

        var listaContactos: MutableList<String>

        db.collection("users").document(buscador)
            .get()
            .addOnSuccessListener { result ->
                listaContactos = (result.get("connections") as MutableList<String>?)?.toMutableList() ?: mutableListOf()
                traerAmigos(listaContactos)
            }
    }

    private fun setRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvAmigos.layoutManager = layoutManager
        binding.rvAmigos.adapter = adapter
    }

    private fun traerAmigos(listaContactos: MutableList<String>) {
        val listaAmigos = mutableListOf<AmigoModel>()


        lifecycleScope.launch(Dispatchers.IO) {
            try {
                for (contacto in listaContactos) {

                    // Utilizo await() para esperar a que el documento se cargue mediante corrutinas
                    val result = db.collection("users").document(contacto).get().await()
                    val amigo = AmigoModel(
                        result.getString("name").orEmpty(),
                        result.getString("lastname").orEmpty(),
                        result.getString("user").orEmpty(),
                        result.getDate("register-date").toString()
                    )
                    listaAmigos.add(amigo)
                }

                withContext(Dispatchers.Main) {
                    if (listaAmigos.isNotEmpty()) {
                        adapter.lista = listaAmigos
                        adapter.notifyDataSetChanged()
                    } else if(add){
                        Toast.makeText(this@ContactosActivity, "No se encontraron contactos", Toast.LENGTH_SHORT).show()
                    } else if(!add){
                        Toast.makeText(this@ContactosActivity, "No se encontraron amigos", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ContactosActivity, "Error al intentar obtener los datos de los amigos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun eliminarUser(user: String) {
        val email = auth.currentUser?.email.toString()
        var listaContactos = mutableListOf<String>()

        lifecycleScope.launch(Dispatchers.IO) {

            try {

                // Obtener datos de Firestore
                val result = db.collection("users").document(email).get().await()
                listaContactos = (result.get("connections") as MutableList<String>?)?.toMutableList() ?: mutableListOf()

                // Modificar la lista
                listaContactos.remove(user)

                // Actualizar Firestore
                db.collection("users").document(email).update("connections", listaContactos).await()

                actualizarListaContactos()

                if(listaContactos.isEmpty()){
                    startActivity(Intent(this@ContactosActivity, ContactosActivity::class.java))
                }


            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ContactosActivity, "Error al intentar obtener los datos de los amigos", Toast.LENGTH_SHORT).show()
                }
            }
        }




    }

    private fun actualizarListaContactos() {
        obtenerContactos("")
    }

    private fun addUser(user: String) {
        val email = auth.currentUser?.email.toString()

        // Corrutina para obtener los datos de Firestore
        lifecycleScope.launch(Dispatchers.IO) {

            try {
                // Obtener datos de Firestore
                val result = db.collection("users").document(email).get().await()
                val listaContactos = (result.get("connections") as MutableList<String>?)?.toMutableList() ?: mutableListOf()
                listaContactos.add(user)
                // Actualizar Firestore
                db.collection("users").document(email).update("connections", listaContactos).await()
                // Volver al contexto principal para llamar a la función obtenerContactos
                withContext(Dispatchers.Main) {
                    adapter.lista = mutableListOf()
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ContactosActivity, "Error al intentar obtener los datos de los amigos", Toast.LENGTH_SHORT).show()
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
        if(!add){
            actualizarListaContactos()
        }

    }

    // Para que no se pueda volver a la pantalla anterior
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if(!add){
            val intent = Intent(this, AppActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            super.onBackPressed()
        }

    }


}