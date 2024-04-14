package com.example.commyproject.data.model

class Comment(
    val _id: String,
    val idUser: String,
    val idFile: String,
    val content: String,
    val votes: List<Evaluation>
) {
}