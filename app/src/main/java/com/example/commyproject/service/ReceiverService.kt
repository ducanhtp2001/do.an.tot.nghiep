package com.example.commyproject.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.commyproject.R
import com.example.commyproject.activities.main.MainActivity
import com.example.commyproject.base.NetworkHelper
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.ultil.Constant
import com.taymay.taoday.service.SocketIOManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReceiverService: Service() {
    @Inject
    private lateinit var share: SharedPreferenceUtils
    @Inject
    private lateinit var socket: SocketIOManager
    @Inject
    private lateinit var networkHelper: NetworkHelper

    private lateinit var channel: NotificationChannel
    private lateinit var notificationManager: NotificationManager
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (networkHelper.isNetworkConnected()) {
            socket.onMsgReceiver { msg, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    startForeground(NOTIFICATION_ID, createNotification(getString(R.string.app_name), msg), ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
                }else{
                    startForeground(NOTIFICATION_ID, createNotification(getString(R.string.app_name), msg))
                }
                val broadcastIntent = Intent(Constant.BROADCAST_ACTION).apply {

                }
                sendBroadcast(broadcastIntent)
            }
        } else Log.e("testing", "network err")
        return START_STICKY
    }

    @SuppressLint("WrongConstant")
    private fun createNotification(title: String, content: String): Notification {
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                channel = NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.app_name),
                    NotificationManagerCompat.IMPORTANCE_HIGH
                ).apply {
                    setSound(null, null)
                    notificationManager.createNotificationChannel(this)
                }
            }
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_pdf)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(false)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)

        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.socketDisconnect()
        Log.e("testing", " ====== service disconnect ====== ")
    }

    companion object {
        const val CHANNEL_ID = "channel_name"
        const val NOTIFICATION_ID = 1
    }
}