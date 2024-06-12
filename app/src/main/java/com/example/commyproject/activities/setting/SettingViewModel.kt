package com.example.commyproject.activities.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.data.model.CODE_ACTION
import com.example.commyproject.data.model.CodeEntry
import com.example.commyproject.data.model.User
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.data.network.ApiService
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val api: ApiClient,
    private val share: SharedPreferenceUtils
) : ViewModel() {
    fun getUserData() = share.getUser()

    val user = getUserData()
    fun clearData() = viewModelScope.launch {
        share.logout()
    }

    fun updateUser(
        user: User,
        callback: (User) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        api.updateUser(user) {
            if (user._id != "") {
                share.putUser(user)
                callback(it)
            }
        }
    }
}