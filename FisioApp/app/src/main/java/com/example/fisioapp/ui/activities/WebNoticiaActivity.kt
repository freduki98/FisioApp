package com.example.fisioapp.ui.activities

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ActivityWebNoticiaBinding

class WebNoticiaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebNoticiaBinding
    private lateinit var url : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding= ActivityWebNoticiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recogerUrl()
        inicializarWebView()
        setListeners()
    }

    private fun recogerUrl() {
        val datos = intent.extras
        if(datos != null){
            url = datos.getString("URL").toString()
        }
    }

    private fun setListeners() {
        binding.fabVolver.setOnClickListener {
            finish()
        }
    }

    private fun inicializarWebView() {

        // Iniciamos el webView y le pasamos la url
        binding.webView.webViewClient = object: WebViewClient(){}

        binding.webView.webChromeClient = object: WebChromeClient(){}

        //Activamos javascript
        binding.webView.settings.javaScriptEnabled = true

        // Cargamos la url
        binding.webView.loadUrl(url)
    }

}