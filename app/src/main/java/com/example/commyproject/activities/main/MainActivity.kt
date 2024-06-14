package com.example.commyproject.activities.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.commyproject.R
import com.example.commyproject.activities.profile.ProfileAct
import com.example.commyproject.activities.setting.SettingActivity
import com.example.commyproject.activities.user.login.LoginActivity
import com.example.commyproject.base.BaseActivity
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.ActivityMainBinding
import com.example.commyproject.service.ReceiverService
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.Constant
import com.example.commyproject.ultil.loadAvatar
import com.example.commyproject.ultil.showNotificationNetworkDialog
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var b: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainActViewModel
    private lateinit var receiver: BroadcastReceiver
    private lateinit var destinationChangedListener: NavController.OnDestinationChangedListener

    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        viewModel = ViewModelProvider(this)[MainActViewModel::class.java]
        user = viewModel.getUserFromCache()

        initView()
        initEvent()

        val intent = Intent(this, ReceiverService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fresh() {
            b.drawerNav.apply {
                val headerView = getHeaderView(0)
                headerView.findViewById<TextView>(R.id.headerUsername)?.text = viewModel.user.userName
                headerView.findViewById<TextView>(R.id.headerGmail)?.text = viewModel.user.email
                context.loadAvatar(viewModel.user._id, headerView.findViewById(R.id.headerImage))
            }
        }
    }

    private fun initEvent() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val notificationMenuItem = b.topAppBar.contentMain.bottomNavigationView.menu.findItem(R.id.notificationFragment)
                notificationMenuItem.setIcon(R.drawable.ic_notification_1)
            }
        }

        val filter = IntentFilter(Constant.BROADCAST_ACTION)
        registerReceiver(receiver, filter)

        destinationChangedListener = NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.notificationFragment) {
                val notificationMenuItem = b.topAppBar.contentMain.bottomNavigationView.menu.findItem(R.id.notificationFragment)
                notificationMenuItem.setIcon(R.drawable.ic_notification)
            }
        }
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    private fun initView() {

        navController = findNavController(R.id.fragmentContainerView)

        b.topAppBar.contentMain.bottomNavigationView.setupWithNavController(navController)
//        b.topAppBar.toolBar.setNavigationIcon(R.drawable.ic_menu_2)

        b.topAppBar.toolBar.setNavigationOnClickListener {
            if (!b.main.isDrawerOpen(GravityCompat.START)) {
                b.main.openDrawer(GravityCompat.START)
            } else {
                b.main.closeDrawer(GravityCompat.START)
            }
        }

//        b.drawerNav.apply {
//            val headerView = getHeaderView(0)
//            headerView.findViewById<TextView>(R.id.headerUsername)?.text = user.userName
//
////            val avatarUrl = Config.SERVER_URL + user.avatar
////
////            Log.d("testing", avatarUrl)
////
////            Glide.with(this)
////                .load(avatarUrl)
////                .into(headerView.findViewById(R.id.headerImage))
//            context.loadAvatar(user._id, headerView.findViewById(R.id.headerImage))
//        }

//        Log.d("testing", "Menu items count: ${b.drawerNav.menu.size()}")

        b.drawerNav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_profile -> {
//                    Log.d("MainActivity", "Profile clicked")
//                    navController.navigate(R.id.fileDetailFragment)
                    startActivity(Intent(this, ProfileAct::class.java).apply {
                        putExtra(Constant.USER_ID, user._id)
                    })
                }

                R.id.menu_item_setting -> {
                    startActivity(Intent(this, SettingActivity::class.java).apply {
                        putExtra(Constant.USER_ID, user._id)
                    })
                }

//                R.id.menu_item_trash -> {
//                    Log.d("MainActivity", "Trash clicked")
//
//                }

                R.id.menu_item_logout -> {
                    Log.d("MainActivity", "Logout")
                    stopService(Intent(this@MainActivity, ReceiverService::class.java))
                    viewModel.clearData()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }

                else -> {}
            }
            b.main.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        navController.removeOnDestinationChangedListener(destinationChangedListener)
        super.onDestroy()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (b.main.isDrawerOpen(GravityCompat.START)) {
            b.main.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}