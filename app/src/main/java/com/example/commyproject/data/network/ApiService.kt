package com.example.commyproject.data.network

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
import com.example.commyproject.data.model.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Streaming


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

    @POST("get-global-file")
    suspend fun getGlobalFile(@Body request: KeyRecommend): Response<List<FileEntry>>

    @POST("get-follow-file")
    suspend fun getFollowFile(@Body user: UserEntity): Response<List<FileEntry>>

    @POST("get-user-by-name")
    suspend fun getUserByName(@Body userName: UserName): Response<List<FollowerResponse>>

    @POST("get-follow-user")
    suspend fun getFollowUser(@Body user: UserEntity): Response<List<FollowerResponse>>

    @POST("get-notifications")
    suspend fun getNotifications(@Body user: UserEntity): Response<List<Notification>>

    @POST("get-single-file")
    suspend fun getSingleFile(@Body file: FileEntity): Response<FileEntry>

    @Multipart
    @POST("upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("fileName") name: RequestBody,
        @Part("id") id: RequestBody,
        @Part("fileId") fileId: RequestBody
    ): Response<MsgResponse>

    @Multipart
    @POST("upload-image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("fileName") name: RequestBody,
        @Part("fileType") type: RequestBody,
    ): Response<MsgResponse>

    @Streaming
    @POST("download")
    suspend fun downloadFile(@Body file: FileEntity): Response<ResponseBody>
}