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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterActViewModel @Inject constructor(
    private val networkHelperCheck: NetworkHelper,
    val api: ApiClient,
    val share: SharedPreferenceUtils,
): ViewModel() {
    val mTAG = "testing register"

    private val _networkHelper = MutableLiveData(checkNetWork())
    val networkHelper: LiveData<Boolean>
        get() = _networkHelper

    private val _stateLoading = MutableLiveData<Boolean>()
    val stateLoading: LiveData<Boolean>
        get() = _stateLoading

    private val _stateRegister = MutableLiveData<Boolean>()
    val stateRegister: LiveData<Boolean>
        get() = _stateRegister

    fun register(user: User)= viewModelScope.launch(Dispatchers.IO) {
        api.register(user) { it ->
            Log.d(mTAG, "register: $it")
            if (it._id != "") {
                _stateLoading.postValue(true)
                val userData = it._id + "_" + it.userName + "_" + it.passWord
                Log.d("testing", "on register save to cache: $userData")
                share.putStringValue(Constant.USER, userData)
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