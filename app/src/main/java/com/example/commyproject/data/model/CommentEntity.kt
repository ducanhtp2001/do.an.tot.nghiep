package com.example.commyproject.data.model

class CommentEntity(
    val _id: String,
    var userId: String?,
    val toId: String?, // id cua nguoi duoc phan hoi
    val fileId: String?,
    val content: String
)