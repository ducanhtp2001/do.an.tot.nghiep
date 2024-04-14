package com.example.commyproject.activities.main.fragment.home.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocalFragmentViewModel @Inject constructor(
    val share: SharedPreferenceUtils,
    val api: ApiClient,
): ViewModel() {
    private val privateFiles = MutableLiveData<List<FileEntry>>().apply {
        value = emptyList()
    }
    val list: LiveData<List<FileEntry>>
        get() = privateFiles


    fun getPrivateFile() = viewModelScope.launch(Dispatchers.IO) {
        val privateFileData = async { api.getPrivateFile(getUser()) }.await()
        withContext(Dispatchers.Main) {
            privateFiles.value = privateFileData
        }
    }

    fun getUser() = share.getUser()
}