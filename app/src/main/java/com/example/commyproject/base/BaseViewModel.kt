package com.example.commyproject.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel constructor(
    private val api: ApiClient,
    private val share: SharedPreferenceUtils
): ViewModel() {

    abstract val profileId: String
    abstract val profileUserName: String
    abstract val profileAvatar: String

    abstract var toId: String?
    abstract var toUserName: String?
//    abstract fun postLike(evaluation: EvaluationEntity, callback: (Evaluation) -> Unit): Job
//    abstract fun postComment(cmt: CommentEntity, callback: (Comment) -> Unit) : Job

    fun postComment(cmt: CommentEntity, callback: (Comment) -> Unit) = viewModelScope.launch(
        Dispatchers.IO) {
        api.postComment(cmt) {
            callback(it)
        }
    }
    fun postLike(evaluation: EvaluationEntity, callback: (Evaluation) -> Unit) = viewModelScope.launch(
        Dispatchers.IO) {
        api.postLike(evaluation) {
            callback(it)
        }
    }

}