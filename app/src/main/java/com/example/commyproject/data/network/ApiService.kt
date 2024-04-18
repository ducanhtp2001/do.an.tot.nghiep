package com.example.commyproject.data.network

import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.MsgResponse
import com.example.commyproject.data.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiService {
    @POST("register")
    suspend fun register(@Body user: User): Response<User>

    @POST("login")
    suspend fun login(@Body user: User): Response<User>

    @POST("update-user")
    suspend fun updateUser(@Body user: User): Response<User>

    @POST("get-private-file")
    suspend fun getPrivateFile(@Body user: User): Response<List<FileEntry>>

    @POST("get-public-file")
    suspend fun getPublicFile(@Body user: User): Response<List<FileEntry>>

    @Multipart
    @POST("upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("fileName") name: RequestBody,
        @Part("id") id: RequestBody,
        @Part("fileId") fileId: RequestBody
    ): Response<MsgResponse>

}