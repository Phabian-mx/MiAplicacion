package com.example.miaplicacion.reloj.presentation

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView
import android.content.Context
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.miaplicacion.R
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import android.widget.Toast

class Prueba : ComponentActivity(), SensorEventListener, MessageClient.OnMessageReceivedListener {
    private lateinit var sensorManager: SensorManager
    private var sensorGiroscopio: Sensor? = null
    private var sensorPresion: Sensor? = null
    private var sensorAcelerometro: Sensor? = null

    private lateinit var tvLuz: TextView
    private lateinit var tvPresion: TextView
    private lateinit var tvAcelerometro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

       setContentView(com.example.miaplicacion.R.layout.prueba)


        tvLuz = findViewById(com.example.miaplicacion.R.id.tvLuz)
        tvPresion = findViewById(com.example.miaplicacion.R.id.tvPresion)
        tvAcelerometro = findViewById(com.example.miaplicacion.R.id.tvAcelerometro)


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorGiroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if (sensorGiroscopio == null) tvLuz.text = "Giroscopio: No disponible"

        sensorPresion = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        if (sensorPresion == null) tvPresion.text = "Presión: No disponible en este reloj"

        sensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensorAcelerometro == null) tvAcelerometro.text = "Acel: No disponible en este reloj"


        val btnVolver = findViewById<Button>(com.example.miaplicacion.R.id.btnVolverReloj)
        btnVolver.setOnClickListener {
            finish()

        }
        val btnResponder = findViewById<Button>(com.example.miaplicacion.R.id.btnResponderCelular)
        btnResponder.setOnClickListener {
            // 1. Extraemos los valores actuales que están escritos en los TextViews
            val datosGiro = tvLuz.text.toString()
            val datosPresion = tvPresion.text.toString()
            val datosAcel = tvAcelerometro.text.toString()

           val mensajeCompleto = "Datos del Reloj:\n$datosGiro\n$datosPresion\n$datosAcel"


            responderAlCelular(mensajeCompleto)
        }
    }

    override fun onResume() {
        Wearable.getMessageClient(this).addListener(this)
        super.onResume()

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
                1001
            )
        }

        sensorGiroscopio?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorPresion?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorAcelerometro?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        Wearable.getMessageClient(this).removeListener(this)
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_GYROSCOPE -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                tvLuz.text = String.format("Giro:\nX: %.2f | Y: %.2f | Z: %.2f", x, y, z)
            }

            Sensor.TYPE_PRESSURE -> {
                val valorPresion = event.values[0]
                tvPresion.text = "Presión: $valorPresion hPa"
            }

            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                tvAcelerometro.text = String.format("Acel:\nX: %.2f\nY: %.2f\nZ: %.2f", x, y, z)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun responderAlCelular(mensaje: String) {
        val payload = mensaje.toByteArray(Charsets.UTF_8)
        Wearable.getNodeClient(this).connectedNodes.addOnSuccessListener { nodos ->
            for (nodo in nodos) {
                Wearable.getMessageClient(this).sendMessage(nodo.id, "/chat_reloj", payload)
            }
        }
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