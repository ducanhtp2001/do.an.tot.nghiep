package com.example.commyproject.activities.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.commyproject.R
import com.example.commyproject.base.BaseActivity
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.ActivityMainBinding
import com.example.commyproject.ultil.Config
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){
    private lateinit var b: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerToggle: ActionBarDrawerToggle
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
        b.apply {
            btnMenu.setOnClickListener {
                drawerNav.visibility = if(drawerNav.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }
    }

     private fun initView() {

        navController = findNavController(R.id.fragmentContainerView)
        b.bottomNavigationView.setupWithNavController(navController)
        drawerToggle = ActionBarDrawerToggle(this, b.main, R.string.open, R.string.close)
        b.main.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(b.toolbar)
         b.drawerNav.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
             when(item.itemId) {
                 R.id.menu_item_profile -> {
                     Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show()
                 }

                 R.id.menu_item_setting -> {
                     Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show()
                 }

                 R.id.menu_item_trash -> {
                     Toast.makeText(this, "trash", Toast.LENGTH_SHORT).show()
                 }

                 R.id.menu_item_logout -> {
                     Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
                 }
             }
             return@OnNavigationItemSelectedListener true
         })

//        b.apply {
//            drawerNav.visibility = View.GONE
//        }

         b.drawerNav.apply {
             val headerView = getHeaderView(0)
             headerView.findViewById<TextView>(R.id.headerUsername)?.text = user.userName

             val avatarUrl = Config.SERVER_URL + user.avatar

             Log.d("testing", avatarUrl)

             Glide.with(this)
                 .load(avatarUrl)
                 .into(headerView.findViewById<ImageView>(R.id.headerImage))
         }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(b.main.isDrawerOpen(GravityCompat.START)) {
            b.main.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}