package com.example.commyproject.activities.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener{
    private lateinit var b: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainActViewModel

    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        viewModel = ViewModelProvider(this)[MainActViewModel::class.java]
        user = viewModel.getUser()

        initView()
        initEvent()
    }
    private fun initEvent() {

        b.main.setOnClickListener {
            Toast.makeText(this, "main", Toast.LENGTH_SHORT).show()

        }

        b.drawerNav.setOnClickListener {
            Toast.makeText(this, "nav", Toast.LENGTH_SHORT).show()

        }

        b.frameLayout.setOnClickListener {
            Toast.makeText(this, "frameL", Toast.LENGTH_SHORT).show()

        }

        b.bottomNavigationView.setOnClickListener {
            Toast.makeText(this, "bott", Toast.LENGTH_SHORT).show()

        }
    }

     private fun initView() {

        navController = findNavController(R.id.fragmentContainerView)
        b.bottomNavigationView.setupWithNavController(navController)
//        b.topAppBar.toolBar.setNavigationIcon(R.drawable.ic_menu_2)
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

         b.drawerNav.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        if(b.main.isDrawerOpen(GravityCompat.START)) {
            b.main.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
         return when(menuItem.itemId) {
            R.id.menu_item_profile -> {
                Log.d("MainActivity", "Profile clicked")
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show()

                true
            }

            R.id.menu_item_setting -> {
                Log.d("MainActivity", "Setting clicked")
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show()

                true
            }

            R.id.menu_item_trash -> {
                Log.d("MainActivity", "Trash clicked")
                Toast.makeText(this, "trash", Toast.LENGTH_SHORT).show()

                true
            }

            R.id.menu_item_logout -> {
                Log.d("MainActivity", "Logout clicked")
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()

                true
            }

             else -> {false}
         }
    }
}