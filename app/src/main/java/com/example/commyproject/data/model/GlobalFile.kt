package com.example.commyproject.data.model

class GlobalFile(
    override val _id: String,
    override val idUser: String,
    override val title: String,
    override val fileName: String,
    val userName: String,
    val avatar: String,
    override val recognizeText: String,
    override val summaryText: String,
    override val state: Boolean,
    override val isPublic: Boolean = false,
    override val isTable: Boolean = false,
    override val likes: MutableList<Evaluation> = mutableListOf(),
    override val comments: MutableList<Comment> = mutableListOf()
): FileEntry(_id, idUser, title, fileName, recognizeText, summaryText, state, isPublic, isTable, likes, comments) {
}