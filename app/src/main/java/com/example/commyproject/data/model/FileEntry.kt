package com.example.commyproject.data.model

open class FileEntry(
    open val _id: String,
    open val idUser: String,
    open val title: String,
    open val fileName: String,
    open val recognizeText: String,
    open val summaryText: String,
    open val state: Boolean,
    open val isPublic: Boolean = false,
    open val isTable: Boolean = false,
    open val likes: MutableList<Evaluation> = mutableListOf(),
    open val comments: MutableList<Comment> = mutableListOf()
) {
}