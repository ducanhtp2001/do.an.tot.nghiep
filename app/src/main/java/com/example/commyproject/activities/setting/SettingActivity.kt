package com.example.commyproject.activities.setting

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.R
import com.example.commyproject.activities.profile.ProfileAct
import com.example.commyproject.activities.user.login.LoginActivity
import com.example.commyproject.data.model.User
import com.example.commyproject.data.share.SharedPreferenceUtils
import com.example.commyproject.databinding.ActivitySettingBinding
import com.example.commyproject.databinding.DialogFeedbackBinding
import com.example.commyproject.databinding.DialogRateBinding
import com.example.commyproject.service.ReceiverService
import com.example.commyproject.ultil.Constant
import com.example.commyproject.ultil.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {
    @Inject
    lateinit var share: SharedPreferenceUtils

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

            btnBack.setOnClickListener {
                onBackPressed()
            }

            btnFeedback.setOnClickListener {
                val dialog = Dialog(this@SettingActivity)
                val binding = DialogFeedbackBinding.inflate(layoutInflater)
                dialog.setCancelable(false)

                binding.apply {
                    btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    btnSend.setOnClickListener {
                        if (inputFeedback.text.isNotEmpty()) {
                            val feedback = inputFeedback.text.toString()
                            share.putStringValue(Constant.FEEDBACK, feedback)
                            dialog.dismiss()
                            showToast("Thanks for your Feedback")
                        }
                    }
                }

                dialog.setContentView(binding.root)
                dialog.show()
            }

            btnRate.setOnClickListener {
                val dialog = Dialog(this@SettingActivity)
                val binding = DialogRateBinding.inflate(layoutInflater)
                dialog.setCancelable(false)
                var list: List<ImageView> = listOf()
                var position = 0
                val draw: (Int) -> Unit = {
                    for (item in list) {
                        if (list.indexOf(item) <= position) item.setImageResource(R.drawable.ic_star_rate)
                        else item.setImageResource(R.drawable.ic_star)
                    }
                }
                val onClick: (Int) -> Unit = { id ->
                    for (item in list) {
                        if (id == item.id) {
                            Log.d("testing", "id = ${it.id}, id = ${item.id}")
                            position = list.indexOf(item)
                            break
                        }
                    }
                    Log.d("testing", "pos = $position")
                    draw(position)
                }
                binding.apply {
                    list = listOf(start1, start2, start3, start4, start5)
                    start1.setOnClickListener { onClick(start1.id) }
                    start2.setOnClickListener { onClick(start2.id) }
                    start3.setOnClickListener { onClick(start3.id) }
                    start4.setOnClickListener { onClick(start4.id) }
                    start5.setOnClickListener { onClick(start5.id) }

                    val oldPos = share.getNumber(Constant.RATE, -1)
                    if (oldPos != -1) {
                        draw(oldPos)
                    }

                    btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    btnSend.setOnClickListener {
                        share.putNumber(Constant.RATE, position)
                        dialog.dismiss()
                        showToast("Thanks for your Ratting")
                    }
                }

                dialog.setContentView(binding.root)
                dialog.show()
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