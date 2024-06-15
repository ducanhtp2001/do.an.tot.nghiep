package com.example.commyproject.activities.user.fogetpass

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.commyproject.R
import com.example.commyproject.activities.bottomsheetdialog.ACTION_CODE
import com.example.commyproject.activities.bottomsheetdialog.showInputCodeToVerify
import com.example.commyproject.activities.user.UserActionViewModel
import com.example.commyproject.activities.user.login.LoginActivity
import com.example.commyproject.base.BaseActivity
import com.example.commyproject.databinding.ActivityChangePasswordBinding
import com.example.commyproject.databinding.ActivityFogetPasswordBinding
import com.example.commyproject.ultil.constraint.PasswordConstraint
import com.example.commyproject.ultil.constraint.UserNameConstraint
import com.example.commyproject.ultil.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FogetPasswordActivity : BaseActivity() {
    val viewModel: UserActionViewModel by viewModels()
    lateinit var b: ActivityFogetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFogetPasswordBinding.inflate(layoutInflater)
        setContentView(b.root)

        initEvent()
    }

    fun loading() {
        b.apply {
            btnReset.visibility = View.GONE
            loading.visibility = View.VISIBLE
        }
    }

    fun loadDone() {
        b.apply {
            btnReset.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }
    }

    private fun initEvent() {
        b.apply {
            btnCancel.setOnClickListener { onBackPressed() }

            btnReset.setOnClickListener {
                loading()
                val userName = edtUserName.text.toString()
                val newPass = edtPass.text.toString()
                val rePass = edtRetypePass.text.toString()
                if (userName.isEmpty() || !UserNameConstraint.checkUserNameFormat(userName)) {
                    showToast("UserName format is incorrect")
                } else if (
                    !PasswordConstraint.checkPassFormat(newPass)
                ) {
                    showToast("New password format is incorrect")
                } else if (newPass != rePass) {
                    showToast("Retype password is incorrect")
                } else {

                    viewModel.forgetPass(userName, newPass) { msg ->
                        runOnUiThread {
                            loadDone()
                            showToast(msg.msg)
                            if (msg.isSuccess) {
                                runOnUiThread {
                                    showInputCodeToVerify(ACTION_CODE.PASS, userName) {
                                        runOnUiThread { showToast(it.msg)
                                            if (it.isSuccess) {
                                                startActivity(
                                                    Intent(
                                                        this@FogetPasswordActivity,
                                                        LoginActivity::class.java)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}