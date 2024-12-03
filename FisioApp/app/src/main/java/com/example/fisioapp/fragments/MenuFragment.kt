package com.example.fisioapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.fisioapp.OnFragmentActionListener
import com.example.fisioapp.R


class MenuFragment : Fragment() {
    private  var listener: OnFragmentActionListener? = null
    private val listaImagenesView= arrayOf(R.id.iv_anuncios, R.id.iv_galeria, R.id.iv_clientes, R.id.iv_perfil)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var imageView: ImageView

        for(i in listaImagenesView.indices){
            imageView=view.findViewById(listaImagenesView[i])

            imageView.setOnClickListener {
                listener?.onClickImagenMenu(i)
            }

        }
    }

    override fun onAttach(context: Context) {
        //se llama cuando se vincule el fragment con el activity
        super.onAttach(context)
        if(context is OnFragmentActionListener) listener=context
    }

    override fun onDetach() {
        // Se llama cuando se desvincula el fragment del activity
        super.onDetach()
        listener=null
    }


}