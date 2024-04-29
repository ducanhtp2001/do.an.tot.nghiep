package com.example.commyproject.activities.bottomsheetdialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.model.FileEntity
import com.example.commyproject.data.model.StatusResponse
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject
@HiltViewModel
class DialogViewModel @Inject constructor(
    private val api: ApiClient,
    private val share: SharedPreferenceUtils
): ViewModel() {
    val user = getUserData()
    var toId: String? = null
    var toUserName: String? = null
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
    fun deleteFile(file: FileEntity, callback: (StatusResponse) -> Unit) = viewModelScope.launch(
        Dispatchers.IO) {
        api.deleteFile(file) {
            if (it.status) callback(it)
            else Log.e("testing", it.msg)
        }
    }
    fun changeState(file: FileEntity, callback: (StatusResponse, file: FileEntity) -> Unit) = viewModelScope.launch(
        Dispatchers.IO) {
        api.changeState(file) {
            if (it.status) callback(it, file)
            else Log.e("testing", it.msg)
        }
    }

    fun download(file: FileEntity, callback: (ResponseBody) -> Unit) = viewModelScope.launch {
        api.download(file) {
            callback(it)
        }
    }
    private fun getUserData() = share.getUser()
}