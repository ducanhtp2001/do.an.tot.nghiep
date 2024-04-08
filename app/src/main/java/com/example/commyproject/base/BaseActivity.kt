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

    protected abstract fun getLayoutResId(): Int
    protected abstract fun init()
    protected abstract fun initData()
    protected abstract fun initObserve()
    protected abstract fun initView()
    protected abstract fun initEvent()


    fun openActivityFirst(context: Context, clazz2: Class<*>, first: Boolean, finish: Boolean) {
        val intent = Intent(context, clazz2)
        intent.putExtra("FIRST_APP", first)
        startActivity(intent)
        if (finish) {
            finish()
        }
    }

    fun openSplashToMain(context: Context, clazz: Class<*>, data: Any){
        val intent = Intent(context, clazz)
        when (data) {
            is Int -> intent.putExtra("DATA", data)
            is String -> intent.putExtra("DATA", data)
            is Boolean -> intent.putExtra("DATA", data)
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    fun openActivityWithData(context: Context, clazz: Class<*>, data: Any, data2: Any) {
        val intent = Intent(context, clazz)
        when (data) {
            is Int -> intent.putExtra("DATA", data)
            is String -> intent.putExtra("DATA", data)
            is Boolean -> intent.putExtra("DATA", data)
        }
        when (data2) {
            is Int -> intent.putExtra("DATA2", data2)
            is String -> intent.putExtra("DATA2", data2)
            is Boolean -> intent.putExtra("DATA2", data2)
        }
        context.startActivity(intent)
    }


    fun openActivityWithDataString(context: Context, clazz2: Class<*>, data: String) {
        val intent = Intent(context, clazz2)
        intent.putExtra("data", data)
        startActivity(intent)
    }

    fun openActivityWithObj(context : Context, clazz2: Class<*>, obj : Serializable){
        val intent = Intent(context, clazz2)
        intent.putExtra("OBJ", obj)
        context.startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
//        HideNavigation.setStatusBar(R.color.black, this)

    }




}