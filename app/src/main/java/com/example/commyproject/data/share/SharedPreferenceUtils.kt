package com.example.commyproject.data.share

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Singleton


@Singleton
class SharedPreferenceUtils(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(MYAPPLICATION, Context.MODE_PRIVATE)

    fun putStringValue(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value).apply()
    }

    fun getStringValue(key: String, def : String): String {
        return sharedPreferences.getString(key, def)!!
    }

    fun putNumber(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value).apply()
    }

    fun putLong(key: String, value : Long){
        val editor = sharedPreferences.edit()
        editor.putLong(key, value).apply()
    }

    fun getLong(key: String, def: Long): Long {
        return sharedPreferences.getLong(key, def)
    }

    fun getNumber(key: String, def: Int): Int {
        return sharedPreferences.getInt(key, def)
    }

    fun getBooleanValue(key: String, def : Boolean): Boolean {
        return sharedPreferences.getBoolean(key, def)
    }

    fun putBooleanValue(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value).apply()
    }


    companion object {
        const val ANGLE_TYPE = "angle_type"
        const val ID_THEME = "id_theme"
        const val MYAPPLICATION = "MY_APPLICATION"
        const val NUMBER_SEPARATOR = "number_separator"
        const val ANSWER_PRECISION = "answer_precision"
        const val AUTO_DELETE_HISTORY = "auto_delete_history"
        const val MILLI_SECONDS_IN_DAYS = 86_400_000L
        const val SAVE_EXPRESSION = "save_expression"

        const val TIME_DELETE_HISTORY = "time_delete_history"
    }


}