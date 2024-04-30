package com.example.commyproject.data.model

class ProfileResponse(
    val user: UserResponse,
    val files: MutableList<FileEntry> = mutableListOf()
) {
}