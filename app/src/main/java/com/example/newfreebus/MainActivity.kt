package com.example.newfreebus

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var noTicketLayout: LinearLayout
    private lateinit var ticketContainer: FrameLayout
    private var lastEnteredCode: String = ""
    private var countDownTimer: CountDownTimer? = null // Добавляем переменную для таймера

    // data/RoutesSingletonSimple.kt
    object RoutesSingletonSimple {
        val routes = mapOf(
            "1" to Triple("МП \"Городской транспорт\"", "Тихие Зори - Станция Красноярск-Северный", "1 билет - 46₽"),
            "2" to Triple("ООО \"Экипаж-ГО\"", "Автовокзал \"Восточный\" - Дом Учёных", "1 билет - 48₽"),
            "3" to Triple("ООО \"СТК\"", "Автовокзал \"Восточный\" - Академгородок", "1 билет - 48₽"),
            "5" to Triple("ИП Долгушина Г.И", "ОАО \"Красфарма\" - Спорткомплекс «Радуга»", "1 билет - 48₽"),
            "6" to Triple("ИП Тагачаков Вадим Геннадьевич", "Кардиологический центр - ДК \"Кировский\"", "1 билет - 48₽"),
            "7" to Triple("ООО «Экипаж-ГО»", "ДК \"Кировский\" - Агротерминал", "1 билет - 48₽"),
            "8" to Triple("ООО \"Перевозчик\"", "пос. Водников - Кардиологический центр", "1 билет - 48₽"),
            "9" to Triple("ООО \"СКАД\"", "В. Базаиха - Междугородный автовокзал", "1 билет - 48₽"),
            "10" to Triple("AO \"КПАТП-7\"", "Поселок Энергетиков - ОАО «Красфарма»", "1 билет - 48₽"),
            "11" to Triple("AO \"КПАТП-5\"", "Молодежная (площадка отстоя) - 3-я Дальневосточная улица", "1 билет - 48₽"),
            "12" to Triple("AO \"КПАТП-7\"", "Совхоз \"Удачный\" - Станция Красноярск-Северный", "1 билет - 48₽"),
            "14" to Triple("AO \"КПАТП-5\"", "Железнодорожная больница - Поселок Овинный", "1 билет - 48₽"),
            "18" to Triple("AO \"КПАТП-7\"", "Техникум транспорта и сервиса - Верхние Черёмушки (конечная)", "1 билет - 48₽"),
            "19" to Triple("AO \"КПАТП-7\"", "Стела - Причал", "1 билет - 48₽"),
            "21" to Triple("ООО \"СТК\"", "Спортзал - Парк \"Прищепка\"", "1 билет - 48₽"),
            "22" to Triple("АО г. Красноярска «КПАТП № 7»", "Жилой комплекс \"Ясный\" - ИТК", "1 билет - 48₽"),
            "23" to Triple("ООО \"Ветеран\"", "ЛДК - Микрорайон Солнечный", "1 билет - 48₽"),
            "26" to Triple("AO \"КПАТП-5\"", "Микрорайон Преображенский (площадка отстоя) - Полигон", "1 билет - 48₽"),
            "27" to Triple("ИП Ялтонский А.М.", "Спортзал - Микрорайон Тихие Зори", "1 билет - 48₽"),
            "30" to Triple("AO \"КПАТП-5\"", "Академия Биатлона - ЛДК", "1 билет - 48₽"),
            "31" to Triple("AO \"КПАТП-7\"", "Оздоровительный комплекс \"Гренада\" - Железнодорожный вокзал", "1 билет - 48₽"),
            "37" to Triple("AO \"КПАТП-7\"", "пос. Таймыр (конечная) - Дом Учёных", "1 билет - 48₽"),
            "38" to Triple("АО \"СТК\"", "Техникум транспорта и сервиса - Полигон", "1 билет - 48₽"),
            "40" to Triple("AO \"КПАТП-7\"", "Автовокзал \"Восточный\" - Сельхозкомплекс", "1 билет - 48₽"),
            "43" to Triple("ООО \"Вавулин-К\"", "Автовокзал \"Восточный\" - Сельхозкомплекс", "1 билет - 48₽"),
            "49" to Triple("AO \"КПАТП-5\"", "Микрорайон Солнечный - Стела", "1 билет - 48₽"),
            "50" to Triple("ИП Читашвили Н. С.", "Озеро-парк - Микрорайон Тихие Зори", "1 билет - 48₽"),
            "52" to Triple("AO \"КПАТП-5\"", "Озеро-парк - Микрорайон Тихие Зори", "1 билет - 48₽"),
            "55" to Triple("AO \"КПАТП-7\"", "п. Цементников - Железнодорожный вокзал", "1 билет - 48₽"),
            "56" to Triple("AO \"КПАТП-7\"", "Поликлиника - Спортзал", "1 билет - 48₽"),
            "58" to Triple("ИП Голунцов С. В.", "Микрорайон Солнечный - Автовокзал \"Восточный\"", "1 билет - 48₽"),
            "60" to Triple("ИП Цугленок М.М.", "Микрорайон Солнечный - Шинное кладбище", "1 билет - 48₽"),
            "61" to Triple("ООО «КПАТП»", "мкрн. Солнечный (ул. Светлова) - Академгородок", "1 билет - 48₽"),
            "63" to Triple("ООО \"КПАТП\"", "Микрорайон Солнечный - Микрорайон Тихие Зори", "1 билет - 48₽"),
            "64" to Triple("AO \"КПАТП-5\"", "ДК \"Кировский\" - Агротерминал", "1 билет - 48₽"),
            "65" to Triple("ИП Ялтонский А.М.", "Улица Петрушина - ИТК", "1 билет - 48₽"),
            "69" to Triple("АО \"КПАТП-7\"", "Спортзал - пос. Таймыр (конечная)", "1 билет - 48₽"),
            "71" to Triple("ООО \"Экипаж-ГО\"", "п. Песчанка (конечная) - Железнодорожная больница", "1 билет - 48₽"),
            "77" to Triple("ООО \"Практик\"", "Стела - Контейнерный двор", "1 билет - 48₽"),
            "78" to Triple("ИП Бронников А. И.", "Микрорайон Тихие Зори - пос. Таймыр (конечная)", "1 билет - 48₽"),
            "80" to Triple("ИП Долгушин Д.Г.", "Железнодорожная больница - ОАО \"Русал\"", "1 билет - 48₽"),
            "81" to Triple("ООО \"Сирена\"", "Дом Учёных - Профилакторий КраМЗа", "1 билет - 48₽"),
            "83" to Triple("ИП Цугленок М.М.", "Сельхозкомплекс - Верхние Черёмушки (конечная)", "1 билет - 48₽"),
            "85" to Triple("ООО \"СКАД\"", "Сельхозкомплекс - Верхние Черёмушки", "1 билет - 48₽"),
            "87" to Triple("AO \"КПАТП-5\"", "Спортзал - Академия Биатлона", "1 билет - 48₽"),
            "88" to Triple("ИП Тагачаков В.Г.", "Верхняя Базаиха - Академия Биатлона", "1 билет - 48₽"),
            "90" to Triple("ИП \"Патрин Н.Н.\"", "Верхняя Базаиха - Академия Биатлона", "1 билет - 48₽"),
            "92" to Triple("ИП Патрин Н.Н.", "Химкомбинат \"Енисей\" - ОАО «Красфарма»", "1 билет - 48₽"),
            "94" to Triple("ООО \"Практик\"", "Верхние Черёмушки - ЛДК", "1 билет - 48₽"),
            "95" to Triple("AO \"КПАТП-7\"", "Верхние Черёмушки - ЛДК", "1 билет - 48₽"),
            "98" to Triple("ИП Гнетов Ю. Н.", "Станция Красноярск - Северный - Цимлянская улица", "1 билет - 48₽"),
            "99" to Triple("ООО \"СТК\"", "Станция Красноярск - Северный - Цимлянская улица", "1 билет - 48₽")
        )

        fun getRouteById(id: String): Triple<String, String, String>? {
            return routes[id]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        noTicketLayout = findViewById(R.id.noTicketLayout)
        ticketContainer = findViewById(R.id.ticketContainer)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.my_ticket -> {


                    true
                }
                R.id.history -> {

                    true
                }
                R.id.paying -> {

                    true
                }
                R.id.menu -> {

                    true
                }
                else -> false
            }
        }

        // Выбираем пункт по умолчанию
        bottomNavigationView.selectedItemId = R.id.my_ticket
    }

        val buyButton = findViewById<MaterialButton>(R.id.button)
        buyButton.setOnClickListener {
            showSimpleDialog()
        }


    }

    private fun showSimpleDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.code_input)

        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val editText = dialog.findViewById<EditText>(R.id.editTextCode)
        val submitButton = dialog.findViewById<TextView>(R.id.buttonSubmit)

        submitButton.setOnClickListener {
            val code = editText.text.toString().trim()

            if (code.isNotEmpty()) {
                lastEnteredCode = code
                showTicket(code)

                dialog.dismiss()

            } else {
                Toast.makeText(this, "Введите номер автобуса и маршрута", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun showTicket(code: String) {
        // Останавливаем предыдущий таймер, если он запущен
        countDownTimer?.cancel()

        val parts = code.split(" ")
        if (parts.size < 2) {
            Toast.makeText(this, "Некорректный код. Формат: 'маршрут номер_автобуса'", Toast.LENGTH_SHORT).show()
            return
        }
        val routeId = parts[0]
        val routeNumber = parts[1]

        val route = RoutesSingletonSimple.getRouteById(routeId)

        if (route == null) {
            Toast.makeText(this, "Маршрут $routeId не найден", Toast.LENGTH_SHORT).show()
            return
        }

        val calendar = Calendar.getInstance()
        val currentDate = calendar.time

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(currentDate)

        val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        noTicketLayout.visibility = View.GONE
        ticketContainer.removeAllViews()

        val ticketView = layoutInflater.inflate(R.layout.ticket, null)

        val timeText = ticketView.findViewById<TextView>(R.id.timeText)
        val dateText = ticketView.findViewById<TextView>(R.id.dateText)
        val routeNumberTicket = ticketView.findViewById<TextView>(R.id.routeNumber)
        val busNumberTicket = ticketView.findViewById<TextView>(R.id.busNumber)
        val routeText = ticketView.findViewById<TextView>(R.id.routeText)
        val minutesText = ticketView.findViewById<TextView>(R.id.minutesText)
        val secondsText = ticketView.findViewById<TextView>(R.id.secondsText)
        val companyText = ticketView.findViewById<TextView>(R.id.companyText)
        val priceText = ticketView.findViewById<TextView>(R.id.priceText)

        timeText.text = formattedTime
        dateText.text = formattedDate


        routeText.text = route.second    // Маршрут

        routeNumberTicket.text = "№$routeId"
        busNumberTicket.text = routeNumber
        companyText.text = route.first
        priceText.text = route.third

        // Устанавливаем начальное значение таймера
        minutesText.text = "45"
        secondsText.text = "00"

        // Создаем и запускаем таймер на 45 минут (45 * 60 * 1000 миллисекунд)
        countDownTimer = object : CountDownTimer(45 * 60 * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60

                minutesText.text = minutes.toString()
                secondsText.text = String.format("%02d", seconds) // Форматируем секунды с ведущим нулем

                // Меняем цвет на красный при остатке менее 5 минут
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

                // Можно добавить звуковое оповещение или вибрацию
                Toast.makeText(this@MainActivity, "Время действия билета истекло!", Toast.LENGTH_SHORT).show()
            }
        }.start()

        ticketContainer.addView(ticketView)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Останавливаем таймер при уничтожении активности
        countDownTimer?.cancel()
    }
}