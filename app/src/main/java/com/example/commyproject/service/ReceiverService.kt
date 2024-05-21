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
import android.database.Observable
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import com.example.commyproject.R
import com.example.commyproject.activities.main.MainActivity
import com.example.commyproject.base.NetworkHelper
import com.example.commyproject.base.NetworkLiveData
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.ultil.Constant
import com.taymay.taoday.service.SocketIOManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReceiverService: Service() {
    @Inject
    lateinit var share: SharedPreferenceUtils
    @Inject
    lateinit var socket: SocketIOManager
    @Inject
    lateinit var networkHelper: NetworkHelper
    @Inject
    lateinit var networkLiveData: NetworkLiveData

    private var observer: Observer<Boolean> = Observer {
        if (!it) stopSelf()
    }

    private lateinit var channel: NotificationChannel
    private lateinit var notificationManager: NotificationManager
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (networkHelper.isNetworkConnected()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                startForeground(NOTIFICATION_ID, createNotification(getString(R.string.app_name), "", true), ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING)
            }else{
                startForeground(NOTIFICATION_ID, createNotification(getString(R.string.app_name), "", true))
            }

            socket.socketConnect()

            socket.onMsgReceiver { msg ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    startForeground(NOTIFICATION_ID, createNotification(getString(R.string.app_name), msg, false), ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING)
                }else{
                    startForeground(NOTIFICATION_ID, createNotification(getString(R.string.app_name), msg, false))
                }
                val broadcastIntent = Intent(Constant.BROADCAST_ACTION).apply {

                }
                sendBroadcast(broadcastIntent)
            }
        } else {
            stopSelf()
            Log.e("testing", "network err")
        }

        networkLiveData.observeForever(observer)

        return START_STICKY
    }

    @SuppressLint("WrongConstant")
    private fun createNotification(title: String, content: String, isHide: Boolean): Notification {
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
            .setContentIntent(pendingIntent)
            .setVisibility(if (isHide) NotificationCompat.VISIBILITY_SECRET else NotificationCompat.VISIBILITY_PUBLIC)

        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkLiveData.removeObserver(observer)
        socket.socketDisconnect()
        Log.e("testing", " ====== service die ====== ")
    }

    companion object {
        const val CHANNEL_ID = "channel_name"
        const val NOTIFICATION_ID = 1
    }
}