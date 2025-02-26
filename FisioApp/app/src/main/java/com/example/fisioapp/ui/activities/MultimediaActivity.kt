package com.example.fisioapp.ui.activities

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.net.Uri
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.fisioapp.R
import com.example.fisioapp.databinding.ActivityMultimediaBinding

class MultimediaActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMultimediaBinding

    // video
    private lateinit var mediaController: MediaController
    private var prevVideo = R.raw.video2
    private var nextVideo = R.raw.video2

    // Musica
    private lateinit var mediaPlayer: MediaPlayer

    // Sensores
    private lateinit var sensorManager: SensorManager
    private var acelerometro: Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMultimediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarControladores()
        inicialiarSensores()
        mostrarExplicacion()
        setListeners()

    }

    private fun inicializarControladores() {
        mediaPlayer = MediaPlayer.create(this, R.raw.audio)
        mediaController = MediaController(this)
    }

    private fun inicialiarSensores() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun mostrarExplicacion() {
        AlertDialog.Builder(this)
            .setTitle("Tutorial de navegación")
            .setMessage("Para cambiar de vídeo debe girar la pantalla")
            .setNegativeButton("Cancelar") { dialog, _ ->
                finish()
                dialog.dismiss()
            }
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun setListeners() {
        binding.btnPlayMusica.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                binding.btnPlayMusica.text = getString(R.string.musica_off)
            } else {
                mediaPlayer.start()
                binding.btnPlayMusica.text = getString(R.string.musica_on)
            }
        }

        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }

        binding.btnSalir.setOnClickListener { finish() }


    }

    private fun verVideo(nextMove: Int) {

        if (nextMove == 1 && prevVideo == R.raw.video2) {
            nextVideo = R.raw.video3
        } else if (nextMove == -1 && prevVideo == R.raw.video3) {
            nextVideo = R.raw.video2
        } else if (nextMove == -1 && prevVideo == R.raw.video2) {
            nextVideo = R.raw.video1
        } else if (nextMove == 1 && prevVideo == R.raw.video1) {
            nextVideo = R.raw.video2
        }

        if (nextMove == 0) {
            nextVideo = R.raw.video2
        }

        // Actualizamos el video que se va a reproducir
        prevVideo = nextVideo

        // Ruta del video
        reproducirVideo("android.resource://$packageName/$nextVideo")
    }

    private fun reproducirVideo(rutaVideo: String) {
        // Cargamos el video
        val uri = Uri.parse(rutaVideo)
        try {
            binding.videoView.setVideoURI(uri)
            binding.videoView.requestFocus()
            binding.videoView.start()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al reproducir el video", Toast.LENGTH_SHORT).show()
        }

        // Añadimos los controles de reproducción al video
        binding.videoView.setMediaController(mediaController)
        mediaController.setAnchorView(binding.videoView)
    }

    // ---------------------------------------------------------------------------------------- //

    //-------------------------------------- SENSORES ----------------------------------------- //

    override fun onPause() {
        super.onPause()
        // Dejamos de escuchar los sensores
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        // Volvemos a escuchar los sensores
        ponerListenerSensores(acelerometro)

    }
    //----------------------------------------------------------------------------------------------

    private fun ponerListenerSensores(sensor: Sensor?) {
        // Comprobamos que el sensor no sea nulo
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }


    //----------------------------------------------------------------------------------------------

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    if (event.values[0] > 10) {
                        verVideo(-1)
                    } else if (event.values[0] < -10) {
                        verVideo(+1)
                    }
                }
            }
        }


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    //----------------------------------------------------------------------------------------------


}