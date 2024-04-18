package com.example.commyproject.activities.main.fragment.home.global

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
class PublicFragmentViewModel @Inject constructor(
    private val api: ApiClient,
    private val share: SharedPreferenceUtils
): ViewModel() {

    private val publicFiles = MutableLiveData<List<FileEntry>>().apply {
        value = emptyList()
    }
    val list: LiveData<List<FileEntry>>
        get() = publicFiles


    fun getPublicFile() = viewModelScope.launch(Dispatchers.IO) {
        val privateFileData = async { api.getPublicFile(getUser()) }.await()
        withContext(Dispatchers.Main) {
            publicFiles.value = privateFileData
        }
    }

    fun getUser() = share.getUser()

}