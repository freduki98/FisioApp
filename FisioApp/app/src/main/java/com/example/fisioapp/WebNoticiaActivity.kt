package com.example.fisioapp

import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        binding.swipe.setOnRefreshListener {
            binding.webView.reload()
        }
    }

    private fun inicializarWebView() {
        binding.webView.webViewClient = object: WebViewClient(){

            //Control del swipeRefreshLayout
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.swipe.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.swipe.isRefreshing = false
            }

        }

        binding.webView.webChromeClient = object: WebChromeClient(){

        }

        //Activamos javascript
        binding.webView.settings.javaScriptEnabled = true

        binding.webView.loadUrl(url)
    }

}