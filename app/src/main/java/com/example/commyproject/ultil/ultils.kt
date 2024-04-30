package com.example.commyproject.ultil

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.commyproject.R

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

const val TAG = "testing"
fun Context.log(context: Context, msg: String) {
    Log.d(TAG, msg)
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
    val isTableCheckbox = dialogView.findViewById<CheckBox>(R.id.isTable)
    val isPublicCheckbox = dialogView.findViewById<CheckBox>(R.id.isPublic)
    val inputTitle = dialogView.findViewById<TextView>(R.id.inputName)

    btnOk.setOnClickListener {
        dialog.dismiss()
        callback(
            inputTitle.text.toString().trim(),
            isTableCheckbox.isChecked,
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