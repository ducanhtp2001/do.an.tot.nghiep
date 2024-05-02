package com.example.commyproject.data.model.networkresponse

import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.UserResponse

class ProfileResponse(
    val user: UserResponse,
    val files: MutableList<FileEntry> = mutableListOf()
)