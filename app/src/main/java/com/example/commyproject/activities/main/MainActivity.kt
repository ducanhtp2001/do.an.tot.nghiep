package com.example.commyproject.activities.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.commyproject.R
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.ActivityMainBinding
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.showToast
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint

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
            // Mở navigation drawer
            if (!b.main.isDrawerOpen(GravityCompat.START)) {
                b.main.openDrawer(GravityCompat.START)
            } else {
                b.main.closeDrawer(GravityCompat.START)
            }
        }
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        b.drawerNav.apply {
            val headerView = getHeaderView(0)
            headerView.findViewById<TextView>(R.id.headerUsername)?.text = user.userName

            val avatarUrl = Config.SERVER_URL + user.avatar

            Log.d("testing", avatarUrl)

            Glide.with(this)
                .load(avatarUrl)
                .into(headerView.findViewById<ImageView>(R.id.headerImage))
        }

        Log.d("testing", "Menu items count: ${b.drawerNav.menu.size()}")

//        setSupportActionBar(b.topAppBar.toolBar)

        b.drawerNav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_profile -> {
                    Log.d("MainActivity", "Profile clicked")
                    Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show()

                }

                R.id.menu_item_setting -> {
                    Log.d("MainActivity", "Setting clicked")
                    Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show()

                }

                R.id.menu_item_trash -> {
                    Log.d("MainActivity", "Trash clicked")
                    Toast.makeText(this, "trash", Toast.LENGTH_SHORT).show()

                }

                R.id.menu_item_logout -> {
                    Log.d("MainActivity", "Logout clicked")
                    Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()

                }

                else -> {}
            }
            b.main.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

    }

    override fun onBackPressed() {
        if (b.main.isDrawerOpen(GravityCompat.START)) {
            b.main.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}