package com.example.commyproject.base

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class HideNavigation {
    companion object {
        fun fullScreenCall(activity: Activity) {
            val currentApiVersion: Int = Build.VERSION.SDK_INT
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

            // This work only for android 4.4+
//            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // This work only for android 4.4+
            if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
                activity.window.decorView.systemUiVisibility = flags

                // Code below is to handle presses of Volume up or Volume down.
                // Without this, after pressing volume buttons, the navigation bar will
                // show up and won't hide
                val decorView = activity.window.decorView
                decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                    if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        decorView.systemUiVisibility = flags
                    }
                }
            }

            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        fun hideNavFragment(view: View) {
            // Set an OnApplyWindowInsetsListener to adjust the layout when the navigation bar is hidden
            // Set an OnApplyWindowInsetsListener to adjust the layout when the navigation bar is hidden
            ViewCompat.setOnApplyWindowInsetsListener(view) { v: View, insets: WindowInsetsCompat ->
                // Get the current system window insets
                val systemWindowInsetBottom = insets.systemWindowInsetBottom

                // If the navigation bar is hidden, adjust the bottom margin of the root view to take up the full screen height
                if (insets.systemWindowInsetBottom == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val params =
                        v.layoutParams as FrameLayout.LayoutParams
                    params.bottomMargin = systemWindowInsetBottom
                    v.layoutParams = params
                }
                insets.replaceSystemWindowInsets(
                    insets.systemWindowInsetLeft,
                    insets.systemWindowInsetTop,
                    insets.systemWindowInsetRight,
                    0
                )
            }
        }

        fun Activity.showSystemUI(white: Boolean) {
            if (white) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            } else {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }


        fun setStatusBar(id: Int, activity: Activity) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(activity, id)
//            window.statusBarColor = Color.parseColor(id)
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = true
            }
        }

        fun Activity.systemBarBlack(b : Boolean) {
            if (b) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                }
            } else {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

}