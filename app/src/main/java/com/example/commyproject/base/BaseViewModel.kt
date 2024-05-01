package com.example.commyproject.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.model.User
import com.example.commyproject.data.model.UserResponse
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    abstract val userId: String
    abstract val userName: String
    abstract val userAvatar: String

    abstract var toId: String?
    abstract var toUserName: String?
    abstract fun postLike(evaluation: EvaluationEntity, callback: (Evaluation) -> Unit): Job
    abstract fun postComment(cmt: CommentEntity, callback: (Comment) -> Unit) : Job
}