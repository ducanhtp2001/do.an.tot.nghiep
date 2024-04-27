package com.example.commyproject.activities.main

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
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.ActivityMainBinding
import com.example.commyproject.ultil.Config
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainActViewModel

    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        viewModel = ViewModelProvider(this)[MainActViewModel::class.java]
        user = viewModel.getUser()

        initView()
        initEvent()
    }

    private fun initEvent() {
    }

    private fun initView() {

        navController = findNavController(R.id.fragmentContainerView)
        b.topAppBar.contentMain.bottomNavigationView.setupWithNavController(navController)
        b.topAppBar.toolBar.setNavigationIcon(R.drawable.ic_menu_2)

        b.topAppBar.toolBar.setNavigationOnClickListener {
            if (!b.main.isDrawerOpen(GravityCompat.START)) {
                b.main.openDrawer(GravityCompat.START)
            } else {
                b.main.closeDrawer(GravityCompat.START)
            }
        }

        b.drawerNav.apply {
            val headerView = getHeaderView(0)
            headerView.findViewById<TextView>(R.id.headerUsername)?.text = user.userName

            val avatarUrl = Config.SERVER_URL + user.avatar

            Log.d("testing", avatarUrl)

            Glide.with(this)
                .load(avatarUrl)
                .into(headerView.findViewById(R.id.headerImage))
        }

//        Log.d("testing", "Menu items count: ${b.drawerNav.menu.size()}")

        b.drawerNav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_profile -> {
                    Log.d("MainActivity", "Profile clicked")

                }

                R.id.menu_item_setting -> {
                    Log.d("MainActivity", "Setting clicked")

                }

                R.id.menu_item_trash -> {
                    Log.d("MainActivity", "Trash clicked")

                }

                R.id.menu_item_logout -> {
                    Log.d("MainActivity", "Logout clicked")

                }

                else -> {}
            }
            b.main.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.socketDisconnect()
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