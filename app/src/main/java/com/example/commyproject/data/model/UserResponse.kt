package com.example.commyproject.data.model

open class UserResponse(
    open val _id: String = "",
    open val userName: String = "",
    val follows: List<FollowerResponse> = emptyList(), // minh theo doi
    val followers: List<FollowerResponse> = emptyList(), // nguoi theo doi mimh ^^
    open val avatar: String = ""
) {
}