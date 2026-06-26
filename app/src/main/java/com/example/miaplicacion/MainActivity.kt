package com.example.miaplicacion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent

class MainActivity : AppCompatActivity(), MessageClient.OnMessageReceivedListener {
    private lateinit var labelChat: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cajaTexto = findViewById<EditText>(R.id.etInput)
        val boton = findViewById<Button>(R.id.btnAccion)
        labelChat = findViewById<TextView>(R.id.tvLabel)


        boton.setOnClickListener {
            val textoIngresado = cajaTexto.text.toString()
            if (textoIngresado.isNotEmpty()) {
                labelChat.append("\nCelular: $textoIngresado") // Agrega al chat local
                enviarTextoAlReloj(textoIngresado) // Llama a la función de envío
                cajaTexto.text.clear() // Limpia la caja para escribir otro mensaje
            }
        }
    } // Cierre del método onCreate
    override fun onResume() {
        super.onResume()
        Wearable.getMessageClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()
        Wearable.getMessageClient(this).removeListener(this)
    }


    private fun enviarTextoAlReloj(texto: String) {
        val payload = texto.toByteArray(Charsets.UTF_8)
        Wearable.getNodeClient(this).connectedNodes.addOnSuccessListener { nodos ->
            for (nodo in nodos) {
                Wearable.getMessageClient(this).sendMessage(nodo.id, "/chat_celular", payload)
            }
        }
    }
    override fun onMessageReceived(event: MessageEvent) {

        if (event.path == "/chat_reloj") {
            val mensajeReloj = String(event.data, Charsets.UTF_8)
            runOnUiThread {
                labelChat.append("\nReloj ⌚: $mensajeReloj")
            }
        }
    }
}