package com.example.commyproject.ultil.converter

class FileConverter {
    companion object {
        fun getFileName(id: String, currentTime: String): String {
            return "${id}_$currentTime"
        }
    }
}