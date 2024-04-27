package com.example.commyproject.data.model

class CommentEntity(
    val _id: String,
    var idUser: String?,
    val toUserId: String?, // id cua nguoi duoc phan hoi
    val idFile: String?,
    val content: String
)