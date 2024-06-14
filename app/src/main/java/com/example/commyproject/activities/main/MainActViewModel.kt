package com.example.commyproject.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.data.model.User
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.taymay.taoday.service.SocketIOManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActViewModel @Inject constructor(
    private val socket: SocketIOManager,
    private val share: SharedPreferenceUtils,
): ViewModel() {

    var user = getUserFromCache()
    fun getUserFromCache() = share.getUser()

    fun fresh(callback: (User) -> Unit) = viewModelScope.launch {
        user = getUserFromCache()
        callback(user)
    }

    fun clearData() = viewModelScope.launch {
        share.logout()
    }
}