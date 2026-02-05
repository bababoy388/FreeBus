package com.example.newfreebus
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class IncorrectCodeActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.incorrect_code)

        val button_back: Button = findViewById(R.id.button_back)
        button_back.setOnClickListener {
            finish()
        }
    }
}