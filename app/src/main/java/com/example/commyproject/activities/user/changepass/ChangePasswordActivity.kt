package com.example.commyproject.activities.user.changepass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.commyproject.R
import com.example.commyproject.activities.user.UserActionViewModel
import com.example.commyproject.activities.user.login.LoginActivity
import com.example.commyproject.base.BaseActivity
import com.example.commyproject.databinding.ActivityChangePasswordBinding
import com.example.commyproject.ultil.constraint.PasswordConstraint
import com.example.commyproject.ultil.loge
import com.example.commyproject.ultil.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity() {

    val viewModel: UserActionViewModel by viewModels()
    lateinit var b: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(b.root)

        initView()
        initEvent()
    }

    private fun initEvent() {
        b.apply {
            btnBack.setOnClickListener {
                onBackPressed()
            }
            btnChangePass.setOnClickListener {
                val oldP = edtOldPass.text.toString()
                val newP = edtPass.text.toString()
                val reP = edtRetypePass.text.toString()

                if (oldP.isEmpty() || newP.isEmpty() || reP.isEmpty()) {
                    showToast("Please fill all")
                } else {
                    loge("old = $oldP, p = ${viewModel.user.passWord} and is true: ${PasswordConstraint.checkPassFormat(oldP)}")
                    if (
                        !PasswordConstraint.checkPassFormat(oldP) ||
                        oldP != viewModel.user.passWord
                    ) {
                        showToast("Old password is incorrect")
                    }else if (oldP == newP) {
                        showToast("New password is like old password")
                    } else if (!PasswordConstraint.checkPassFormat(newP)) {
                        showToast("New password format is incorrect")
                    } else if (newP != reP) {
                        showToast("Retype password is incorrect")
                    } else {
                        viewModel.changePass(newP) {
                            runOnUiThread {
                                showToast(it.msg)
                                if (it.isSuccess) {
                                    viewModel.logout()
                                    startActivity(Intent(this@ChangePasswordActivity, LoginActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initView() {

    }
}