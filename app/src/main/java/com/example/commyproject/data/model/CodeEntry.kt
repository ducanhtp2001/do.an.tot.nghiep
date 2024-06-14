package com.example.commyproject.data.model

import com.example.commyproject.ultil.converter.FileConverter

class CodeEntry(
    val _id: String,
    val idUser: String,
    val action: CODE_ACTION,
    val userName: String,
    val code: String
) {
    companion object {
        fun initCode(idUser: String, action: CODE_ACTION): CodeEntry {
            val _id = FileConverter.generateIdByUserId(userId = idUser)
            return CodeEntry(_id, idUser, action, "", "")
        }

        fun initCodeToVerify(idUser: String, code: String): CodeEntry {
            val _id = FileConverter.generateIdByUserId(userId = idUser)
            return CodeEntry(_id, idUser, CODE_ACTION.ACTION_VERIFY, "", code)
        }

        fun initCodeToReset(userName: String, code: String): CodeEntry {
            return CodeEntry("", "", CODE_ACTION.ACTION_RESET, userName, code)
        }
    }
}

enum class CODE_ACTION {
    ACTION_RESET, ACTION_VERIFY
}