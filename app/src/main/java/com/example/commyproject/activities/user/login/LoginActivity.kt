package com.example.commyproject.activities.user.login

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
import com.example.commyproject.activities.main.MainActivity
import com.example.commyproject.activities.user.fogetpass.FogetPasswordActivity
import com.example.commyproject.activities.user.register.RegisterActivity
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.ActivityLoginBinding
import com.example.commyproject.ultil.constraint.PasswordConstraint
import com.example.commyproject.ultil.constraint.UserNameConstraint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var b: ActivityLoginBinding
    private lateinit var viewModel: LoginActViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)
        viewModel = ViewModelProvider(this)[LoginActViewModel::class.java]

        initEvent()
        initObserver()
    }

    private fun initObserver() {
        viewModel.stateLoading.observe(this) {
            if (it) {
                loading()
            } else {
                loadDone()
                if (viewModel.stateLogin.value != null && viewModel.stateLogin.value!!) {
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.stateLogin.observe(this) {
            if (it) startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun initEvent() {
        b.apply {
            btnForgetPass.setOnClickListener {
                startActivity(Intent(this@LoginActivity, FogetPasswordActivity::class.java))
            }

            btnNewAcc.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            btnLogin.setOnClickListener {
                viewModel.networkHelper.observe(this@LoginActivity) {
                    if (it) {
                        val userName = edtName.text.toString()
                        val pass = edtPass.text.toString()
                        if (userName.isEmpty() || pass.isEmpty())
                            Toast.makeText(
                                this@LoginActivity,
                                "Fill all information",
                                Toast.LENGTH_SHORT
                            ).show()
                        else if (!UserNameConstraint.checkUserNameFormat(userName)) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Username format is not correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!PasswordConstraint.checkPassFormat(pass)) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Password format is not correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val user = User(userName = userName, passWord =  pass)
                            Log.d("testing", "user: $user")
                            viewModel.login(user)
                        }
                    }
                }
            }
        }
    }

    private fun loading() {
        b.apply {
            btnLogin.isEnabled = false
            btnForgetPass.isEnabled = false
            btnNewAcc.isEnabled = false
            progressBar.visibility = View.VISIBLE
        }
        Log.d("login", "loading")
    }
    private fun loadDone() {
        b.apply {
            btnLogin.isEnabled = true
            btnForgetPass.isEnabled = true
            btnNewAcc.isEnabled = true
            progressBar.visibility = View.GONE
        }
        Log.d("login", "loadDone")

    }
}