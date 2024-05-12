package com.example.commyproject.activities.main.fragment.home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class HomeFragmentViewModel @Inject constructor(
    val api: ApiClient,
    val share: SharedPreferenceUtils,
    val socket: SocketIOManager
) : ViewModel() {

    private val _stateLoading = MutableLiveData<Boolean>()
    val stateLoading: LiveData<Boolean>
        get() = _stateLoading

    var msg = ""
    fun upload(context: Context, uri: Uri, description: String, fileId: String, userId: String, callback:() -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            val userStr = share.getStringValue(Constant.USER, "")
            var user: User?
            if(userStr != "") {
                user = UserConverter.str2User(userStr)
                val mFileName = "$fileId.pdf"

                api.upload(context, uri, description, mFileName, userId, fileId) {
                    _stateLoading.postValue(true)
                    msg = it.msg
                    socket.requestExecute()
                    _stateLoading.postValue(false)
                }
                Log.d("testing", userStr)
            }
        }

    fun getUser() = share.getUser()
}