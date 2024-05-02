package com.example.commyproject.activities.main.fragment.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.GlobalFile
import com.example.commyproject.data.model.KeyRecommend
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GlobalFragmentViewModel @Inject constructor(
    private val api: ApiClient,
    private val share: SharedPreferenceUtils
): ViewModel() {
    private val _recommendFiles = MutableLiveData<List<GlobalFile>>().apply {
        value = emptyList()
    }
    val list: LiveData<List<GlobalFile>>
        get() = _recommendFiles

    fun getGlobalFile(key: KeyRecommend) = viewModelScope.launch(Dispatchers.IO) {
        val privateFileData = async { api.getGlobalFile(key) }.await()
        withContext(Dispatchers.Main) {
            _recommendFiles.value = privateFileData
        }
    }

    fun getUser() = share.getUser()
}