package com.example.commyproject.activities.main.fragment.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.commyproject.base.BaseViewModel
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.FollowerResponse
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FollowFragmentViewModel @Inject constructor(
    private val api: ApiClient,
    private val share: SharedPreferenceUtils
): BaseViewModel(api, share) {
    private val _followFiles = MutableLiveData<List<FileEntry>>().apply {
        value = emptyList()
    }

    val fileList: LiveData<List<FileEntry>>
        get() = _followFiles

    private val _followList = MutableLiveData<List<FollowerResponse>>().apply {
        value = emptyList()
    }

    val followList: LiveData<List<FollowerResponse>>
        get() = _followList

    val user = getUserData()

    fun getFollowFile() = viewModelScope.launch(Dispatchers.IO) {
        val userEntity = UserEntity(user._id)
        val globalFileData = async { api.getFollowFile(userEntity) }.await()
        withContext(Dispatchers.Main) {
            _followFiles.value = globalFileData
        }
    }
    fun getFollowUser() = viewModelScope.launch(Dispatchers.IO) {
        val userEntity = UserEntity(user._id)
        val follows = async { api.getFollowUser(userEntity) }.await()
        withContext(Dispatchers.Main) {
            _followList.value = follows
        }
    }

    override val profileId: String = user._id
    override val profileUserName: String = user.userName
    override val profileAvatar: String = user.avatar
    override var toId: String? = null
    override var toUserName: String? = null

}