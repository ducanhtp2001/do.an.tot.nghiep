package com.example.commyproject.activities.permission

import android.app.Activity
import android.app.VoiceInteractor.Request
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.commyproject.R
import com.example.commyproject.base.checkPermissionNotification
import com.example.commyproject.base.requestAppPermissionNotification
import com.example.commyproject.databinding.ActivityRequestPermissionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestPermissionAct : AppCompatActivity() {

    lateinit var b: ActivityRequestPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_permission)

        b = ActivityRequestPermissionBinding.inflate(layoutInflater)
        setContentView(b.root)

        initEvent()
    }

    private fun initEvent() {
        b.apply {

            btnNotification.setOnClickListener {
                val isAccess = this@RequestPermissionAct.checkPermissionNotification()
                if (isAccess) {

                } else {
                    this@RequestPermissionAct.requestAppPermissionNotification(RQC_NOTIFICATION)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    companion object {
        const val RQC_NOTIFICATION = 100
    }

}