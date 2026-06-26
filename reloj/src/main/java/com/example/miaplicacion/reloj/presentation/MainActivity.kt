package com.example.miaplicacion.reloj.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.miaplicacion.R
import android.os.Vibrator
import android.media.MediaPlayer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable



class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener {
    private lateinit var vibrator: Vibrator
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        vibrator = getSystemService(android.content.Context.VIBRATOR_SERVICE) as Vibrator
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido)


        val boton: Button = findViewById(R.id.btnAccionReloj)

        boton.setOnClickListener {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(android.os.VibrationEffect.createOneShot(500, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
            }
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }


            val intent = android.content.Intent(this, Prueba::class.java).apply {
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        Wearable.getMessageClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()
        Wearable.getMessageClient(this).removeListener(this)
    }

    override fun onRestart() {
        super.onRestart()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
       mediaPlayer = MediaPlayer.create(this, R.raw.sonido)
    }

    override fun onDestroy() {
        super.onDestroy()
       mediaPlayer.release()
    }
    override fun onMessageReceived(event: MessageEvent) {
        if (event.path == "/chat_celular") {
            val mensajeCelular = String(event.data, Charsets.UTF_8)
            runOnUiThread {
                Toast.makeText(this, "Cel dice: $mensajeCelular", Toast.LENGTH_SHORT).show()
            }
        }
    }
}