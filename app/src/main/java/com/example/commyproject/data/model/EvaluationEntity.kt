package com.example.commyproject.data.model

class EvaluationEntity(
    val _id: String,
    val idUser: String,
    val idFile: String? = null,
    val idComment: String? = null,
    val type: EvaluationEntityType,
)

enum class EvaluationEntityType {
    FILE,
    COMMENT
}