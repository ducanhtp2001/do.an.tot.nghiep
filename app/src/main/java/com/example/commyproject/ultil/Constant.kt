package com.example.commyproject.ultil

object Constant {
    const val USER = "user"
    const val IS_LOGIN = "is_login"
    const val USER_ID = "userId"
    const val BROADCAST_ACTION = "my_broadcast_action"
    val KEY_RECOMMEND_LIST = listOf("Recommend", "Python", "Java", "C/C++", "Android", "Socket")
    val SPINNER_OPTION = listOf("Latest", "Most popular")

    const val ACTION_SEND_NOTIFY_STATE_NETWORK = "com.taymay.file.transfer.action.send.notify.state.network"
    const val DATA_STATE_NETWORK = "com.taymay.file.transfer.data.send.notify.state.network"

    const val FEEDBACK = "feedback"
    const val RATE = "rate"

    fun getStringRecommend(position: Int): String? {
        return if (position == 0) null else KEY_RECOMMEND_LIST[position]
    }
}