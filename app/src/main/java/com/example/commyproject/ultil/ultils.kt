package com.example.commyproject.ultil

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.commyproject.R
import com.example.commyproject.activities.bottomsheetdialog.runOnUiThread
import com.example.commyproject.activities.main.fragment.home.HomeFragment
import com.example.commyproject.databinding.DialogNotifiNetworkConnectStatusBinding
import com.example.commyproject.ultil.Constant.ACTION_SEND_NOTIFY_STATE_NETWORK
import com.example.commyproject.ultil.Constant.DATA_STATE_NETWORK
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
fun Context.showLongToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
fun Context.loadImg(imgName: String, imageView: ImageView) {
    val avatarUrl = Config.SERVER_URL + imgName
    Glide.with(this)
        .load(avatarUrl)
        .into(imageView)
}
const val TAG = "testing"
fun Context.log(msg: String) {
    Log.d(TAG, msg)
}
fun log(msg: String) {
    Log.d(TAG, msg)
}
fun loge(msg: String) {
    Log.e(TAG, msg)
}
fun Context.showSetConfigDialog(callback: (name: String, isTable: Boolean, isPublic: Boolean) -> Unit) {

    val builder = AlertDialog.Builder(this)
    val inflater = LayoutInflater.from(this)
    val dialogView = inflater.inflate(R.layout.dialog_set_config_layout, null)
    builder.setView(dialogView)
    val dialog = builder.create()
    dialog.setCancelable(true)

    val btnOk = dialogView.findViewById<Button>(R.id.btnOk)
    val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
//    val isTableCheckbox = dialogView.findViewById<CheckBox>(R.id.isTable)
    val isPublicCheckbox = dialogView.findViewById<CheckBox>(R.id.isPublic)
    val inputTitle = dialogView.findViewById<TextView>(R.id.inputName)

    btnOk.setOnClickListener {
        dialog.dismiss()
        callback(
            inputTitle.text.toString().trim(),
//            isTableCheckbox.isChecked
              false,
            isPublicCheckbox.isChecked
        )
    }
    btnCancel.setOnClickListener { dialog.dismiss() }
    dialog.show()
}

fun Fragment.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

fun Fragment.getNavigationBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

fun Activity.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

fun Activity.getNavigationBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

fun Context.loadAvatar(idUser: String, view: ImageView) {
    val avatarUrl = Config.SERVER_URL + "/get_avatar/$idUser.png"
    Glide.with(this)
        .load(avatarUrl)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(view)
}

fun Context.loadBanner(idUser: String, view: ImageView) {
    val avatarUrl = Config.SERVER_URL + "/get_banner/$idUser.png"
    Glide.with(this)
        .load(avatarUrl)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(view)
}

fun Context.checkFilePermission(): Boolean {
    return (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) != PackageManager.PERMISSION_GRANTED)
}

fun Activity.requestFilePermission() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
        HomeFragment.PERMISSION_READ_STORAGE
    )
    if (checkFilePermission())
        startActivityForResult(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(
                    "package:${this.packageName}"
                )
            ), HomeFragment.PERMISSION_READ_STORAGE
        )
}

var networkDialog: Dialog? = null
var firstNotifyNetwork : Boolean = true
fun Context.showNotificationNetworkDialog(isConnect: Boolean) {

    if (networkDialog != null && networkDialog!!.isShowing) {
        networkDialog!!.dismiss()
    }
    networkDialog = Dialog(this)

    val binding = DialogNotifiNetworkConnectStatusBinding.inflate(LayoutInflater.from(this))
    networkDialog!!.setContentView(binding.root)
    networkDialog!!.setCancelable(false)
    binding.apply {
        if (isConnect) {
            contentDialog.text = getString(R.string.network_connected)
        } else {
            contentDialog.text = getString(R.string.network_disconnected)
        }

        btnOk.setOnClickListener {
            networkDialog!!.dismiss()
        }
    }
//    networkDialog!!.show()
    networkDialog!!.apply {
        loge("before: isShowing: $isShowing, isFirst: $firstNotifyNetwork")
        if (isShowing) {
            dismiss()
            show()
        }
        if (!firstNotifyNetwork) {
            show()
        } else {
            firstNotifyNetwork = false
        }
    }
    loge("show this dialog?: ${networkDialog!!.isShowing}")
}

fun Context.registerNetworkBroadCaseReceiver(): BroadcastReceiver {
    return registerBroadCastWithTag(ACTION_SEND_NOTIFY_STATE_NETWORK) { _, intent ->
        try {
            val isConnect = intent?.getStringExtra(DATA_STATE_NETWORK).toBoolean()
            MainScope().launch {
                showNotificationNetworkDialog(isConnect)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
fun Context.sendNetworkBroadCastReceiver(isConnected: Boolean) {
    sendBroadCastWithTag(ACTION_SEND_NOTIFY_STATE_NETWORK, mapOf(DATA_STATE_NETWORK to isConnected.toString()))
}

fun Context.sendBroadCastWithTag(action: String, data: Map<String, String>?) {
    sendBroadcast(Intent(action).apply {
        data?.forEach { (name, value) ->
            putExtra(name, value)
        }
    })
}

fun Context.registerBroadCastWithTag(
    action: String,
    callback: (context: Context?, intent: Intent?) -> Unit
): BroadcastReceiver {

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            loge("network event   ..........................  ")
            try {
                callback(context, intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    val intentFilter = IntentFilter().apply {
        addAction(action)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        registerReceiver(broadcastReceiver, intentFilter, Service.RECEIVER_NOT_EXPORTED)
    } else {
        registerReceiver(broadcastReceiver, intentFilter)
    }
    return broadcastReceiver
}