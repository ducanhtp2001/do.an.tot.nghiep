package com.example.commyproject.data.model

class CommentEntity(
    val _id: String,
    var userId: String?,
    val type: EvaluationEntityType,
    val comment: String,
) {
}