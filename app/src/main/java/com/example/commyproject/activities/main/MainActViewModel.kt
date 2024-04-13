package com.example.commyproject.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    init {
        socket.socketConnect()
        socket.login()
    }

    fun getUser() = share.getUser()
}