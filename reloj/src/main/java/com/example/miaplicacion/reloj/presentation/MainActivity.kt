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


        setContentView(com.example.miaplicacion.reloj.R.layout.activity_main)

        vibrator = getSystemService(android.content.Context.VIBRATOR_SERVICE) as Vibrator
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido)
        // 1. Buscamos el botón por el ID exacto de tu reloj ("btnAccionReloj")
        val boton: Button = findViewById(R.id.btnAccionReloj)


        boton.setOnClickListener {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(android.os.VibrationEffect.createOneShot(500, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
            }
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }

            val intent = android.content.Intent(this, Prueba::class.java)
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