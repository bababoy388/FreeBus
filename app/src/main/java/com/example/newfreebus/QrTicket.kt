package com.example.newfreebus

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class QrTicket : AppCompatActivity() {

    private lateinit var minutesText: TextView
    private lateinit var secondsText: TextView

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrcode_ticket)

        // Инициализация всех View
        minutesText = findViewById(R.id.minutes)
        secondsText = findViewById(R.id.seconds)

        // Проверяем наличие активного таймера
        if (!TimerManager.hasActiveTimer(this)) {
            Toast.makeText(this, "Нет активного билета", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Получаем данные из TimerManager
        val timerState = TimerManager.getTimerState(this)
        if (timerState.ticketCode.isEmpty()) {
            Toast.makeText(this, "Ошибка загрузки билета", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Отображаем информацию о билете
        setupTicketInfo(timerState)

        // Запускаем таймер
        startTimer()
    }

    private fun setupTicketInfo(timerState: TimerManager.TimerState) {
        val routeId = timerState.routeId
        val routeNumber = timerState.routeNumber
        val route = TicketFragment.getRouteById(routeId) ?: return

        // Устанавливаем время покупки
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timerState.buyTime

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
    }

    private fun startTimer() {
        val remainingTime = TimerManager.getRemainingTime(this)

        if (remainingTime <= 0) {
            Toast.makeText(this, "Время билета истекло", Toast.LENGTH_SHORT).show()
            TimerManager.clearTimer(this)
            finish()
            return
        }

        // Инициализируем начальные значения
        val initialMinutes = remainingTime / 1000 / 60
        val initialSeconds = (remainingTime / 1000) % 60

        minutesText.text = initialMinutes.toString()
        secondsText.text = String.format("%02d", initialSeconds)

        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60

                minutesText.text = minutes.toString()
                secondsText.text = String.format("%02d", seconds)

                // Меняем цвет при малом времени
                if (minutes < 5) {
                    minutesText.setTextColor(Color.RED)
                    secondsText.setTextColor(Color.RED)
                } else {
                    minutesText.setTextColor(Color.WHITE)
                    secondsText.setTextColor(Color.WHITE)
                }
            }

            override fun onFinish() {
                minutesText.text = "0"
                secondsText.text = "00"
                minutesText.setTextColor(Color.RED)
                secondsText.setTextColor(Color.RED)

                Toast.makeText(this@QrTicket, "Время билета истекло", Toast.LENGTH_SHORT).show()
                TimerManager.clearTimer(this@QrTicket)
                finish()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}