package com.example.newfreebus

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.newfreebus.data.model.LoginRequest
import com.example.newfreebus.data.remote.RetrofitClient
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class Authorization : AppCompatActivity() {

    private lateinit var editTextCode: EditText
    private lateinit var loginButton: MaterialButton
    private lateinit var textView9: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization)

        // Находим элементы по их ID
        editTextCode = findViewById(R.id.editTextCode)
        loginButton = findViewById(R.id.loginButton)
        textView9 = findViewById(R.id.textView9)

        textView9.setOnClickListener {
            // Сохраняем токен в SharedPreferences
            val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("auth_token", "NoKey")
                apply()
            }
            startActivity(Intent(this@Authorization, MainActivity::class.java))
        }

        // Устанавливаем обработчик нажатия на кнопку
        loginButton.setOnClickListener {
            val token = editTextCode.text.toString().trim()
            if (token.isNotEmpty()) {
                performLogin(token)
            } else {
                Toast.makeText(this, "Введите ключ авторизации", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performLogin(token: String) {
        lifecycleScope.launch {
            try {
                // Отправляем POST-запрос с токеном
                val response = RetrofitClient.apiService.login(LoginRequest(token))
                if (response.valid) {
                    // Сохраняем токен в SharedPreferences
                    val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("auth_token", token)
                        apply()
                    }

                    startActivity(Intent(this@Authorization, MainActivity::class.java))
                    finish()
                } else {
                    // Токен не найден
                    Toast.makeText(this@Authorization, "Неверный ключ авторизации", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Ошибка сети или сервера
                Toast.makeText(this@Authorization, "Неизвестная ошибка, попробуйте снова", Toast.LENGTH_SHORT).show()
            }
        }
    }
}