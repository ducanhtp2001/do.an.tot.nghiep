package com.example.commyproject.activities.main.fragment.home.global

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.model.FileEntity
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.StatusResponse
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

    var toId: String? = null
    var toUserName: String? = null


    fun getPublicFile() = viewModelScope.launch(Dispatchers.IO) {
        val privateFileData = async { api.getPublicFile(getUser()) }.await()
        withContext(Dispatchers.Main) {
            publicFiles.value = privateFileData
        }
    }

    fun postComment(cmt: CommentEntity, callback: (Comment) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        api.postComment(cmt) {
            callback(it)
        }
    }

    fun postLike(evaluation: EvaluationEntity, callback: (Evaluation) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        api.postLike(evaluation) {
            callback(it)
        }
    }

    fun deleteFile(file: FileEntity, callback: (StatusResponse) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        api.deleteFile(file) {
            if (it.status) callback(it)
            else Log.e("testing", it.msg)
        }
    }

    fun changeState(file: FileEntity, callback: (StatusResponse) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        api.changeState(file) {
            if (it.status) callback(it)
            else Log.e("testing", it.msg)
        }
    }

    fun getUser() = share.getUser()

}