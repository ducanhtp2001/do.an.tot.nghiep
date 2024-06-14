package com.example.commyproject.data.model

open class FileEntry(
    val _id: String,
    val idUser: String,
    val userName: String? = null,
    val avatar: String? = null,
    val title: String,
    val fileName: String,
    val gmail: String? = "",
    val recognizeText: String,
    val summaryText: String,
    val state: Boolean,
    val isPublic: Boolean = false,
    val isTable: Boolean = false,
    val likes: MutableList<Evaluation> = mutableListOf(),
    val comments: MutableList<Comment> = mutableListOf(),
    val followers: MutableList<UserEntity> = mutableListOf()
) {
    override fun toString(): String {
        return "FileEntry(_id='$_id', idUser='$idUser', userName=$userName, avatar=$avatar, " +
                "title='$title', fileName='$fileName', gmail=$gmail, recognizeText='$recognizeText', " +
                "summaryText='$summaryText', state=$state, isPublic=$isPublic, isTable=$isTable, " +
                "likes=$likes, comments=$comments, followers=$followers)"
    }
}