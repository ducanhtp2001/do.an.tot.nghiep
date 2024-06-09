package com.example.commyproject.activities.main.fragment.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.commyproject.base.BaseViewModel
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.KeyRecommend
import com.example.commyproject.data.model.requestmodel.RequestFollow
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
): BaseViewModel(api, share) {
    private val _recommendFiles = MutableLiveData<List<FileEntry>>().apply {
        value = emptyList()
    }

    val list: LiveData<List<FileEntry>>
        get() = _recommendFiles

    val user = getUserData()

    var spinnerOption: Int = 0
    var time: Int = 1
    var keyword: String? = ""

    fun getGlobalFile(key: KeyRecommend) = viewModelScope.launch(Dispatchers.IO) {
        if (key.keyword == keyword) {
            time++
            key.time = time
        } else {
            keyword = key.keyword
            time = 1
        }
        val globalFileData = async { api.getGlobalFile(key) }.await()
        withContext(Dispatchers.Main) {
            _recommendFiles.value = globalFileData
        }
    }

    fun followUser(data: RequestFollow, callback: (String) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        api.followUser(data) {
            callback(it.msg)
        }
    }

    override val profileId: String = user._id
    override val profileUserName: String = user.userName
    override val profileAvatar: String = user.avatar
    override var toId: String? = null
    override var toUserName: String? = null
}