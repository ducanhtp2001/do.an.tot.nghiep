package com.example.commyproject.base

import android.content.BroadcastReceiver
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.commyproject.ultil.registerNetworkBroadCaseReceiver
import com.example.commyproject.ultil.showNotificationNetworkDialog

abstract class BaseActivity: AppCompatActivity() {
    val listReceiver: MutableList<BroadcastReceiver> = mutableListOf()
    val baseViewModel: BaseViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listReceiver.apply {
            add(registerNetworkBroadCaseReceiver())
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        listReceiver.forEach {
            unregisterReceiver(it)
        }
    }
}