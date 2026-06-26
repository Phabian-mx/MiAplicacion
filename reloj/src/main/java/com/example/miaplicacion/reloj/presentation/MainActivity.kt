package com.example.miaplicacion.reloj.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.miaplicacion.reloj.R

class MainActivity : ComponentActivity() { // Línea 1 de la diapositiva
    override fun onCreate(savedInstanceState: Bundle?) { // Línea 2 de la diapositiva
        super.onCreate(savedInstanceState) // Línea 5 de la diapositiva

        // Enlaza el diseño XML clásico del reloj que ya editamos
        setContentView(com.example.miaplicacion.reloj.R.layout.activity_main) // Línea 6 de la diapositiva


        // 1. Buscamos el botón por el ID exacto de tu reloj ("btnAccionReloj")
        val boton: Button = findViewById(R.id.btnAccionReloj)

        // 2. Programamos la acción del clic (Diapositiva 11)
        boton.setOnClickListener {
            // Creamos la ruta para viajar desde esta ventana hacia "Prueba"
            val intent = android.content.Intent(this, Prueba::class.java)
            startActivity(intent)
        }
    }
}