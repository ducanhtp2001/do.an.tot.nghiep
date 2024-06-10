package com.example.commyproject.activities.find

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.commyproject.R
import com.example.commyproject.base.BaseActivity
import com.example.commyproject.data.model.FollowerResponse
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.data.model.UserResponse
import com.example.commyproject.databinding.ActivityFindBinding
import com.example.commyproject.ultil.adapter.PeopleRCAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindActivity : BaseActivity() {
    private lateinit var b: ActivityFindBinding
    private lateinit var viewModel: FindViewModel
    private lateinit var adapter: PeopleRCAdapter
    private lateinit var userList: MutableList<FollowerResponse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFindBinding.inflate(layoutInflater)
        setContentView(b.root)

        viewModel = ViewModelProvider(this)[FindViewModel::class.java]

        initData()
        initView()
        initObserver()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.list.observe(this) {list ->
            if (list != null && list.isNotEmpty()) {
                this@FindActivity.userList.clear()
                this@FindActivity.userList.addAll(list)
                this@FindActivity.adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initData() {
        userList = mutableListOf()
        adapter = PeopleRCAdapter(this, R.layout.item_people_horizontal, userList)
    }

    private fun initView() {

        b.apply {
            btnBack.setOnClickListener { onBackPressed() }
            btnFind.setOnClickListener {
                if (inputKeyword.text.isNotEmpty()) {
                    viewModel.findUser(inputKeyword.text.toString())
                }
            }
            rcvUser.apply {
                layoutManager = LinearLayoutManager(this@FindActivity)
                adapter = this@FindActivity.adapter
            }
        }

    }
}