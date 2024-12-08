package com.example.fisioapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.fisioapp.databinding.ActivityGaleriaBinding
import com.example.fisioapp.fragments.MenuFragment

class GaleriaActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityGaleriaBinding

    private lateinit var fragment : MenuFragment
    private val bundle = Bundle()

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

        Toast.makeText(this, "Activity Galería", Toast.LENGTH_SHORT).show()


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
        recogerBotonPulsado()
        inicializarFragment()
        cargarFragment(fragment)

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
                cuerpoHumano = p0.getItemAtPosition(p2).toString()
                pintarGaleriaCuerpoHumano()
            }
            R.id.sp_galeria_movimientos -> {
                movimiento = p0.getItemAtPosition(p2).toString()
                pintarGaleriaMovimientos()
            }
            R.id.sp_galeria_ejercicios -> {
                ejercicio = p0.getItemAtPosition(p2).toString()
                pintarGaleriaEjercicios()
            }

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    private fun recogerBotonPulsado() {
        val datos = intent.extras
        if(datos != null){
            val botonPulsado = datos.getInt("BOTONPULSADO")
            bundle.putInt("BOTONPULSADO", botonPulsado)
        }
    }

    private fun inicializarFragment() {
        fragment = MenuFragment().apply {
            arguments = bundle
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
            binding.ivCuerpo6.setImageResource(R.drawable.piern1)
            binding.ivCuerpo7.setImageResource(R.drawable.piern2)
            binding.ivCuerpo8.setImageResource(R.drawable.piern3)
            binding.ivCuerpo9.setImageResource(R.drawable.piern4)
            binding.ivCuerpo10.setImageResource(R.drawable.piern5)
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

    private fun cargarFragment(fg: MenuFragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fcv_menu_galeria, fg)
        }
    }

}