package com.example.commyproject.ultil.constraint

class UserNameConstraint {
    companion object {
        fun checkUserNameFormat(userName: String): Boolean {
            val regex = Regex("^[A-Za-z][A-Za-z0-9]{5,19}$")
            return regex.matches(userName)
        }
    }
}