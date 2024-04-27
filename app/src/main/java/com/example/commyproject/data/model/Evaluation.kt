package com.example.commyproject.data.model

class Evaluation(
    val _id: String,
    val idUser: String,
    val type: EvaluationEntityType,
    val idFile: String? = null,
    val idEntity: String? = null
) {
}

enum class EvaluationEntityType {
    FILE,
    COMMENT
}