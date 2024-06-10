package com.example.commyproject.repository

import android.content.Context
import android.net.Uri
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.model.FileEntity
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.FollowerResponse
import com.example.commyproject.data.model.KeyRecommend
import com.example.commyproject.data.model.Notification
import com.example.commyproject.data.model.networkresponse.MsgResponse
import com.example.commyproject.data.model.networkresponse.ProfileResponse
import com.example.commyproject.data.model.networkresponse.StatusResponse
import com.example.commyproject.data.model.User
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.data.model.UserName
import com.example.commyproject.data.model.requestmodel.RequestFollow
import com.example.commyproject.data.network.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class ApiClient @Inject constructor(private val apiService: ApiService) {

    suspend fun register(userData: User, callback: (User) -> Unit) {
        val response = apiService.register(userData)
        if (response.isSuccessful) {
            callback(response.body()!!)
        } else {

        }
    }
    suspend fun login(user: User, callback: (User) -> Unit) {
        val response = apiService.login(user)
        if (response.isSuccessful) {
            callback(response.body()!!)
        } else {

        }
    }

    suspend fun getPrivateFile(user: User): List<FileEntry> {
        val response = apiService.getPrivateFile(user)
//        if (response.isSuccessful) {
//            callback(response.body()!!)
//        } else {
//
//        }
        return response.body()!!
    }

    suspend fun getPublicFile(user: User): List<FileEntry> {
        val response = apiService.getPublicFile(user)
        return response.body()!!
    }

    suspend fun postComment(cmt: CommentEntity, callback: (Comment) -> Unit) {
        val response = apiService.postComment(cmt)
        if (response.isSuccessful) {
            callback(response.body()!!)
        }
    }

    suspend fun postLike(evaluation: EvaluationEntity, callback: (Evaluation) -> Unit) {
        val response = apiService.postLike(evaluation)
        if (response.isSuccessful) {
            callback(response.body()!!)
        }
    }

    suspend fun deleteFile(file: FileEntity, callback: (StatusResponse) -> Unit) {
        val response = apiService.deleteFile(file)
        if (response.isSuccessful) {
            callback(response.body()!!)
        }
    }

    suspend fun changeState(file: FileEntity, callback: (StatusResponse) -> Unit) {
        val response = apiService.changeState(file)
        if (response.isSuccessful) {
            callback(response.body()!!)
        }
    }

    suspend fun changeGmail(userEntity: UserEntity): MsgResponse? {
        val response = apiService.changeGmail(userEntity)
        if (response.isSuccessful) {
            return response.body()!!
        }
        return null
    }

    suspend fun getProfile(userEntity: UserEntity): ProfileResponse? {
        val response = apiService.getProfile(userEntity)
        if (response.isSuccessful) {
            return response.body()!!
        }
        return null
    }

    suspend fun getUserByName(userName: UserName): List<FollowerResponse>? {
        val response = apiService.getUserByName(userName)
        if (response.isSuccessful) {
            return response.body()!!
        }
        return null
    }

    suspend fun getGlobalFile(key: KeyRecommend): List<FileEntry>? {
        val response = apiService.getGlobalFile(key)
        if (response.isSuccessful) {
            return response.body()!!
        }
        return null
    }

    suspend fun getFollowFile(user: UserEntity): List<FileEntry>? {
        val response = apiService.getFollowFile(user)
        if (response.isSuccessful) {
            return response.body()!!
        }
        return null
    }

    suspend fun getFollowUser(user: UserEntity): List<FollowerResponse>? {
        val response = apiService.getFollowUser(user)
        if (response.isSuccessful) {
            return response.body()!!
        }
        return null
    }

    suspend fun getNotifications(user: UserEntity, callback: (List<Notification>) -> Unit) {
        val response = apiService.getNotifications(user)
        if (response.isSuccessful) {
            callback(response.body()!!)
        }
    }

    suspend fun getSingleFile(file: FileEntity, callback: (FileEntry) -> Unit) {
        val response = apiService.getSingleFile(file)
        if (response.isSuccessful) {
            callback(response.body()!!)
        }
    }

    suspend fun followUser(
        data: RequestFollow,
        callback: (MsgResponse) -> Unit
    ) {
        val response = apiService.followUser(data)
        if (response.isSuccessful) {
            callback(response.body()!!)
        } else {
        }
    }

    suspend fun upload(
        context: Context,
        uri: Uri,
        description: String,
        fileName: String,
        id: String,
        fileId: String,
        callback: (MsgResponse) -> Unit
    ) {

        val inputStream = context.contentResolver.openInputStream(uri)
        val file = inputStream?.readBytes()

        val requestBody = file?.toRequestBody("application/pdf".toMediaTypeOrNull())

        val filePart = MultipartBody.Part.createFormData("file", "file_name.pdf", requestBody!!)
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val fileNamePart = fileName.toRequestBody("text/plain".toMediaTypeOrNull())
        val idPart = id.toRequestBody("text/plain".toMediaTypeOrNull())
        val idFilePart = fileId.toRequestBody("text/plain".toMediaTypeOrNull())

        val response = apiService.uploadFile(filePart, descriptionPart, fileNamePart, idPart, idFilePart)
        if (response.isSuccessful) {
            callback(response.body()!!)
        } else {

        }
    }

    suspend fun uploadImage(
        context: Context,
        uri: Uri,
        type: String,
        fileName: String,
        callback: (MsgResponse) -> Unit
    ) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = inputStream?.readBytes()

        val mimeType = context.contentResolver.getType(uri)
        val requestBody = file?.toRequestBody(mimeType?.toMediaTypeOrNull())

        val filePart = MultipartBody.Part.createFormData("file", fileName, requestBody!!)
        val fileNamePart = fileName.toRequestBody("text/plain".toMediaTypeOrNull())
        val fileTypePart = type.toRequestBody("text/plain".toMediaTypeOrNull())

        val response = apiService.uploadImage(filePart, fileNamePart, fileTypePart)
        if (response.isSuccessful) {
            callback(response.body()!!)
        } else {

        }
    }

    suspend fun download(file: FileEntity, callback: (ResponseBody) -> Unit) {
        val response = apiService.downloadFile(file)
        if (response.isSuccessful) {
            callback(response.body()!!)
        }
    }
}