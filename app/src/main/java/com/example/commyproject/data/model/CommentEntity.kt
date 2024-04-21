package com.example.commyproject.data.model

class CommentEntity(
    val _id: String,
    val userId: String?,
    val type: EvaluationEntityType,
    val comment: String,
) {
}