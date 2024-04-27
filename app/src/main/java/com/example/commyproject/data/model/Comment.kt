package com.example.commyproject.data.model

class Comment(
    val _id: String,
    val idUser: String,
    val avatar: String,
    val userName: String,
    val idFile: String,
    val toId: String? = null,
    val type: EvaluationEntityType,
    val content: String,
    val like: Int = 0
) {
}