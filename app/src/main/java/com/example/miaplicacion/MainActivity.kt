package com.example.miaplicacion
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // aqui se conecta el diseño de la pantalla con el código
        val cajaTexto = findViewById<EditText>(R.id.etInput)
        val boton = findViewById<Button>(R.id.btnAccion)
        val label = findViewById<TextView>(R.id.tvLabel)

        // Acción al momento de  presionar el botón
        boton.setOnClickListener {
            // Guarda el texto que puso el usuario
            val textoIngresado = cajaTexto.text.toString()

            //  Pasar el texto al label
            label.text = textoIngresado

            //  se Configura la alerta emergente
            val builder = AlertDialog.Builder(this)
            builder.setTitle("¡Éxito!")
            builder.setMessage("El texto se ha transferido correctamente.")
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss() // Cierra la alerta al presionar aceptar
            }

            val alerta = builder.create()
            alerta.show()
        }
    }
}