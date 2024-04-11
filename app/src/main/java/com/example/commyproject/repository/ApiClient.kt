package com.example.commyproject.repository

import android.content.Context
import android.net.Uri
import com.example.commyproject.data.model.MsgResponse
import com.example.commyproject.data.model.User
import com.example.commyproject.data.network.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    suspend fun upload(
        context: Context,
        uri: Uri,
        description: String,
        fileName: String,
        id: String,
        callback: (MsgResponse) -> Unit
    ) {

        val inputStream = context.contentResolver.openInputStream(uri)
        val file = inputStream?.readBytes()

        val requestBody = file?.toRequestBody("application/pdf".toMediaTypeOrNull())

        val filePart = MultipartBody.Part.createFormData("file", "file_name.pdf", requestBody!!)
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val fileNamePart = fileName.toRequestBody("text/plain".toMediaTypeOrNull())
        val idPart = id.toRequestBody("text/plain".toMediaTypeOrNull())

        val response = apiService.uploadFile(filePart, descriptionPart, fileNamePart, idPart)
        if (response.isSuccessful) {
            callback(response.body()!!)
        } else {

        }
    }
}