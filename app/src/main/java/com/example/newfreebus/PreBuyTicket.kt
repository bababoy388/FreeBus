package com.example.newfreebus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PreBuyTicket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buy_ticket)

        val nextButton: Button = findViewById(R.id.nextButton)

        // Обработка нажатия кнопки
        nextButton.setOnClickListener {
            // Создаем Intent для перехода на следующую активность
            val intent = Intent(this, BuyTicket::class.java)
            startActivity(intent)
        }
    }
}