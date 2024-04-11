package com.example.commyproject.ultil

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

const val TAG = "testing"
fun Context.log(context: Context, msg: String) {
    Log.d(TAG, msg)
}