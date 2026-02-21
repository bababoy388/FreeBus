package com.example.newfreebus

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.activity.enableEdgeToEdge

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()
        window.statusBarColor = android.graphics.Color.parseColor("#40000000")
        window.navigationBarColor = Color.BLACK

        val toolbarr = findViewById<Toolbar>(R.id.toolbar)
        ViewCompat.setOnApplyWindowInsetsListener(toolbarr) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = v.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = systemBars.top
            v.layoutParams = params
            insets
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        // Показываем TicketFragment при запуске
        if (savedInstanceState == null) {
            showFragment(TicketFragment())
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.my_ticket -> {
                    showFragment(TicketFragment())
                    toolbar.visibility = View.VISIBLE
                    toolbar.title = "Мой билет"
                    true
                }
                R.id.history -> {
                    showFragment(HistoryFragment())
                    toolbar.visibility = View.VISIBLE
                    toolbar.title = "История"
                    true
                }
                R.id.paying -> {
                    showFragment(PayingFragment())
                    toolbar.visibility = View.VISIBLE
                    toolbar.title = "Оплата"
                    true
                }
                R.id.menu -> {
                    showFragment(MenuFragment())
                    toolbar.visibility = View.GONE
                    true
                }
                else -> false
            }
        }

        // Пункт по умолчанию
        bottomNavigationView.selectedItemId = R.id.my_ticket
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}