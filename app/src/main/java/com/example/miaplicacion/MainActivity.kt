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
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity(), MessageClient.OnMessageReceivedListener {
    private lateinit var labelChat: TextView
      private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cajaTexto = findViewById<EditText>(R.id.etInput)
        val boton = findViewById<Button>(R.id.btnAccion)
        labelChat = findViewById<TextView>(R.id.tvLabel)

// Referenciar los nuevos botones
        val btnGet = findViewById<Button>(R.id.btnGet)
        val btnPost = findViewById<Button>(R.id.btnPost)

        // Acción al tocar el botón GET
        btnGet.setOnClickListener {
            hacerPeticionGet()
        }

        // Acción al tocar el botón POST
        btnPost.setOnClickListener {
            hacerPeticionPost()
        }

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
    // --- FUNCIÓN GET ---
    private fun hacerPeticionGet() {
        val request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/posts/1")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Error $response")
                    val respuestaServidor = response.body?.string()
                    runOnUiThread {
                        labelChat.append("\n\n-- GET OK --\n$respuestaServidor")
                    }
                }
            }
        })
    }

    // --- FUNCIÓN POST ---
    private fun hacerPeticionPost() {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val jsonBody = """{"sensor":"giroscopio", "x":"-0.05", "y":"0.30"}"""
        val body = jsonBody.toRequestBody(JSON)

        val request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/posts")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Error $response")
                    val respuestaServidor = response.body?.string()
                    runOnUiThread {
                        labelChat.append("\n\n-- POST OK --\n$respuestaServidor")
                    }
                }
            }
        })
    }
}