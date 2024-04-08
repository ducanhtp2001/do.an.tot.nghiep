package com.example.commyproject.activities.user.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.base.NetworkHelper
import com.example.commyproject.data.model.User
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import com.example.commyproject.ultil.Constant
import com.taymay.taoday.service.SocketIOManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterActViewModel @Inject constructor(
    private val networkHelperCheck: NetworkHelper,
    val api: ApiClient,
    val share: SharedPreferenceUtils,
    val socket: SocketIOManager
): ViewModel() {
    val mTAG = "register_act"

    fun register(user: User)= viewModelScope.launch {
        api.register(user) { user ->
            if (user.id != "") {
                val userData = user.id + "_" + user.userName + "_" + user.passWord
                share.putStringValue(Constant.ID_USER, userData)
            } else {
                Log.d(mTAG, "false register")
            }
        }
    }

    private fun checkNetWork(): Boolean {
        return networkHelperCheck.isNetworkConnected()
    }
}