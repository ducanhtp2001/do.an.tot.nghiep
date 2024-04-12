package com.example.commyproject.activities.main

import androidx.lifecycle.ViewModel
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.taymay.taoday.service.SocketIOManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActViewModel @Inject constructor(
    val socket: SocketIOManager,
    val share: SharedPreferenceUtils,
): ViewModel() {
    init {
        socket.socketConnect()
    }
}