package com.example.commyproject.activities.user.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.R
import com.example.commyproject.activities.user.login.LoginActivity
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.ActivityRegisterBinding
import com.example.commyproject.ultil.constaint.PasswordConstraint
import com.example.commyproject.ultil.constaint.UserNameConstraint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var b: ActivityRegisterBinding
    private lateinit var viewModel: RegisterActViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        b = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(b.root)
        viewModel = ViewModelProvider(this)[RegisterActViewModel::class.java]

        initEvent()
        initObserver()

    }

    private fun initObserver() {
        viewModel.stateLoading.observe(this) {
            if (it) {
                loading()
            } else {
                loadDone()
            }
        }

        viewModel.stateRegister.observe(this) {
            if (it) startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun loading() {
        b.apply {
            btnRegister.isEnabled = false
            btnBackLogin.isEnabled = false
            progressBar.visibility = View.VISIBLE
        }
        Log.d("register", "loading")
    }
    private fun loadDone() {
        b.apply {
            btnRegister.isEnabled = true
            btnBackLogin.isEnabled = true
            progressBar.visibility = View.GONE
        }
        Log.d("register", "loadDone")
    }

    private fun initEvent() {
        b.apply {
            btnRegister.setOnClickListener {
                viewModel.networkHelper.observe(this@RegisterActivity) {
                    if (it) {
                        val userName = edtName.text.toString()
                        val pass = edtPass.text.toString()
                        val rePass = edtRetypePass.text.toString()
                        if (userName.isEmpty() || pass.isEmpty() || rePass.isEmpty())
                            Toast.makeText(
                                this@RegisterActivity,
                                "Fill all information",
                                Toast.LENGTH_SHORT
                                ).show()
                        else if (!UserNameConstraint.checkUserNameFormat(userName)) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Username format is not correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!PasswordConstraint.checkPassFormat(pass)) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Password format is not correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!pass.equals(rePass)) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Re-enter incorrect password",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val user = User.userInit(userName, pass)
                            viewModel.register(user)
                        }
                    }
                }
            }

            btnBackLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }


}