package com.example.commyproject.activities.permission

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.commyproject.R
import com.example.commyproject.activities.user.login.LoginActivity
import com.example.commyproject.base.checkPermissionFile
import com.example.commyproject.base.checkPermissionNotification
import com.example.commyproject.base.requestAppPermissionNotification
import com.example.commyproject.base.showPermissionSettingsDialog
import com.example.commyproject.databinding.ActivityRequestPermissionBinding
import com.example.commyproject.ultil.loge
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
                    btnNotification.setImageResource(R.drawable.ic_ok)
                    btnNotification.setBackgroundColor(Color.parseColor("#30d8a0"))
                } else {
                    this@RequestPermissionAct.requestAppPermissionNotification(RQ_NOTIFICATION)
                }

                if (checkPermission()) goMain()
            }
            btnStorage.setOnClickListener {
                val isNotAccess = this@RequestPermissionAct.checkPermissionFile()
                loge("is isNotAccess $isNotAccess")
                if (!isNotAccess) {
                    btnNotification.setImageResource(R.drawable.ic_ok)
                    btnNotification.setBackgroundColor(Color.parseColor("#30d8a0"))
                } else {
                    this@RequestPermissionAct.showPermissionSettingsDialog()
                }

                if (checkPermission()) goMain()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission()) goMain()
    }

    private fun goMain() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    fun checkPermission(): Boolean {
        return !this@RequestPermissionAct.checkPermissionFile() &&
                this@RequestPermissionAct.checkPermissionNotification()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    companion object {
        const val RQ_NOTIFICATION = 100
        const val RQ_MANAGE_STORAGE = 101
    }

}