package com.example.commyproject.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.commyproject.data.share.SharedPreferenceUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReceiverService: Service() {
    @Inject
    private lateinit var share: SharedPreferenceUtils
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }
}