package com.example.commyproject.data.model

class Comment(
    val _id: String,
    val idUser: String,
    val avatar: String,
    val userName: String,
    val idEntity: String,
    val type: EvaluationEntityType,
    val comment: String,
    val votes: List<Evaluation>,
    val replies: List<Comment>
) {
}