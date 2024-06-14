package com.example.commyproject.activities.bottomsheetdialog

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.commyproject.base.BaseViewModel
import com.example.commyproject.data.model.CODE_ACTION
import com.example.commyproject.data.model.CodeEntry
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.model.FileEntity
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.data.model.UserName
import com.example.commyproject.data.model.networkresponse.MsgResponse
import com.example.commyproject.data.model.networkresponse.StatusResponse
import com.example.commyproject.data.model.requestmodel.RequestFollow
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.repository.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject
@HiltViewModel
class DialogViewModel @Inject constructor(
    private val api: ApiClient,
    private val share: SharedPreferenceUtils
): BaseViewModel(api, share) {
    val user = getUserData()
    override val profileId: String = user._id
    override val profileUserName: String = user.userName
    override val profileAvatar: String = user.avatar

    override var toId: String? = null
    override var toUserName: String? = null

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

    fun followUser(data: RequestFollow, callback: (String) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        api.followUser(data) {
            callback(it.msg)
        }
    }

    fun changeGmail(
        user: UserEntity,
        callback: (MsgResponse) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        val response = api.changeGmail(userEntity = user)
        response?.let {
            withContext(Dispatchers.Main) {
                callback(response)
            }
        }
    }

    fun verifyCode(
        actionCode: ACTION_CODE,
        code: String,
        userName: String = "",
        callback: (MsgResponse) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (actionCode == ACTION_CODE.EMAIL) {
            api.postCode(CodeEntry.initCodeToVerify(user._id, code)) {
                callback(it)
            }
        } else {
            api.postCode(CodeEntry.initCodeToReset(userName, code)) {
                callback(it)
            }
        }
    }
}