package com.example.commyproject.data.share

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.commyproject.data.model.User
import com.example.commyproject.ultil.Constant
import com.example.commyproject.ultil.converter.UserConverter
import javax.inject.Singleton


@Singleton
class SharedPreferenceUtils(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(MYAPPLICATION, Context.MODE_PRIVATE)

    fun putUser(user: User) {
        val userData = user.toStringSave()
        val editor = sharedPreferences.edit()
        Log.d("testing", "save to cache: $userData")
        editor.putString(Constant.USER, userData).apply()
    }

    fun login(user: User) {
        putUser(user)
        putBoolean(Constant.IS_LOGIN, true)
    }

    fun getUser(): User {
        try {
            val userData = sharedPreferences.getString(Constant.USER, "")!!
            val dataArr = userData.split("-")
//        $_id-$userName-$passWord-$email-$follow-$avatar
            return User(
                _id = dataArr[0],
                userName = dataArr[1],
                passWord = dataArr[2],
                email = dataArr[3],
                follow = UserConverter.str2ListFollow(dataArr[4]),
                avatar = dataArr[5],
            )
        } catch (e: Exception) {
            return User()
        }
    }

    fun checkLogin(): Boolean {
        return getBooleanValue(Constant.IS_LOGIN, false)
    }

    fun logout() {
        val editor = sharedPreferences.edit()
        editor.remove(Constant.USER).apply()
        putBoolean(Constant.IS_LOGIN, false)
    }

    fun putStringValue(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value).apply()
    }

    fun getStringValue(key: String, def: String): String {
        return sharedPreferences.getString(key, def)!!
    }

    fun putNumber(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value).apply()
    }

    fun putLong(key: String, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value).apply()
    }

    fun getLong(key: String, def: Long): Long {
        return sharedPreferences.getLong(key, def)
    }

    fun getNumber(key: String, def: Int): Int {
        return sharedPreferences.getInt(key, def)
    }

    fun getBooleanValue(key: String, def: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, def)
    }

    fun putBooleanValue(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value).apply()
    }


    companion object {
        const val MYAPPLICATION = "MY_APPLICATION"
    }


}