package com.example.app_s9


import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEdad = findViewById<EditText>(R.id.etEdad)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnCargar = findViewById<Button>(R.id.btnCargar)

        val prefs = getSharedPreferences("PerfilPrefs", Context.MODE_PRIVATE)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val edad = etEdad.text.toString()
            val email = etEmail.text.toString()

            with(prefs.edit()) {
                putString("nombre", nombre)
                putString("edad", edad)
                putString("email", email)
                apply()
            }
            Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
        }

        btnCargar.setOnClickListener {
            etNombre.setText(prefs.getString("nombre", ""))
            etEdad.setText(prefs.getString("edad", ""))
            etEmail.setText(prefs.getString("email", ""))
            Toast.makeText(this, "Datos cargados", Toast.LENGTH_SHORT).show()
        }
    }
}
