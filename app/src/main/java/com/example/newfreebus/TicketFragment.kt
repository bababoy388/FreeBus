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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.newfreebus.data.model.Bus
import com.example.newfreebus.data.remote.RetrofitClient
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TicketFragment : Fragment() {

    private lateinit var noTicketLayout: LinearLayout
    private lateinit var ticketContainer: FrameLayout
    private lateinit var buyButton: MaterialButton
    private lateinit var buyButtonTwo: MaterialButton
    private lateinit var showButton: MaterialButton
    private lateinit var buttonsContainer: ConstraintLayout
    private var countDownTimer: CountDownTimer? = null

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "TicketPrefs"
        private const val KEY_TICKET_CODE = "ticket_code"
        private const val KEY_TICKET_TIME = "ticket_time"

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

        fun getRouteById(id: String): Triple<String, String, String>? = routes[id]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noTicketLayout = view.findViewById(R.id.noTicketLayout)
        ticketContainer = view.findViewById(R.id.ticketContainer)
        buyButton = view.findViewById(R.id.buyButton)
        buyButtonTwo = view.findViewById(R.id.buyButtonTwo)
        showButton = view.findViewById(R.id.showButton)
        buttonsContainer = view.findViewById(R.id.buttonsContainer)

        buttonsContainer.visibility = View.GONE
        buyButton.visibility = View.VISIBLE

        buyButton.setOnClickListener { showSimpleDialog() }
        buyButtonTwo.setOnClickListener { showSimpleDialog() }

        showButton.setOnClickListener {
            if (TimerManager.hasActiveTimer(requireContext())) {
                startActivity(Intent(requireContext(), QrTicket::class.java))
            } else {
                Toast.makeText(requireContext(), "Нет активного билета", Toast.LENGTH_SHORT).show()
            }
        }

        restoreTicket()
    }

    private fun restoreTicket() {
        val savedTicketCode = sharedPreferences.getString(KEY_TICKET_CODE, null)
        val savedTicketTime = sharedPreferences.getLong(KEY_TICKET_TIME, 0)

        if (savedTicketCode != null && savedTicketTime > 0) {
            val elapsed = System.currentTimeMillis() - savedTicketTime
            val ticketDuration = 45 * 60 * 1000L
            if (elapsed < ticketDuration) {
                processTicketInput(savedTicketCode, ticketDuration - elapsed, false)
            } else {
                clearTicket()
            }
        } else if (TimerManager.hasActiveTimer(requireContext())) {
            val remaining = TimerManager.getRemainingTime(requireContext())
            if (remaining > 0) {
                processTicketInput(TimerManager.getTimerState(requireContext()).ticketCode, remaining, false)
            } else {
                TimerManager.clearTimer(requireContext())
            }
        }
    }

    private fun showSimpleDialog() {
        Dialog(requireContext()).apply {
            setContentView(R.layout.code_input)
            window?.setLayout((resources.displayMetrics.widthPixels * 0.9).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)

            val editText = findViewById<EditText>(R.id.editTextCode)
            val submit = findViewById<TextView>(R.id.buttonSubmit)
            val cancel = findViewById<TextView>(R.id.buttonBack)
            val qr = findViewById<TextView>(R.id.qrButton)

            qr.setOnClickListener {
                startActivity(Intent(requireContext(), QrScanner::class.java))
                dismiss()
            }

            submit.setOnClickListener {
                val input = editText.text.toString().trim()
                if (input.isNotEmpty()) {
                    processTicketInput(input, 45 * 60 * 1000L, true)
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Введите номер автобуса и маршрута", Toast.LENGTH_SHORT).show()
                }
            }

            cancel.setOnClickListener { dismiss() }
            show()
        }
    }

    private fun processTicketInput(input: String, durationMillis: Long, saveToPrefs: Boolean) {
        if (input.contains(" ")) {
            val parts = input.split(" ", limit = 2)
            val routeId = parts[0]
            val busNumber = parts[1]
            val route = getRouteById(routeId)
            if (route != null) {
                showLocalTicket(routeId, busNumber, route, durationMillis, saveToPrefs)
            } else {
                Toast.makeText(requireContext(), "Маршрут с номером $routeId не найден", Toast.LENGTH_SHORT).show()
            }
        } else {
            try {
                fetchAndShowTicket(input.toInt(), durationMillis, saveToPrefs)
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Введите число или номер маршрута и номер автобуса через пробел", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchAndShowTicket(code: Int, durationMillis: Long, saveToPrefs: Boolean) {
        lifecycleScope.launch {
            try {
                val bus = withContext(Dispatchers.IO) { RetrofitClient.apiService.getBuses(code) }
                if (isAdded) {
                    displayTicket(bus, code.toString(), durationMillis, saveToPrefs)
                }
            } catch (e: Exception) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Ошибка загрузки: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayTicket(bus: Bus, code: String, durationMillis: Long, saveToPrefs: Boolean) {
        countDownTimer?.cancel()
        val now = Calendar.getInstance()
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now.time)
        val date = SimpleDateFormat("d MMMM", Locale.getDefault()).format(now.time)

        noTicketLayout.visibility = View.GONE
        ticketContainer.visibility = View.VISIBLE
        buttonsContainer.visibility = View.VISIBLE
        buyButton.visibility = View.GONE
        ticketContainer.removeAllViews()

        layoutInflater.inflate(R.layout.ticket, ticketContainer, true).apply {
            findViewById<TextView>(R.id.timeText).text = time
            findViewById<TextView>(R.id.dateText).text = date
            findViewById<TextView>(R.id.routeNumber).text = bus.routerNumber.toString()
            findViewById<TextView>(R.id.busNumber).text = bus.busNumber
            findViewById<TextView>(R.id.routeText).text = bus.router
            findViewById<TextView>(R.id.companyText).text = bus.company
            findViewById<TextView>(R.id.priceText).text = bus.price

            TimerManager.saveTimerState(requireContext(), System.currentTimeMillis(), durationMillis, code, bus.routerNumber.toString(), bus.busNumber)
            if (saveToPrefs) saveTicketCode(code)

            startCountDownTimer(findViewById(R.id.minutesText), findViewById(R.id.secondsText), durationMillis)
        }
    }

    private fun showLocalTicket(routeId: String, busNumber: String, route: Triple<String, String, String>, durationMillis: Long, saveToPrefs: Boolean) {
        countDownTimer?.cancel()
        val now = Calendar.getInstance()
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now.time)
        val date = SimpleDateFormat("d MMMM", Locale.getDefault()).format(now.time)

        noTicketLayout.visibility = View.GONE
        ticketContainer.visibility = View.VISIBLE
        buttonsContainer.visibility = View.VISIBLE
        buyButton.visibility = View.GONE
        ticketContainer.removeAllViews()

        layoutInflater.inflate(R.layout.ticket, ticketContainer, true).apply {
            findViewById<TextView>(R.id.timeText).text = time
            findViewById<TextView>(R.id.dateText).text = date
            findViewById<TextView>(R.id.routeNumber).text = routeId
            findViewById<TextView>(R.id.busNumber).text = busNumber
            findViewById<TextView>(R.id.routeText).text = route.second
            findViewById<TextView>(R.id.companyText).text = route.first
            findViewById<TextView>(R.id.priceText).text = route.third

            val code = "$routeId $busNumber"
            TimerManager.saveTimerState(requireContext(), System.currentTimeMillis(), durationMillis, code, routeId, busNumber)
            if (saveToPrefs) saveTicketCode(code)

            startCountDownTimer(findViewById(R.id.minutesText), findViewById(R.id.secondsText), durationMillis)
        }
    }

    private fun startCountDownTimer(minutesText: TextView, secondsText: TextView, durationMillis: Long) {
        val remaining = TimerManager.getRemainingTime(requireContext()).takeIf { it > 0 } ?: durationMillis
        minutesText.text = (remaining / 1000 / 60).toString()
        secondsText.text = String.format("%02d", (remaining / 1000) % 60)

        countDownTimer = object : CountDownTimer(remaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                minutesText.text = (millisUntilFinished / 1000 / 60).toString()
                secondsText.text = String.format("%02d", (millisUntilFinished / 1000) % 60)
            }
            override fun onFinish() {
                minutesText.text = "0"
                secondsText.text = "00"
                minutesText.setTextColor(Color.RED)
                secondsText.setTextColor(Color.RED)
                clearTicket()
            }
        }.start()
    }

    private fun saveTicketCode(code: String) {
        sharedPreferences.edit().apply {
            putString(KEY_TICKET_CODE, code)
            putLong(KEY_TICKET_TIME, System.currentTimeMillis())
            apply()
        }
    }

    private fun clearTicket() {
        sharedPreferences.edit().apply {
            remove(KEY_TICKET_CODE)
            remove(KEY_TICKET_TIME)
            apply()
        }
        TimerManager.clearTimer(requireContext())
        buttonsContainer.visibility = View.GONE
        buyButton.visibility = View.VISIBLE
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