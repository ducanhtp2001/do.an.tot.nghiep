package com.example.commyproject.data.model

class Comment(
    val _id: String,
    val idUser: String,
    val avatar: String,
    val userName: String,
    val idFile: String,
    val toUserId: String? = null,
    val content: String,
    var likes: MutableList<Evaluation>? = mutableListOf()
)