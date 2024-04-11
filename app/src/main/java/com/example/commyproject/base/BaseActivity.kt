package com.example.commyproject.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.example.commyproject.base.HideNavigation.Companion.systemBarBlack
import com.example.commyproject.databinding.ActivityLoginBinding
import java.io.Serializable


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HideNavigation.fullScreenCall(this)
        systemBarBlack(true)
        init()
        initData()
        initObserve()
        initView()
        initEvent()

    }

    protected abstract fun init()
    protected abstract fun initData()
    protected abstract fun initObserve()
    protected abstract fun initView()
    protected abstract fun initEvent()


    fun openActivity(context: Context, clazz2: Class<*>, finish: Boolean) {
        val intent = Intent(context, clazz2)
        startActivity(intent)
        if (finish) {
            finish()
        }
    }

    fun openActivityWithObj(context : Context, clazz2: Class<*>, obj : Serializable){
        val intent = Intent(context, clazz2)
        intent.putExtra("OBJ", obj)
        context.startActivity(intent)
    }
}