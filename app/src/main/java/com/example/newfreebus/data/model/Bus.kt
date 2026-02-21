package com.example.newfreebus.data.model

data class Bus(
    val routerNumber: Int,
    val busNumber: String,
    val price: String,
    val router: String,
    val company: String
)

data class LoginRequest(
    val token: String
)

data class LoginResponse(
    val valid: Boolean
)

data class CodeInput(
    val code: Int
)

