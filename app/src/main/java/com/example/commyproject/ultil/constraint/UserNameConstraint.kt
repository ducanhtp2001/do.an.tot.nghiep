package com.example.commyproject.ultil.constraint

import android.text.TextUtils

class UserNameConstraint {
    companion object {
        fun checkUserNameFormat(userName: String): Boolean {
            val regex = Regex("^[A-Za-z][A-Za-z0-9]{5,19}$")
            return regex.matches(userName)
        }

        fun checkGmailFormat(gmail: String): Boolean {
            return !TextUtils.isEmpty(gmail) && android.util.Patterns.EMAIL_ADDRESS.matcher(gmail).matches()
        }
    }
}