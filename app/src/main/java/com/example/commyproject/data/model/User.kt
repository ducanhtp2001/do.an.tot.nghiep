package com.example.commyproject.data.model

class User(
    val id: String = "",
    val userName: String = "",
    val passWord: String = "",
    val email: String = "",
    val follow: List<String> = emptyList()
    ) {

    override fun toString(): String {
        return "User(id:$id, userName:$userName, passWord:$passWord, email:$email, follow: $follow)"
    }

    companion object {
        fun userInit(name: String, passWord: String): User {
            return User(
                id = System.currentTimeMillis().toString(),
                userName = name,
                passWord = passWord,
                email = "",
                follow = emptyList()
            )
        }
    }
}