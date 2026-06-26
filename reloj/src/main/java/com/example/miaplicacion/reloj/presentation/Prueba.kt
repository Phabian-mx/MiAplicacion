package com.example.miaplicacion.reloj.presentation

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class Prueba : ComponentActivity() { // Hereda de ComponentActivity (Línea 7)
    override fun onCreate(savedInstanceState: Bundle?) { // Línea 9
        super.onCreate(savedInstanceState) // Línea 10

        // Vincula el diseño XML limpio que acabamos de corregir
        setContentView(com.example.miaplicacion.reloj.R.layout.prueba) // Línea 12

        // Agregamos la lógica para controlar el botón "Volver" de la ventana 2
        val btnVolver = findViewById<Button>(com.example.miaplicacion.reloj.R.id.btnVolver)
        btnVolver.setOnClickListener {
            // Cierra esta ventana y regresa automáticamente a la MainActivity principal
            finish()
        }
    }
}