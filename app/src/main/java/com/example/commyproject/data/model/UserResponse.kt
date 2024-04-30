package com.example.commyproject.data.model

class UserResponse(
    val _id: String = "",
    val userName: String = "",
    val follows: List<FollowerResponse> = emptyList(), // minh theo doi
    val followers: List<FollowerResponse> = emptyList(), // nguoi theo doi mimh ^^
    val avatar: String = ""
) {
}