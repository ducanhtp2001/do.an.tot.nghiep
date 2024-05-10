package com.example.commyproject.ultil.converter

class FileConverter {
    companion object {
        fun getFileName(id: String, currentTime: String): String {
            return "${id}_$currentTime"
        }

        fun generateIdByUserId(userId: String): String {
            return "${System.currentTimeMillis()}_$userId"
        }

        fun getTimePassFromId(_id: String): String {
            val arr = _id.split("_")
            val fileTimeMillis = arr[0].toLong()
            val currentTimeMillis = System.currentTimeMillis()

            val diffMillis = currentTimeMillis - fileTimeMillis

            val seconds = diffMillis / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val months = days / 30
            val years = days / 365

            return when {
                years > 0 -> "$years years ago"
                months > 0 -> "$months months ago"
                days > 0 -> "$days days ago"
                hours > 0 -> "$hours hours ago"
                minutes > 0 -> "$minutes minutes ago"
                else -> "$seconds seconds ago"
            }
        }

        fun getTimePassFromString(time: String): String {
            val diffMillis = time.toLong()

            val seconds = diffMillis / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val months = days / 30
            val years = days / 365

            return when {
                years > 0 -> "$years years ago"
                months > 0 -> "$months months ago"
                days > 0 -> "$days days ago"
                hours > 0 -> "$hours hours ago"
                minutes > 0 -> "$minutes minutes ago"
                else -> "$seconds seconds ago"
            }
        }
    }
}