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

        // Enlazamos las vistas del XML con Kotlin
        val cajaTexto = findViewById<EditText>(R.id.etInput)
        val boton = findViewById<Button>(R.id.btnAccion)
        val label = findViewById<TextView>(R.id.tvLabel)

        // Acción al presionar el botón
        boton.setOnClickListener {
            // 1. Obtener el texto de la caja
            val textoIngresado = cajaTexto.text.toString()

            // 2. Pasar el texto al label
            label.text = textoIngresado

            // 3. Mostrar la alerta tipo pop-up
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