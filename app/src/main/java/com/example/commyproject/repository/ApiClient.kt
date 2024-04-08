package com.example.commyproject.repository

import com.example.commyproject.data.model.User
import com.example.commyproject.data.network.ApiService
import javax.inject.Inject

class ApiClient @Inject constructor(private val apiService: ApiService) {

    suspend fun register(userData: User, callback: (User) -> Unit) {
        val response = apiService.register(userData)
        if (response.isSuccessful) {
            callback(response.body()!!)
        } else {

        }
    }
}