package com.example.fisioapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.databinding.ActivityCrearPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class CrearPostActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCrearPostBinding
    private lateinit var title : String
    private lateinit var description : String
    private lateinit var image : String
    private lateinit var link : String

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCrearPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        setListeners()
    }

    private fun datosCorrectos(): Boolean {
        title = binding.etPostTitle.text.toString().trim()
        if(title.isEmpty()){
            binding.etPostTitle.error = "El titulo no puede estar vacio"
            return false
        }
        description = binding.etPostDescription.text.toString().trim()
        if(description.isEmpty()){
            binding.etPostDescription.error = "La descripcion no puede estar vacia"
            return false
        }

        return true
    }

    private fun setListeners() {
        binding.btnGuardarPost.setOnClickListener {
            if(datosCorrectos()){
                title = binding.etPostTitle.text.toString().trim()
                description = binding.etPostDescription.text.toString().trim()
                image = binding.etUrlPost.text.toString().trim()
                link = binding.etLinkPost.text.toString().trim()
                email = auth.currentUser?.email.toString()

                val postId = UUID.randomUUID().toString()
                val postDocument = mapOf(
                    "user" to email,
                    "title" to title,
                    "image" to image,
                    "link" to link,
                    "description" to description,
                    "Date-created" to FieldValue.serverTimestamp()
                )

                db.collection("posts").document(postId).set(postDocument)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Post creado con éxito", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al guardar el post: ${e.message}", Toast.LENGTH_SHORT).show()
                    }


            }
        }


    }
}