package com.example.commyproject.data.model

class FileEntry(
    val _id: String,
    val idUser: String,
    val title: String,
    val fileName: String,
    val recognizeText: String,
    val summaryText: String,
    val state: Boolean,
    val isPublic: Boolean = false,
    val isTable: Boolean = false,
    val evaluation: List<Evaluation>? = emptyList(),
    val comments: List<Comment>? = emptyList()
) {
}