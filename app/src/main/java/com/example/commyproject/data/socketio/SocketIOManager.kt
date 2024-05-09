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
    private val listener: SocketIOListener,
    private val share: SharedPreferenceUtils
) {
    val user by lazy {
        share.getUser()
    }

    companion object {
        private const val TAG = "SocketIOManager"
        const val SERVER_URL = Config.SERVER_URL
        const val TRANSACTION_EVENT = "transaction_event"
        const val REQUEST_EXECUTE = "start_task"
        const val ON_EXECUTE_DONE = "on_file_execute_done"
    }

    fun socketDisconnect() {
        socketOff()
        logout()
        mSocket.disconnect()
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
        socketOff()
        mSocket.on(TRANSACTION_EVENT, listener.onTransactionsListening)
    }


    private fun socketOff() {
        mSocket.off(TRANSACTION_EVENT)
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

    private fun logout() {
        onLoginReceiver()

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
        mSocket.on("on_msg_receive") { args ->
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
            mSocket.emit("on_msg_receive", requestData)

            // handler this msg
            callback(msg)
//            Log.d("testing", " ---------------- get new msg: $msg")
        }
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






//    fun getFriendRequest(callback: (String) -> Unit) {
//        mSocket.on("friend_request_received") { args ->
//            val data = args[0] as JSONObject
//            val senderId = data.getString("sender_id")
//            callback(senderId)
//        }
//        mSocket.on("friend_request_block") { args ->
//            val data = args[0] as JSONObject
//            val senderId = data.getString("sender_id")
//            callback(senderId)
//        }
//        mSocket.on("friend_request_sended") { args ->
//            val data = args[0] as JSONObject
//            val senderId = data.getString("sender_id")
//            callback(senderId)
//
//        }
//        mSocket.on("friend_request_isfriend") { args ->
//            val data = args[0] as JSONObject
//            val senderId = data.getString("sender_id")
//        }
//    }
//
//    fun blockFriend(senderId: String, receiverId: String) {
//        val data = JSONObject()
//        data.put("idRequest", senderId + "_" + receiverId)
//        data.put("idSender", senderId)
//        data.put("idReceiver", receiverId)
//        mSocket.emit("block_friend", data)
//    }

}


