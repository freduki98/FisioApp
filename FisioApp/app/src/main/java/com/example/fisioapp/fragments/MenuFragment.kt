package com.example.fisioapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.fisioapp.AjustesActivity
import com.example.fisioapp.AppActivity
import com.example.fisioapp.NoticiasActivity
import com.example.fisioapp.ClientesActivity
import com.example.fisioapp.GaleriaActivity
import com.example.fisioapp.R


class MenuFragment : Fragment() {
    private val listaImagenesView= arrayOf(R.id.iv_anuncios, R.id.iv_galeria, R.id.iv_home , R.id.iv_clientes, R.id.iv_perfil)
    private val listaImagenesSeleccionado= arrayOf(R.drawable.noticias_boton_seleccionado, R.drawable.galeria_boton_seleccionado, R.drawable.home_boton_seleccionado ,R.drawable.clientes_boton_seleccionado, R.drawable.ajustes_boton_seleccionado)
    private val listaActivities= arrayOf(
        NoticiasActivity::class.java,
        GaleriaActivity::class.java,
        AppActivity::class.java,
        ClientesActivity::class.java,
        AjustesActivity::class.java)

    private var botonPulsado = 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        botonPulsado = arguments?.getInt("BOTONPULSADO")?: -1

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var imageView: ImageView

        for(i in listaImagenesView.indices){
            imageView=view.findViewById(listaImagenesView[i])

            if(i == botonPulsado){
                imageView.setBackgroundResource(listaImagenesSeleccionado[i])
            }

            imageView.setOnClickListener {

                startActivity(Intent(activity, listaActivities[i]).apply {
                    putExtra("BOTONPULSADO", i)
                })

            }

        }
    }


}