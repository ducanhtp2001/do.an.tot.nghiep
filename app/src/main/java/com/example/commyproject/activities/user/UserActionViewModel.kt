package com.example.commyproject.activities.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.base.NetworkHelper
import com.example.commyproject.base.NetworkLiveData
import com.example.commyproject.data.model.User
import com.example.commyproject.data.model.networkresponse.MsgResponse
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import com.taymay.taoday.service.SocketIOManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserActionViewModel @Inject constructor(
    val share: SharedPreferenceUtils,
    val socket: SocketIOManager,
    val api: ApiClient
): ViewModel(){
    val user = share.getUser()

    fun logout() = viewModelScope.launch {
        share.logout()
    }
    fun changePass(newPass: String, callback:(MsgResponse) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val update = user
        update.passWord = newPass
        api.changePass(update) {
            callback(it)
        }
    }

    fun forgetPass(userName: String, newPass: String, callback:(MsgResponse) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val update = User()
        update.userName = userName
        update.passWord = newPass
        api.forgetPass(update) {
            callback(it)
        }
    }
}