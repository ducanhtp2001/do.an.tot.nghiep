package com.example.commyproject.data.model

import com.example.commyproject.ultil.converter.FileConverter

class CodeEntry(
    val _id: String,
    val idUser: String,
    val action: CODE_ACTION,
    val code: String
) {
    companion object {
        fun initCode(idUser: String, action: CODE_ACTION): CodeEntry {
            val _id = FileConverter.generateIdByUserId(userId = idUser)
            return CodeEntry(_id, idUser, action, "")
        }

        fun initCodeToVerify(idUser: String, code: String): CodeEntry {
            val _id = FileConverter.generateIdByUserId(userId = idUser)
            return CodeEntry(_id, idUser, CODE_ACTION.ACTION_VERIFY, code)
        }
    }
}

enum class CODE_ACTION {
    ACTION_GENERATE, ACTION_VERIFY
}