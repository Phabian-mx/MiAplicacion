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

class Prueba : ComponentActivity(), SensorEventListener {
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


        setContentView(com.example.miaplicacion.reloj.R.layout.prueba)
// Enlazar los TextViews que pusiste en el XML
        tvLuz = findViewById(com.example.miaplicacion.reloj.R.id.tvLuz)
        tvPresion = findViewById(com.example.miaplicacion.reloj.R.id.tvPresion)
        tvAcelerometro = findViewById(com.example.miaplicacion.reloj.R.id.tvAcelerometro)

        // 2. MODIFICACIÓN AQUÍ: Inicializar el sistema de sensores de forma segura
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorGiroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if (sensorGiroscopio == null) tvLuz.text = "Giroscopio: No disponible"

        sensorPresion = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        if (sensorPresion == null) tvPresion.text = "Presión: No disponible en este reloj"

        sensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensorAcelerometro == null) tvAcelerometro.text = "Acel: No disponible en este reloj"

        val btnVolver = findViewById<Button>(com.example.miaplicacion.reloj.R.id.btnVolverReloj)
        btnVolver.setOnClickListener {
            finish()

        }
    }

    override fun onResume() {
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
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // Recibir los datos de los sensores en tiempo real
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

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    // Dejamos el método de permisos limpio para evitar conflictos con el sistema de Samsung
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}