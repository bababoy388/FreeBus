package com.example.newfreebus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HistoryFragment : Fragment() {

    // Этот метод создаёт экран фрагмента
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Просто возвращаем твой старый layout
        return inflater.inflate(R.layout.list_tikets, container, false)
    }
}