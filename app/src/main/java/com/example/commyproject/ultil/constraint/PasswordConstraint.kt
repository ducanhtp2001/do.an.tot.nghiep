package com.example.commyproject.ultil.constraint

class PasswordConstraint {
    companion object {
        fun checkPassFormat(pass: String): Boolean {
            // check pass format
            val regex = Regex("^[A-Z][A-Za-z0-9]{5,11}$")

            return regex.matches(pass)
        }

        fun checkCodeFormat(code: String): Boolean {
            val regex = Regex("^[0-9][0-9]{3}$")
            return regex.matches(code)
        }
    }
}

fun main() {
    println(PasswordConstraint.checkPassFormat("Afoaisjdrofj"))
}