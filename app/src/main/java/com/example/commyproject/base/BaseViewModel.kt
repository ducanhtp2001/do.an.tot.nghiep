package com.example.commyproject.base

import androidx.lifecycle.ViewModel
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

    abstract val profileId: String
    abstract val profileUserName: String
    abstract val profileAvatar: String

    abstract var toId: String?
    abstract var toUserName: String?
    abstract fun postLike(evaluation: EvaluationEntity, callback: (Evaluation) -> Unit): Job
    abstract fun postComment(cmt: CommentEntity, callback: (Comment) -> Unit) : Job
}