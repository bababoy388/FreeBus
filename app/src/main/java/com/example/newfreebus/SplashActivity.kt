package com.example.newfreebus

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        enableEdgeToEdge()
        window.statusBarColor = android.graphics.Color.parseColor("#40000000")
        window.navigationBarColor = Color.BLACK

        // Проверяем, сохранён ли токен
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("auth_token", null)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (token != null) {
                // Токен есть – сразу на главную
                Intent(this, MainActivity::class.java)
            } else {
                // Токена нет – на экран входа
                Intent(this, Authorization::class.java)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, 1200)
    }

    override fun onBackPressed() {
        // Запрещаем возврат
    }
}