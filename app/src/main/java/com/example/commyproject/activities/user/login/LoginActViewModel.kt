package com.example.commyproject.activities.user.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.base.NetworkHelper
import com.example.commyproject.base.NetworkLiveData
import com.example.commyproject.data.model.User
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import com.example.commyproject.ultil.Constant
import com.example.commyproject.ultil.converter.UserConverter
import com.taymay.taoday.service.SocketIOManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginActViewModel @Inject constructor(
    val networkHelperCheck: NetworkHelper,
    val networkLiveData: NetworkLiveData,
    val share: SharedPreferenceUtils,
    val socket: SocketIOManager,
    val api: ApiClient
): ViewModel() {

    val mTAG = "login_act"

    private val _networkHelper = MutableLiveData(checkNetWork())
    val networkHelper: LiveData<Boolean>
        get() = _networkHelper

    private val _stateLoading = MutableLiveData<Boolean>()
    val stateLoading: LiveData<Boolean>
        get() = _stateLoading

    private val _stateLogin = MutableLiveData<Boolean>()
    val stateLogin: LiveData<Boolean>
        get() = _stateLogin

    private fun loadCache(): User? {
        val userStr = share.getStringValue(Constant.USER, "")
        return UserConverter.str2User(userStr)
    }

    fun login(user: User, onFalse: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        api.login(
            user,
            callback = {
                Log.d(mTAG, "response: $it")
                if (it._id != "") {
                    _stateLoading.postValue(true)
                    share.login(it)
                    _stateLogin.postValue(true)
                } else {
                    Log.d(mTAG, "false login")
                }
                _stateLoading.postValue(false)
            },
            onFalse = {
                onFalse()
            }
        )
    }

    fun checkLogin(onFalse: () -> Unit) = viewModelScope.launch {
        val isLogin = share.checkLogin()
        if (isLogin) {
            val user = share.getUser()
            login(user) {
                onFalse()
            }
        }
    }

    private fun checkNetWork(): Boolean {
        return networkHelperCheck.isNetworkConnected()
    }
}