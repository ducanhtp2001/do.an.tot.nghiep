package com.example.commyproject.activities.main.fragment.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.commyproject.base.BaseViewModel
import com.example.commyproject.data.model.FileEntity
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.Notification
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class NotificationFragmentViewModel @Inject constructor(
    private val api: ApiClient,
    private val share: SharedPreferenceUtils
) : BaseViewModel(api, share){

    val user = getUserData()
    override val profileId: String = user._id
    override val profileUserName: String = user.userName
    override val profileAvatar: String = user.avatar
    override var toId: String? = null
    override var toUserName: String? = null

    private val _notifications = MutableLiveData<List<Notification>>().apply {
        value = emptyList()
    }
    val list: LiveData<List<Notification>>
        get() = _notifications

    private val _file = MutableLiveData<FileEntry>().apply {
        value = null
    }
    val file: LiveData<FileEntry>
        get() = _file
    fun getNotifications(user: UserEntity) = viewModelScope.launch(Dispatchers.IO) {
        api.getNotifications(user) {
            _notifications.postValue(it)
        }
    }

    fun getSingleFile(file: FileEntity) = viewModelScope.launch(Dispatchers.IO) {
        api.getSingleFile(file) {
            _file.postValue(it)
        }
    }
}