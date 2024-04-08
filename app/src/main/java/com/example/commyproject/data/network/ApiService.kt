package com.example.commyproject.data.network

import com.example.commyproject.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("register")
    suspend fun register(@Body user: User): Response<User>

    @POST("login")
    suspend fun login(@Body user: User): Response<User>

    @POST("update-user")
    suspend fun updateUser(@Body user: User): Response<User>

}