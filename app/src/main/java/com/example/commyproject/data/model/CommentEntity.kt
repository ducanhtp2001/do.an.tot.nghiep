package com.example.commyproject.data.model

class CommentEntity(
    val _id: String,
    var userId: String?,
    val fileId: String?,
    val commentId: String?,
    val type: EvaluationEntityType,
    val comment: String
)