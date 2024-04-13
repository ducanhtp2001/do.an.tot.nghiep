package com.example.commyproject.data.model

class User(
    val _id: String = "",
    val userName: String = "",
    val passWord: String = "",
    val email: String = "",
    val follow: List<String> = emptyList(),
    val avatar: String = ""
    ) {

    override fun toString(): String {
        return "User(id:$_id, userName:$userName, passWord:$passWord, email:$email, follow: $follow, avatar: $avatar)"
    }

    fun toStringSave(): String {
        return "$_id-$userName-$passWord-$email-$follow-$avatar"
    }

    companion object {
        fun userInit(name: String, passWord: String): User {
            return User(
                _id = System.currentTimeMillis().toString(),
                userName = name,
                passWord = passWord,
                email = "",
                follow = emptyList(),
                avatar = ""
            )
        }
    }
}