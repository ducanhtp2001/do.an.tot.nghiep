package com.example.commyproject.data.model

class Comment(
    val _id: String,
    val idUser: String,
    val avatar: String,
    val userName: String,
    val idFile: String,
    val idComment: String? = null,
    val type: EvaluationEntityType,
    val comment: String,
    val votes: List<Evaluation>? = null,
    val replies: List<Comment>? = null
) {
}