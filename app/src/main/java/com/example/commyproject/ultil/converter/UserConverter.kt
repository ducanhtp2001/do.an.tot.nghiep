package com.example.commyproject.ultil.converter

import com.example.commyproject.data.model.User

class UserConverter {
    companion object {
        fun str2User(userData: String): User? {
            if (userData.equals("")) return null
            else {
                val userArr = userData.split("_")
                return try {
                    User(_id = userArr[0], userName = userArr[1], passWord = userArr[2])
                } catch (e: Exception) {
                    null
                }
            }
        }

        fun str2ListFollow(str: String): List<String> {
            val strData = str.substring(1, str.length - 1)
            return strData.split(",").toList()
        }
    }
}