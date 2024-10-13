package com.example.fisioapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fisioapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private var edad = 0
    private var altura = 40
    private var estado = "Malo"
    private var provincia = ""
    private var idioma = ""

    private lateinit var adapterProvincias: ArrayAdapter<CharSequence>
    private lateinit var adapterIdiomas: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pintarEtiquetas("Edad")
        pintarEtiquetas("Altura")
        pintarEtiquetas("Estado")

        adapterIdiomas = ArrayAdapter.createFromResource(
            this,
            R.array.array_idiomas,
            android.R.layout.simple_spinner_item)

        adapterProvincias = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            provincias
        )

        binding.spIdiomas.adapter = adapterIdiomas
        binding.spProvincia.adapter = adapterProvincias

        setListeners()

    }

    private fun setListeners() {
        binding.sbEdad.setOnSeekBarChangeListener(this)
        binding.sbAltura.setOnSeekBarChangeListener(this)
        binding.sbEstado.setOnSeekBarChangeListener(this)

        binding.spIdiomas.onItemSelectedListener = this
        binding.spProvincia.onItemSelectedListener = this

        binding.btnReset.setOnClickListener{
            limpiar()
        }
        binding.btnEnviar.setOnClickListener{
            if(datosCorrectos()){
                enviarDatos()
            }
        }
    }

    private fun datosCorrectos(): Boolean {
        //controlaremos que hayamos elegido un idioma y una provincia
        if(binding.spIdiomas.selectedItemId.toInt()==0){
            Toast.makeText(this, "Selecciona un idioma", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.spProvincia.selectedItemId.toInt()==0){
            Toast.makeText(this, "Selecciona un provincia", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun enviarDatos() {
        val i = Intent(this, EditActivity::class.java)
        val bundle=Bundle().apply {
            putString(ESTADO, estado)
            putString(PROVINCIA, provincia)
            putString(IDIOMA, idioma)
            putInt(EDAD, edad)
            putInt(ALTURA, altura)
        }
        i.putExtras(bundle)
        startActivity(i)
    }

    private fun limpiar() {
        edad = 0
        pintarEtiquetas("Edad")
        binding.sbEdad.progress = 0

        altura = 40
        pintarEtiquetas("Altura")
        binding.sbAltura.progress = 40

        estado = "Malo"
        pintarEtiquetas("Estado")
        binding.sbEstado.progress = 1

        binding.spProvincia.setSelection(0)
        binding.spIdiomas.setSelection(0)
    }


    private fun pintarEtiquetas(campo: String) {
        when(campo) {
            "Edad" -> {
                binding.tvEdad.text = String.format(getString(R.string.tv_edad), edad)
            }
            "Altura" -> {
                binding.tvAltura.text = String.format(getString(R.string.tv_altura), altura)
            }
            "Estado" -> {
                binding.tvEstado.text = String.format(getString(R.string.tv_estado), estado)
            }

        }
    }

    // -- Listeners para los seekbars -- //
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        when(p0?.id){
            R.id.sb_edad -> {
                edad = p1
                pintarEtiquetas("Edad")
            }
            R.id.sb_altura -> {
                altura = p1
                pintarEtiquetas("Altura")
            }
            R.id.sb_estado -> {
                when(p1){
                    1 -> {
                        pintarEtiquetas("Estado")
                        estado = "Malo"
                    }
                    2 -> {
                        estado = "Regular"
                        pintarEtiquetas("Estado")
                    }
                    3 -> {
                        estado = "Bueno"
                        pintarEtiquetas("Estado")
                    }
                    4 -> {
                        estado = "Excelente"
                        pintarEtiquetas("Estado")
                    }
                    5 -> {
                        estado = "Nuevo"
                        pintarEtiquetas("Estado")
                    }
                }
            }
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }


    // Listeners para los spinners //
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(p0?.id){
            R.id.sp_idiomas -> {
                idioma = p0.getItemAtPosition(p2).toString()
            }
            R.id.sp_provincia -> {
                provincia = p0.getItemAtPosition(p2).toString()
            }

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}