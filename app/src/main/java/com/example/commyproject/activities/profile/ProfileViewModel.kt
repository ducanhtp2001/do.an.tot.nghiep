package com.example.commyproject.activities.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.base.BaseViewModel
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.model.ProfileResponse
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.data.model.UserResponse
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val api: ApiClient,
    private val share: SharedPreferenceUtils,
): BaseViewModel() {
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

    override fun postLike(evaluation: EvaluationEntity, callback: (Evaluation) -> Unit) = viewModelScope.launch(
        Dispatchers.IO) {
        api.postLike(evaluation) {
            callback(it)
        }
    }

    override fun postComment(cmt: CommentEntity, callback: (Comment) -> Unit) = viewModelScope.launch(
        Dispatchers.IO) {
        api.postComment(cmt) {
            callback(it)
        }
    }

    private fun getUserData() = share.getUser()
}