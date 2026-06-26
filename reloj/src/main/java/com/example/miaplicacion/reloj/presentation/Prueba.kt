package com.example.miaplicacion.reloj.presentation

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class Prueba : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(com.example.miaplicacion.reloj.R.layout.prueba)


        val btnVolver = findViewById<Button>(com.example.miaplicacion.reloj.R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }
    }
}