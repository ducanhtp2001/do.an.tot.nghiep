package com.example.commyproject.data.network

import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.model.FileEntity
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.MsgResponse
import com.example.commyproject.data.model.ProfileResponse
import com.example.commyproject.data.model.StatusResponse
import com.example.commyproject.data.model.User
import com.example.commyproject.data.model.UserEntity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Streaming
import retrofit2.http.Url


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

    @POST("post-comment")
    suspend fun postComment(@Body commentEntity: CommentEntity): Response<Comment>

    @POST("delete-file")
    suspend fun deleteFile(@Body file: FileEntity): Response<StatusResponse>

    @POST("change-state")
    suspend fun changeState(@Body file: FileEntity): Response<StatusResponse>

    @POST("post-like")
    suspend fun postLike(@Body evaluationEntity: EvaluationEntity): Response<Evaluation>

    @POST("get-profile")
    suspend fun getProfile(@Body user: UserEntity): Response<ProfileResponse>

    @Multipart
    @POST("upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("fileName") name: RequestBody,
        @Part("id") id: RequestBody,
        @Part("fileId") fileId: RequestBody
    ): Response<MsgResponse>

    @Streaming
    @POST("download")
    suspend fun downloadFile(@Body file: FileEntity): Response<ResponseBody>
}