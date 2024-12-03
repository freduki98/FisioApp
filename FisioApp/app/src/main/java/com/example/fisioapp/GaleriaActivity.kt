package com.example.fisioapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.databinding.ActivityGaleriaBinding

class GaleriaActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityGaleriaBinding

    private lateinit var adapterCuerpo: ArrayAdapter<CharSequence>
    private lateinit var adapterMovimientos: ArrayAdapter<CharSequence>
    private lateinit var adapterEjercicios: ArrayAdapter<CharSequence>

    private var  cuerpoHumano = ""
    private var  movimiento = ""
    private var  ejercicio = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGaleriaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        adapterCuerpo = ArrayAdapter.createFromResource(
            this,
            R.array.array_cuerpoHumano,
            android.R.layout.simple_spinner_item)

        adapterMovimientos = ArrayAdapter.createFromResource(
            this,
            R.array.array_movimientos,
            android.R.layout.simple_spinner_item)

        adapterEjercicios = ArrayAdapter.createFromResource(
            this,
            R.array.array_ejercicios,
            android.R.layout.simple_spinner_item)

        binding.spGaleriaCuerpoHumano.adapter = adapterCuerpo
        binding.spGaleriaMovimientos.adapter = adapterMovimientos
        binding.spGaleriaEjercicios.adapter = adapterEjercicios

        setListeners()

    }

    private fun setListeners() {
        binding.spGaleriaCuerpoHumano.onItemSelectedListener = this
        binding.spGaleriaMovimientos.onItemSelectedListener = this
        binding.spGaleriaEjercicios.onItemSelectedListener = this
    }

    // Listeners para los spinners //
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(p0?.id){
            R.id.sp_galeria_cuerpoHumano -> {
                if (p0 != null) {
                    cuerpoHumano = p0.getItemAtPosition(p2).toString()
                    pintarGaleriaCuerpoHumano()
                }
            }
            R.id.sp_galeria_movimientos -> {
                if (p0 != null) {
                    movimiento = p0.getItemAtPosition(p2).toString()
                    pintarGaleriaMovimientos()
                }
            }
            R.id.sp_galeria_ejercicios -> {
                if (p0 != null) {
                    ejercicio = p0.getItemAtPosition(p2).toString()
                    pintarGaleriaEjercicios()
                }
            }

        }
    }

    private fun pintarGaleriaEjercicios() {
        if(ejercicio == "CARDIOVASCULAR"){
            binding.ivCuerpo11.setImageResource(R.drawable.card1)
            binding.ivCuerpo12.setImageResource(R.drawable.card2)
            binding.ivCuerpo13.setImageResource(R.drawable.card3)
            binding.ivCuerpo14.setImageResource(R.drawable.card4)
            binding.ivCuerpo15.setImageResource(R.drawable.card5)
        } else if(ejercicio == "FUERZA MAXIMA"){
            binding.ivCuerpo11.setImageResource(R.drawable.fuerz1)
            binding.ivCuerpo12.setImageResource(R.drawable.fuerz2)
            binding.ivCuerpo13.setImageResource(R.drawable.fuerz3)
            binding.ivCuerpo14.setImageResource(R.drawable.fuerz4)
            binding.ivCuerpo15.setImageResource(R.drawable.fuerz5)
        } else if(ejercicio == "HIPERTROFIA"){
            binding.ivCuerpo11.setImageResource(R.drawable.hiper1)
            binding.ivCuerpo12.setImageResource(R.drawable.hiper2)
            binding.ivCuerpo13.setImageResource(R.drawable.hiper3)
            binding.ivCuerpo14.setImageResource(R.drawable.hiper4)
            binding.ivCuerpo15.setImageResource(R.drawable.hiper5)
        }
    }

    private fun pintarGaleriaMovimientos() {
        if(movimiento == "COLUMNA VERTEBRAL"){
            binding.ivCuerpo6.setImageResource(R.drawable.col1)
            binding.ivCuerpo7.setImageResource(R.drawable.col2)
            binding.ivCuerpo8.setImageResource(R.drawable.col3)
            binding.ivCuerpo9.setImageResource(R.drawable.col4)
            binding.ivCuerpo10.setImageResource(R.drawable.col5)
        } else if(movimiento == "MIEMBROS SUPERIORES"){
            binding.ivCuerpo6.setImageResource(R.drawable.mov1)
            binding.ivCuerpo7.setImageResource(R.drawable.mov2)
            binding.ivCuerpo8.setImageResource(R.drawable.mov3)
            binding.ivCuerpo9.setImageResource(R.drawable.mov4)
            binding.ivCuerpo10.setImageResource(R.drawable.mov5)
        } else if(movimiento == "MIEMBROS INFERIORES"){
            binding.ivCuerpo6.setImageResource(R.drawable.cad1)
            binding.ivCuerpo7.setImageResource(R.drawable.cad2)
            binding.ivCuerpo8.setImageResource(R.drawable.cad3)
            binding.ivCuerpo9.setImageResource(R.drawable.cad4)
            binding.ivCuerpo10.setImageResource(R.drawable.cad5)
        }
    }

    private fun pintarGaleriaCuerpoHumano() {
        if(cuerpoHumano == "SIST. ESQUELÉTICO"){
            binding.ivCuerpo1.setImageResource(R.drawable.esq1)
            binding.ivCuerpo2.setImageResource(R.drawable.esq2)
            binding.ivCuerpo3.setImageResource(R.drawable.esq3)
            binding.ivCuerpo4.setImageResource(R.drawable.esq4)
            binding.ivCuerpo5.setImageResource(R.drawable.esq5)
        } else if(cuerpoHumano == "SIST. MUSCULAR"){
            binding.ivCuerpo1.setImageResource(R.drawable.musc1)
            binding.ivCuerpo2.setImageResource(R.drawable.musc2)
            binding.ivCuerpo3.setImageResource(R.drawable.musc3)
            binding.ivCuerpo4.setImageResource(R.drawable.musc4)
            binding.ivCuerpo5.setImageResource(R.drawable.musc5)
        } else if(cuerpoHumano == "SIST. NERVIOSO"){
            binding.ivCuerpo1.setImageResource(R.drawable.nerv1)
            binding.ivCuerpo2.setImageResource(R.drawable.nerv2)
            binding.ivCuerpo3.setImageResource(R.drawable.nerv3)
            binding.ivCuerpo4.setImageResource(R.drawable.nerv4)
            binding.ivCuerpo5.setImageResource(R.drawable.nerv5)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}