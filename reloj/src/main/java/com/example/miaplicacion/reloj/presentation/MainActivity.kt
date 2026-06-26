package com.example.miaplicacion.reloj.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.miaplicacion.reloj.R
import android.os.Vibrator
import android.media.MediaPlayer



class MainActivity : ComponentActivity() {
    private lateinit var vibrator: Vibrator
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        vibrator = getSystemService(android.content.Context.VIBRATOR_SERVICE) as Vibrator
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido)

        // Buscamos el botón usando la referencia limpia de R
        val boton: Button = findViewById(R.id.btnAccionReloj)

        boton.setOnClickListener {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(android.os.VibrationEffect.createOneShot(500, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
            }
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }

            // Abrimos la pantalla Prueba de forma limpia y segura para Wear OS
            val intent = android.content.Intent(this, Prueba::class.java).apply {
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }

    override fun onRestart() {
        super.onRestart()
        // Cuando regresas de la Ventana 2 a esta, la música se detiene de inmediato
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        // Volvemos a inicializar el audio para dejarlo listo por si vuelven a presionar el botón
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Si el usuario cierra la aplicación por completo, liberamos la memoria del reproductor
        mediaPlayer.release()
    }
}