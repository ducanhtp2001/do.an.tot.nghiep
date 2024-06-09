package com.example.commyproject.activities.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.commyproject.base.BaseViewModel
import com.example.commyproject.data.model.User
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.data.model.networkresponse.ProfileResponse
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import com.example.commyproject.ultil.Constant
import com.example.commyproject.ultil.converter.UserConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val api: ApiClient,
    private val share: SharedPreferenceUtils,
): BaseViewModel(api, share) {
    val user = getUserData()
    override lateinit var profileId: String
    override lateinit var profileUserName: String
    override lateinit var profileAvatar: String

    private val _profile = MutableLiveData<ProfileResponse>().apply {
        value = null
    }

    val loadingState = MutableLiveData<Boolean>().apply {
        value = false
    }
    val profile: LiveData<ProfileResponse>
        get() = _profile

    override var toId: String? = null
    override var toUserName: String? = null

    fun getProfile(userEntity: UserEntity) = viewModelScope.launch(Dispatchers.IO) {
        loadingState.postValue(true)
        val profile = async { api.getProfile(userEntity) }.await()
        withContext(Dispatchers.Main) {
            _profile.value = profile
            loadingState.postValue(false)
        }
    }

    fun uploadImage(context: Context, uri: Uri, userId: String, type: String, callback:(String) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            val userStr = share.getStringValue(Constant.USER, "")
            var user: User?
            if(userStr != "") {
                user = UserConverter.str2User(userStr)
                api.uploadImage(context, uri, type, userId) {
                    val msg = it.msg
                    callback(msg)
                }
                Log.d("testing", userStr)
            }
        }
}