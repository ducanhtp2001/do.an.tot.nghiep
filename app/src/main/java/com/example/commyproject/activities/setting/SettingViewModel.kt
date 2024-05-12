package com.example.commyproject.activities.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.data.share.SharedPreferenceUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val share: SharedPreferenceUtils
): ViewModel() {
    fun getUserData() = share.getUser()
    fun clearData() = viewModelScope.launch {
        share.logout()
    }
}