package com.taymay.taoday.service

import android.util.Log
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.DebounceUtils
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketIOManager @Inject constructor(
    private val mSocket: Socket,
    private val share: SharedPreferenceUtils
) {
    val user by lazy {
        share.getUser()
    }

    companion object {
        private const val TAG = "SocketIOManager"
        const val SERVER_URL = Config.SERVER_URL
        const val REQUEST_EXECUTE = "start_task"
        const val ON_EXECUTE_DONE = "on_file_execute_done"
        const val ON_MSG_RECEIVE = "on_msg_receive"
        const val REQUEST_CHECK_NOTIFY = "check_has_notify"
    }

    fun socketDisconnect() {
        logout()
        mSocket.disconnect()
        socketOff()
    }

    fun socketConnect() {
        if (!mSocket.connected()) {
            DebounceUtils.debounce100(object : DebounceUtils.DebounceCallback {
                override fun run() {
                    mSocket.on(Socket.EVENT_CONNECT, onConnect)
                    mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect)
                    mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
                    mSocket.connect()
                }
            })
        }
    }

    private fun socketOn() {
        login()
    }


    private fun socketOff() {
        mSocket.off(ON_MSG_RECEIVE)
        mSocket.off(ON_EXECUTE_DONE)
    }


    private val onConnect = Emitter.Listener {
        Log.e(
            TAG,
            "SocketManager  isConnected " + mSocket.connected() + " |  isActive  " + mSocket.isActive
        )
        socketOn()
    }


    private val onDisconnect = Emitter.Listener {
        Log.e(
            TAG,
            "SocketManager   Disconnected " + mSocket.connected() + " |  isActive  " + mSocket.isActive
        )
        socketOff()
    }


    private val onConnectError = Emitter.Listener { args: Array<Any> ->
        Log.e(TAG, "SocketManager Error connecting..." + args[0].toString())
        socketOff()
    }


    fun login() {
        onLoginReceiver()

        val requestData = JSONObject()
        requestData.put("id", user._id)
        mSocket.emit("login", requestData)
    }

    private fun checkHasNotify() {
        Log.d("testing", "check has notify")
        val requestData = JSONObject()
        requestData.put("id", user._id)
        mSocket.emit(REQUEST_CHECK_NOTIFY, requestData)
    }

    private fun logout() {
        val requestData = JSONObject()
        requestData.put("id", user._id)
        mSocket.emit("logout", requestData)
    }

    private fun onLoginReceiver() {
        Log.d("testing", "register on")
        mSocket.on("on_login_receive") { args ->
            val data = args[0] as JSONObject
            val msg = data.getString("msg")

            Log.d("testing", msg)
        }
    }

    fun onMsgReceiver(callback: (msg: String) -> Unit) {
        Log.d("testing", "register on")
        mSocket.on(ON_MSG_RECEIVE) { args ->
            val data =  args.getOrNull(0) as? JSONObject
            val _id = data?.getString("_id") ?: ""
            val msg = data?.getString("msg") ?: ""
            Log.d("testing", " ---------------- id: $_id get new msg: $msg")
//            val idFile = data?.optString("idFile") ?: ""

            // notify to server that client is receiver this msg
            val requestData = JSONObject()
            requestData.put("id", _id)
            requestData.put("idUser", user._id)
//            requestData.put("idFile", idFile)
            mSocket.emit(ON_MSG_RECEIVE, requestData)

            // handler this msg
            callback(msg)
//            Log.d("testing", " ---------------- get new msg: $msg")
        }

        // request server check this client has notify
        checkHasNotify()
    }

    fun requestExecute() {
        onExecuteDone()
        Log.d("testing", "request execute")
        mSocket.emit(REQUEST_EXECUTE)
    }

    private fun onExecuteDone() {

        mSocket.on(ON_EXECUTE_DONE) { args ->
            Log.d("testing", "on execute done")
            val data = args[0] as JSONObject
            val title = data.getString("fileTitle")

            Log.d("testing", "file $title is done")
        }
    }

}


