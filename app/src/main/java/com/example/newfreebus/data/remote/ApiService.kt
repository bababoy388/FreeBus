package com.example.newfreebus.data.remote

import com.example.newfreebus.data.model.Bus
import com.example.newfreebus.data.model.LoginRequest
import com.example.newfreebus.data.model.LoginResponse
import com.example.newfreebus.data.model.CodeInput
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    @POST("code")
    suspend fun getBuses(@Body request: Int): Bus
}

