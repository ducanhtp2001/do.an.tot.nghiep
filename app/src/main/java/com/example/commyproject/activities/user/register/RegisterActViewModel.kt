package com.example.commyproject.activities.user.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _networkHelper = MutableLiveData(checkNetWork())
    val networkHelper: LiveData<Boolean>
        get() = _networkHelper

    private val _stateLoading = MutableLiveData<Boolean>()
    val stateLoading: LiveData<Boolean>
        get() = _stateLoading

    private val _stateRegister = MutableLiveData<Boolean>()
    val stateRegister: LiveData<Boolean>
        get() = _stateRegister

    fun register(user: User)= viewModelScope.launch {
        api.register(user) { user ->
            if (user.id != "") {
                _stateLoading.postValue(true)
                val userData = user.id + "_" + user.userName + "_" + user.passWord
                share.putStringValue(Constant.ID_USER, userData)
                _stateRegister.postValue(true)
            } else {
                Log.d(mTAG, "false register")
            }
            _stateLoading.postValue(false)
        }
    }

    private fun checkNetWork(): Boolean {
        return networkHelperCheck.isNetworkConnected()
    }
}