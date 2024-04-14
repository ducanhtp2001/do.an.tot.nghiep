package com.taymay.taoday.service

import android.util.Log
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.DebounceUtils
import com.google.gson.Gson
import com.google.gson.JsonParser
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

    companion object {
        private const val TAG = "SocketIOManager"
        const val SERVER_URL = Config.SERVER_URL
        const val TRANSACTION_EVENT = "transaction_event"
        const val REQUEST_EXECUTE = "start_task"
        const val REQUEST_EXECUTE_DONE = "on_file_execute_done"
    }

    fun socketDisconnect() {
        socketOff()
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

        val user = share.getUser()
        val requestData = JSONObject()
        requestData.put("id", user._id)
        mSocket.emit("login", requestData)
    }

    private fun onLoginReceiver() {
        Log.d("testing", "register on")
        mSocket.on("on_login_receive") { args ->
            val data = args[0] as JSONObject
            val msg = data.getString("msg")

            Log.d("testing", msg)
        }
    }

    fun requestExecute() {
        onExecuteDone()
        Log.d("testing", "request execute")
        mSocket.emit(REQUEST_EXECUTE)
    }

    private fun onExecuteDone() {

        mSocket.on(REQUEST_EXECUTE_DONE) { args ->
            Log.d("testing", "on execute done")
            val data = args[0] as JSONObject
            val title = data.getString("fileTitle")

            Log.d("testing", "file $title is done")
        }
    }






    fun getFriendRequest(callback: (String) -> Unit) {
        mSocket.on("friend_request_received") { args ->
            val data = args[0] as JSONObject
            val senderId = data.getString("sender_id")
            callback(senderId)
        }
        mSocket.on("friend_request_block") { args ->
            val data = args[0] as JSONObject
            val senderId = data.getString("sender_id")
            callback(senderId)
        }
        mSocket.on("friend_request_sended") { args ->
            val data = args[0] as JSONObject
            val senderId = data.getString("sender_id")
            callback(senderId)

        }
        mSocket.on("friend_request_isfriend") { args ->
            val data = args[0] as JSONObject
            val senderId = data.getString("sender_id")
        }
    }

    fun blockFriend(senderId: String, receiverId: String) {
        val data = JSONObject()
        data.put("idRequest", senderId + "_" + receiverId)
        data.put("idSender", senderId)
        data.put("idReceiver", receiverId)
        mSocket.emit("block_friend", data)
    }


    fun unblockFriend(senderId: String, receiverId: String) {
        val data = JSONObject()
        data.put("idSender", senderId)
        data.put("idReceiver", receiverId)
        mSocket.emit("unblock_friend", data)
    }

    fun acceptFriendRequest(idSender: String, idReceiver: String) {
        val requestData = JSONObject()
//        requestData.put("idRequest", idSender + "_" + idReceiver)
        requestData.put("idSender", idSender)
        requestData.put("idReceiver", idReceiver)
        mSocket.emit("accept_friend", requestData)
    }

    fun denyFriendRequest(idSender: String, idReceiver: String) {
        val requestData = JSONObject()
        requestData.put("your_id", idSender)
        requestData.put("other_id", idReceiver)
        mSocket.emit("deny_request_friend", requestData)
    }

    fun handleFriendRequestDenied(callback: (String) -> Unit) {
        mSocket.on("friend_request_denied") { args ->
            val data = args[0] as JSONObject
            val senderId = data.getString("sender_id")

            callback(senderId)
//            elog(senderId)
        }
    }

    fun handleFriendRequestAccepted(callback: (String) -> Unit) {
        mSocket.on("friend_request_accepted") { args ->
            val data = args[0] as JSONObject
            val receiverId = data.getString("receiver_id")

            callback(receiverId)

//            elog("Yêu cầu kết bạn của bạn đã được chấp nhận bởi $receiverId.")
        }
    }


    fun sendLocationToFriends(roomIds: List<String>, senderId: String, location: JSONObject) {
        val data = JSONObject().apply {
            put("room_ids", roomIds)
            put("sender_id", senderId)
            put("location", location)
        }

        mSocket.emit("send_location_to_rooms", data)
    }

    fun getLocationFromRooms(roomIds: List<String>, userId: String) {
        val data = JSONObject().apply {
            put("room_ids", roomIds)
            put("user_id", userId)
        }
        mSocket.emit("get_location_from_rooms", data)
    }

    fun unFriend(yourId: String, friendId: String) {
        val data = JSONObject().apply {
            put("your_id", yourId)
            put("friend_id", friendId)
        }
        mSocket.emit("unfriend", data)
    }

    fun handleUnFriend(callback: (String) -> Unit) {
        mSocket.on("friend_left_room") { args ->
            val data = args[0] as JSONObject
            val userId = data.getString("user_id")
            callback(userId)
        }

    }

    fun senSOS(friendIds: List<String>, yourID: String) {
        val gson = Gson()
        val data = JSONObject().apply {
            put("your_id", gson.toJson(friendIds))
            put("friend_ids", JsonParser.parseString(yourID))
        }
        mSocket.emit("sos_alarm", data)
    }

    fun receivedFriendLocaton() {
        mSocket.on("received_location") { args ->
            val data = args[0] as JSONObject
            val senderId = data.getString("sender_id")
            val location = data.getJSONObject("location")

            println("Received realtime location from sender ID: $senderId")
            println("Location: $location")

        }
    }

    fun login(userId: String) {
        val requestData = JSONObject()
        requestData.put("idUser", userId)
        mSocket.emit("login", requestData)
    }
}


