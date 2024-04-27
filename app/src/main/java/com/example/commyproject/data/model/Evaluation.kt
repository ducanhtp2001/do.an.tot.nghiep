package com.example.commyproject.data.model

class Evaluation(
    val _id: String,
    val idUser: String,
    val avatar: String,
    val userName: String,
    val idFile: String? = null,
    val idComment: String? = null,
    val type: EvaluationEntityType,
)