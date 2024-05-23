package com.example.commyproject.activities.find

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.data.model.FollowerResponse
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.data.model.UserName
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class FindViewModel @Inject constructor(
    private val api: ApiClient,
    private val share: SharedPreferenceUtils,
): ViewModel() {

    val user by lazy {
        share.getUser()
    }

    private val _userList = MutableLiveData<List<FollowerResponse>>().apply {
        value = mutableListOf()
    }
    val list : LiveData<List<FollowerResponse>>
        get() = _userList

    fun findUser(str: String) = viewModelScope.launch(Dispatchers.IO) {
        val userName = UserName(str)
        val userList = async { api.getUserByName(userName) }.await()
        withContext(Dispatchers.Main) {
            _userList.value = userList
        }
    }
}