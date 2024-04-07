package com.example.commyproject.base


import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


fun Activity.requestAppPermissionNotification(requestCode: Int) {
    if (SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        ActivityCompat.requestPermissions(this, arrayOf(POST_NOTIFICATIONS), requestCode)
    }
}

fun Activity.requestAppPermissionFile(requestCode: Int) {

    if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissions13 = arrayOf(READ_MEDIA_VIDEO, READ_MEDIA_AUDIO, READ_MEDIA_IMAGES)
        ActivityCompat.requestPermissions(this, permissions13, requestCode)
    } else {
        val permissions =
            arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }
}

fun Activity.checkPermissionFile(): Boolean {
//    val cameraPermission = ContextCompat.checkSelfPermission(this, CAMERA)
    if (SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        if (ContextCompat.checkSelfPermission(
                this,
                READ_MEDIA_VIDEO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                READ_MEDIA_AUDIO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }else{
        if (ContextCompat.checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }


    return true
}

fun Activity.checkPermissionNotification(): Boolean {
    if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermission = ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
        if (notificationPermission != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    } else {
        return true
    }
}

fun Context.checkPermissionRecognition(): Boolean {
    return if (SDK_INT >= Build.VERSION_CODES.Q){
        val permission = Manifest.permission.ACTIVITY_RECOGNITION
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }else{
        true
    }
}

fun Activity.requestPermissionRecognition(requestCode: Int) {
    if (SDK_INT >= Build.VERSION_CODES.Q) {
        val permission = Manifest.permission.ACTIVITY_RECOGNITION
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }
}



//fun Context.showPermissionSettingsDialog() {
//    val builder = AlertDialog.Builder(this, R.style.YourCustomAlertDialogTheme)
//    builder.setTitle("R.string.title_dialog_permission")
//        .setMessage("R.string.content_dialog_permission")
//        .setPositiveButton("R.string.setting") { _, _ ->
//            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//            val uri: Uri = Uri.fromParts("package", packageName, null)
//            intent.data = uri
//            startActivity(intent)
//        }
//        .setNegativeButton("R.string.tv_cancel") { dialog, _ ->
//            dialog.dismiss()
//        }
//        .show()
//}

