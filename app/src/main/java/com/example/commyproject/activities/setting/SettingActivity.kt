package com.example.commyproject.activities.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.R
import com.example.commyproject.activities.profile.ProfileAct
import com.example.commyproject.activities.user.login.LoginActivity
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.ActivitySettingBinding
import com.example.commyproject.service.ReceiverService
import com.example.commyproject.ultil.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private lateinit var b: ActivitySettingBinding
    private lateinit var viewModel: SettingViewModel
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(b.root)
        viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        initData()
        initEvent()
    }

    private fun initEvent() {
        b.apply {
            btnProfile.setOnClickListener {
                startActivity(Intent(this@SettingActivity, ProfileAct::class.java).apply {
                    putExtra(Constant.USER_ID, user._id)
                })
            }

            btnLogout.setOnClickListener {
                stopService(Intent(this@SettingActivity, ReceiverService::class.java))
                viewModel.clearData()
                startActivity(Intent(this@SettingActivity, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }
        }
    }

    private fun initData() {
        user = viewModel.getUserData()
    }

}