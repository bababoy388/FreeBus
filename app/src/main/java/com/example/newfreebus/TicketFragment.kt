package com.example.newfreebus

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TicketFragment : Fragment() {

    private lateinit var noTicketLayout: LinearLayout
    private lateinit var ticketContainer: FrameLayout
    private lateinit var buyButton: MaterialButton
    private var countDownTimer: CountDownTimer? = null

    // SharedPreferences для сохранения состояния
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "TicketPrefs"
        private const val KEY_TICKET_CODE = "ticket_code"
        private const val KEY_TICKET_TIME = "ticket_time" // Время покупки

        // Маршруты - объединяем в один companion object
        val routes = mapOf(
            "1" to Triple("МП \"Городской транспорт\"", "Тихие Зори → Ст.Красноярск-Сев", "1 билет - 46₽"),
            "2" to Triple("ООО \"Экипаж-ГО\"", "Автовокз.Вост → Дом Учёных", "1 билет - 48₽"),
            "3" to Triple("ООО \"СТК\"", "Автовокз.Вост → Академгородок", "1 билет - 48₽"),
            "5" to Triple("ИП Долгушина Г.И", "ОАО Красфарма → Спорткомпл.Радуга", "1 билет - 48₽"),
            "6" to Triple("ИП Тагачаков Вадим Геннадьевич", "Кардиоцентр → ДК Кировский", "1 билет - 48₽"),
            "7" to Triple("ООО «Экипаж-ГО»", "ДК Кировский → Агротерминал", "1 билет - 48₽"),
            "8" to Triple("ООО \"Перевозчик\"", "пос. Водников → Кардиоцентр", "1 билет - 48₽"),
            "9" to Triple("ООО \"СКАД\"", "В. Базаиха → Межд.автовокзал", "1 билет - 48₽"),
            "10" to Triple("AO \"КПАТП-7\"", "пос.Энергетиков → Красфарма", "1 билет - 48₽"),
            "11" to Triple("AO \"КПАТП-5\"", "Молодежная → 3-я Дальневост.ул", "1 билет - 48₽"),
            "12" to Triple("AO \"КПАТП-7\"", "Совхоз Удачный → Ст.Кр-ск-Север", "1 билет - 48₽"),
            "14" to Triple("AO \"КПАТП-5\"", "Ж/д больница → пос. Овинный", "1 билет - 48₽"),
            "18" to Triple("AO \"КПАТП-7\"", "Техникум → Верх.Черёмушки", "1 билет - 48₽"),
            "19" to Triple("AO \"КПАТП-7\"", "Стела → Причал", "1 билет - 48₽"),
            "21" to Triple("ООО \"СТК\"", "Спортзал → Парк Прищепка", "1 билет - 48₽"),
            "22" to Triple("АО г. Красноярска «КПАТП № 7»", "ЖК Ясный → ИТК", "1 билет - 48₽"),
            "23" to Triple("ООО \"Ветеран\"", "ЛДК → мкр-н Солнечный", "1 билет - 48₽"),
            "26" to Triple("AO \"КПАТП-5\"", "мкр-н Преображенский → Полигон", "1 билет - 48₽"),
            "27" to Triple("ИП Ялтонский А.М.", "Спортзал → мкр-н Тихие Зори", "1 билет - 48₽"),
            "30" to Triple("AO \"КПАТП-5\"", "Акад.Биатлона → ЛДК", "1 билет - 48₽"),
            "31" to Triple("AO \"КПАТП-7\"", "ОК Гренада → Ж/д вокзал", "1 билет - 48₽"),
            "37" to Triple("AO \"КПАТП-7\"", "пос. Таймыр → Дом Учёных", "1 билет - 48₽"),
            "38" to Triple("АО \"СТК\"", "Техникум → Полигон", "1 билет - 48₽"),
            "40" to Triple("AO \"КПАТП-7\"", "Автовокз.Вост → Сельхозкомпл", "1 билет - 48₽"),
            "43" to Triple("ООО \"Вавулин-К\"", "Автовокз.Вост → Сельхозкомпл", "1 билет - 48₽"),
            "49" to Triple("AO \"КПАТП-5\"", "мкр-н Солнечный → Стела", "1 билет - 48₽"),
            "50" to Triple("ИП Читашвили Н. С.", "Озеро-парк → мкр-н Тих.Зори", "1 билет - 48₽"),
            "52" to Triple("AO \"КПАТП-5\"", "Озеро-парк → мкр-н Тих.Зори", "1 билет - 48₽"),
            "55" to Triple("AO \"КПАТП-7\"", "п.Цементников → Ж/д вокзал", "1 билет - 48₽"),
            "56" to Triple("AO \"КПАТП-7\"", "Поликлиника → Спортзал", "1 билет - 48₽"),
            "58" to Triple("ИП Голунцов С. В.", "мкр-н Солнечный → Авт.Вост", "1 билет - 48₽"),
            "60" to Triple("ИП Цугленок М.М.", "мкр-н Солнечный → Шинное клдбщ", "1 билет - 48₽"),
            "61" to Triple("ООО «КПАТП»", "мкр-н Солнечный → Академгородок", "1 билет - 48₽"),
            "63" to Triple("ООО \"КПАТП\"", "мкр-н Солнечный → мкр-н Т.Зори", "1 билет - 48₽"),
            "64" to Triple("AO \"КПАТП-5\"", "ДК Кировский → Агротерминал", "1 билет - 48₽"),
            "65" to Triple("ИП Ялтонский А.М.", "ул. Петрушина → ИТК", "1 билет - 48₽"),
            "69" to Triple("АО \"КПАТП-7\"", "Спортзал → пос. Таймыр", "1 билет - 48₽"),
            "71" to Triple("ООО \"Экипаж-ГО\"", "п. Песчанка → Ж/д больница", "1 билет - 48₽"),
            "77" to Triple("ООО \"Практик\"", "Стела → Контейнер. двор", "1 билет - 48₽"),
            "78" to Triple("ИП Бронников А. И.", "мкр-н Т.Зори → пос.Таймыр", "1 билет - 48₽"),
            "80" to Triple("ИП Долгушин Д.Г.", "Ж/д больница → ОАО Русал", "1 билет - 48₽"),
            "81" to Triple("ООО \"Сирена\"", "Дом Учёных → Проф.КраМЗа", "1 билет - 48₽"),
            "83" to Triple("ИП Цугленок М.М.", "Сельхозкомпл → Верх.Черёмушки", "1 билет - 48₽"),
            "85" to Triple("ООО \"СКАД\"", "Сельхозкомпл → Верх.Черёмушки", "1 билет - 48₽"),
            "87" to Triple("AO \"КПАТП-5\"", "Спортзал → Акад.Биатлона", "1 билет - 48₽"),
            "88" to Triple("ИП Тагачаков В.Г.", "В.Базаиха → Акад.Биатлона", "1 билет - 48₽"),
            "90" to Triple("ИП \"Патрин Н.Н.\"", "В.Базаиха → Акад.Биатлона", "1 билет - 48₽"),
            "92" to Triple("ИП Патрин Н.Н.", "Химкомб.Енисей → Красфарма", "1 билет - 48₽"),
            "94" to Triple("ООО \"Практик\"", "Верх.Черёмушки → ЛДК", "1 билет - 48₽"),
            "95" to Triple("AO \"КПАТП-7\"", "Верх.Черёмушки → ЛДК", "1 билет - 48₽"),
            "98" to Triple("ИП Гнетов Ю. Н.", "Ст.Кр-ск-Сев → ул.Цимлянская", "1 билет - 48₽"),
            "99" to Triple("ООО \"СТК\"", "Ст.Кр-ск-Сев → ул.Цимлянская", "1 билет - 48₽")
        )

        fun getRouteById(id: String): Triple<String, String, String>? {
            return routes[id]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инициализируем SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noTicketLayout = view.findViewById(R.id.noTicketLayout)
        ticketContainer = view.findViewById(R.id.ticketContainer)
        buyButton = view.findViewById(R.id.button)

        buyButton.setOnClickListener {
            showSimpleDialog()
        }

        // Восстанавливаем билет при создании фрагмента
        restoreTicket()
    }

    private fun restoreTicket() {
        // Пытаемся восстановить сохраненный билет
        val savedTicketCode = sharedPreferences.getString(KEY_TICKET_CODE, null)
        val savedTicketTime = sharedPreferences.getLong(KEY_TICKET_TIME, 0)

        if (savedTicketCode != null && savedTicketTime > 0) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - savedTicketTime
            val ticketDuration = 45 * 60 * 1000L // 45 минут в миллисекундах

            if (elapsedTime < ticketDuration) {
                // Билет еще действителен
                val remainingTime = ticketDuration - elapsedTime
                showTicket(savedTicketCode, remainingTime, false)
            } else {
                // Билет истек
                clearTicket()
            }
        }
    }

    private fun showSimpleDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.code_input)

        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val editText = dialog.findViewById<EditText>(R.id.editTextCode)
        val submitButton = dialog.findViewById<TextView>(R.id.buttonSubmit)

        submitButton.setOnClickListener {
            val code = editText.text.toString().trim()

            if (code.isNotEmpty()) {
                showTicket(code, 45 * 60 * 1000L, true)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Введите номер автобуса и маршрута", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun showTicket(code: String, durationMillis: Long, saveToPrefs: Boolean) {
        countDownTimer?.cancel()

        val parts = code.split(" ")
        if (parts.size < 2) {
            val intent = Intent(requireContext(), IncorrectCodeActivity::class.java)
            startActivity(intent)
            return
        }
        val routeId = parts[0]
        val routeNumber = parts[1]

        // Используем getRouteById из companion object
        val route = getRouteById(routeId)

        if (route == null) {
            val intent = Intent(requireContext(), IncorrectCodeActivity::class.java)
            startActivity(intent)
            return
        }

        val calendar = Calendar.getInstance()
        val currentDate = calendar.time

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(currentDate)

        val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        // Скрываем только сообщение "Нет билета"
        noTicketLayout.visibility = View.GONE
        ticketContainer.visibility = View.VISIBLE
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
        routeText.text = route.second
        routeNumberTicket.text = "№$routeId"
        busNumberTicket.text = routeNumber
        companyText.text = route.first
        priceText.text = route.third

        // Сохраняем билет в SharedPreferences если нужно
        if (saveToPrefs) {
            saveTicketCode(code)
        }

        // Запускаем таймер
        startCountDownTimer(minutesText, secondsText, durationMillis)

        ticketContainer.addView(ticketView)
    }

    private fun startCountDownTimer(minutesText: TextView, secondsText: TextView, durationMillis: Long) {
        // Рассчитываем начальные значения
        val initialMinutes = durationMillis / 1000 / 60
        val initialSeconds = (durationMillis / 1000) % 60

        minutesText.text = initialMinutes.toString()
        secondsText.text = String.format("%02d", initialSeconds)

        countDownTimer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60

                minutesText.text = minutes.toString()
                secondsText.text = String.format("%02d", seconds)

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

                // Очищаем билет при истечении времени
                clearTicket()
            }
        }.start()
    }

    private fun saveTicketCode(code: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_TICKET_CODE, code)
        editor.putLong(KEY_TICKET_TIME, System.currentTimeMillis()) // Сохраняем текущее время
        editor.apply()
    }

    private fun clearTicket() {
        // Очищаем SharedPreferences
        val editor = sharedPreferences.edit()
        editor.remove(KEY_TICKET_CODE)
        editor.remove(KEY_TICKET_TIME)
        editor.apply()

        // Очищаем UI
        ticketContainer.visibility = View.GONE
        noTicketLayout.visibility = View.VISIBLE
        ticketContainer.removeAllViews()
        countDownTimer?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
    }
}