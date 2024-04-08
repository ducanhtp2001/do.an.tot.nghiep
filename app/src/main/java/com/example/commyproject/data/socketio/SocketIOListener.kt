package com.taymay.taoday.service

import android.util.Log
import com.example.commyproject.data.share.SharedPreferenceUtils
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class SocketIOListener @Inject constructor(
    private val sharedPreferenceUtils: SharedPreferenceUtils
) {

    companion object {
        private const val TAG = "SocketListeners"
    }

    var onTransactionsListening =
        Emitter.Listener { args: Array<Any> ->
            try {
                val messageJson = JSONObject(args[0].toString())
                Log.d(
                    TAG,
                    "SocketHelper setListening: json----   $messageJson"
                )
            } catch (e: JSONException) {
                Log.d(
                    TAG,
                    "SocketHelper  call: error " + e.message
                )
                e.printStackTrace()
            }
        }
}