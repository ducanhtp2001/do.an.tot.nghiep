package com.example.commyproject.ultil

object Constant {
    const val USER = "user"
    const val USER_ID = "userId"
    val KEY_RECOMMEND_LIST = listOf("Recommend", "Python", "Java", "C/C++", "Android", "Socket")
    val SPINNER_OPTION = listOf("Latest", "Most popular")

    fun getStringRecommend(position: Int): String? {
        return if (position == 0) null else KEY_RECOMMEND_LIST[position]
    }
}