package com.example.newfreebus

import android.content.Context
import android.content.SharedPreferences

object TimerManager {
    private const val PREFS_NAME = "TimerPrefs"
    private const val KEY_START_TIME = "timer_start_time"
    private const val KEY_DURATION = "timer_duration"
    private const val KEY_TICKET_CODE = "timer_ticket_code"
    private const val KEY_ROUTE_ID = "route_id"
    private const val KEY_ROUTE_NUMBER = "route_number"
    private const val KEY_BUY_TIME = "buy_time"

    fun saveTimerState(context: Context, startTime: Long, duration: Long,
                       ticketCode: String, routeId: String, routeNumber: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong(KEY_START_TIME, startTime)
        editor.putLong(KEY_DURATION, duration)
        editor.putString(KEY_TICKET_CODE, ticketCode)
        editor.putString(KEY_ROUTE_ID, routeId)
        editor.putString(KEY_ROUTE_NUMBER, routeNumber)
        editor.putLong(KEY_BUY_TIME, System.currentTimeMillis())
        editor.apply()
    }

    fun getRemainingTime(context: Context): Long {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val startTime = prefs.getLong(KEY_START_TIME, 0)
        val duration = prefs.getLong(KEY_DURATION, 0)

        if (startTime == 0L || duration == 0L) return 0L

        val currentTime = System.currentTimeMillis()
        val elapsed = currentTime - startTime
        val remaining = duration - elapsed

        return if (remaining > 0) remaining else 0
    }

    fun getTicketCode(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TICKET_CODE, null)
    }

    fun getRouteId(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_ROUTE_ID, null)
    }

    fun getRouteNumber(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_ROUTE_NUMBER, null)
    }

    fun getBuyTime(context: Context): Long {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getLong(KEY_BUY_TIME, 0)
    }

    fun clearTimer(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    fun hasActiveTimer(context: Context): Boolean {
        return getRemainingTime(context) > 0
    }

    fun getTimerState(context: Context): TimerState {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return TimerState(
            startTime = prefs.getLong(KEY_START_TIME, 0),
            duration = prefs.getLong(KEY_DURATION, 0),
            ticketCode = prefs.getString(KEY_TICKET_CODE, null) ?: "",
            routeId = prefs.getString(KEY_ROUTE_ID, null) ?: "",
            routeNumber = prefs.getString(KEY_ROUTE_NUMBER, null) ?: "",
            buyTime = prefs.getLong(KEY_BUY_TIME, 0)
        )
    }

    data class TimerState(
        val startTime: Long,
        val duration: Long,
        val ticketCode: String,
        val routeId: String,
        val routeNumber: String,
        val buyTime: Long
    )
}