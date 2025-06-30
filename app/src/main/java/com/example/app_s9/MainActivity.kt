package com.example.app_s9

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var editTextUsername: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button
    private lateinit var buttonClear: Button
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Inicializar SharedPreferencesHelper
        sharedPreferencesHelper = SharedPreferencesHelper(this)
        
        // Inicializar vistas
        initViews()
        
        // Configurar listeners
        setupListeners()
        
        // Verificar si es la primera vez que se abre la app
        checkFirstTime()
        val tvVisitCount = findViewById<TextView>(R.id.tvVisitCount)
        val btnReset = findViewById<Button>(R.id.btnResetCounter)

        // Obtener instancia de SharedPreferences
        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        var visitCount = sharedPref.getInt("visit_count", 0)

        // Incrementar y guardar el nuevo valor
        visitCount++
        sharedPref.edit().putInt("visit_count", visitCount).apply()

        // Mostrar en pantalla
        tvVisitCount.text = "Visitas: $visitCount"

        // Reiniciar al presionar el botón
        btnReset.setOnClickListener {
            visitCount = 0
            sharedPref.edit().putInt("visit_count", 0).apply()
            tvVisitCount.text = "Visitas: 0"
        }
        val btnIrPerfil = findViewById<Button>(R.id.btnIrPerfil)
        btnIrPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        val switchModo = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switchModoOscuro)

        val isDarkMode = sharedPref.getBoolean("modo_oscuro", false)

// Aplica el modo almacenado
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

// Mostrar el estado en el switch
        switchModo.isChecked = isDarkMode

// Guardar cuando se cambia
        switchModo.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("modo_oscuro", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
            recreate() // Reinicia la actividad para aplicar los cambios de tema
        }
    }
    
    private fun initViews() {
        editTextUsername = findViewById(R.id.editTextUsername)
        buttonSave = findViewById(R.id.buttonSave)
        buttonLoad = findViewById(R.id.buttonLoad)
        buttonClear = findViewById(R.id.buttonClear)
        textViewResult = findViewById(R.id.textViewResult)
    }
    
    private fun setupListeners() {
        buttonSave.setOnClickListener {
            saveData()
        }
        
        buttonLoad.setOnClickListener {
            loadData()
        }
        
        buttonClear.setOnClickListener {
            clearAllData()
        }
    }
    
    private fun saveData() {
        val username = editTextUsername.text.toString().trim()
        
        if (username.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un nombre", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Guardar datos
        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_USERNAME, username)
        sharedPreferencesHelper.saveBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, false)
        sharedPreferencesHelper.saveInt(SharedPreferencesHelper.KEY_USER_ID, (1000..9999).random())
        
        Toast.makeText(this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show()
        editTextUsername.setText("")
    }
    
    private fun loadData() {
        val username = sharedPreferencesHelper.getString(SharedPreferencesHelper.KEY_USERNAME, "Sin nombre")
        val isFirstTime = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, true)
        val userId = sharedPreferencesHelper.getInt(SharedPreferencesHelper.KEY_USER_ID, 0)
        
        val result = "Usuario: $username\nID: $userId\nPrimera vez: ${if (isFirstTime) "Sí" else "No"}"
        textViewResult.text = result
    }
    
    private fun clearAllData() {
        sharedPreferencesHelper.clearAll()
        textViewResult.text = ""
        editTextUsername.setText("")
        Toast.makeText(this, "Todas las preferencias han sido eliminadas", Toast.LENGTH_SHORT).show()
    }
    
    private fun checkFirstTime() {
        val isFirstTime = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, true)
        
        if (isFirstTime) {
            Toast.makeText(this, "¡Bienvenido por primera vez!", Toast.LENGTH_LONG).show()
        }
    }
}