package com.example.commyproject.activities.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.commyproject.R
import com.example.commyproject.activities.bottomsheetdialog.showContextMenuDialog
import com.example.commyproject.activities.find.FindActivity
import com.example.commyproject.activities.setting.SettingActivity
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.databinding.ActivityProfileBinding
import com.example.commyproject.ultil.Constant
import com.example.commyproject.ultil.adapter.GlobalFileAdapter
import com.example.commyproject.ultil.adapter.PeopleRCAdapter
import com.example.commyproject.ultil.loadImg
import com.example.commyproject.ultil.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileAct : AppCompatActivity() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var b: ActivityProfileBinding
    private lateinit var list: MutableList<FileEntry>
    private lateinit var followerAdapter: PeopleRCAdapter
    private lateinit var fileAdapter: GlobalFileAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(b.root)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        initData()
        initObserver()
    }

    private fun initObserver() {
        viewModel.apply {
            profile.observe(this@ProfileAct) {
                if (viewModel.profile.value != null) {
                    viewModel.user = it.user
                    viewModel.userName = it.user.userName
                    viewModel.userId = it.user._id
                    viewModel.userAvatar = it.user.avatar
                    list = it.files
                    initView()
                }
            }
            loadingState.observe(this@ProfileAct) {
                if (it) b.progressBar.visibility = View.VISIBLE
                else b.progressBar.visibility = View.GONE
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun initView() {
        val user = viewModel.profile.value!!.user
        b.apply {
            btnBack.setOnClickListener { finish() }
            txtUserNameTop.text = user.userName
            txtUserNameBot.text = user.userName
            this@ProfileAct.loadImg(user.avatar, imgAvatar)
            btnFind.setOnClickListener {
                this@ProfileAct.startActivity(
                    Intent(
                        this@ProfileAct,
                        FindActivity::class.java
                    )
                )
            }
            txtFollowCount.text = getString(R.string.follow_count, user.follows.size)
            txtFollowerCount.text = getString(R.string.followers_count, user.followers.size)
            btnSetting.setOnClickListener {
                startActivity(Intent(this@ProfileAct, SettingActivity::class.java))
            }
            followerAdapter = PeopleRCAdapter(this@ProfileAct, user.followers)
            val layoutManager = LinearLayoutManager(this@ProfileAct)
            listViewFollower.layoutManager = layoutManager
            listViewFollower.adapter = followerAdapter

            fileAdapter = GlobalFileAdapter(
                this@ProfileAct,
                user._id,
                user.userName,
                list,
                createContextMenu = { file ->
                     this@ProfileAct.showContextMenuDialog(
                             file,
                         updateState = { response, mFile ->
                             this@ProfileAct.showToast(response.msg)
                             list.removeIf {
                                 it._id == mFile._id
                             }
                             this@ProfileAct.runOnUiThread {
                                 fileAdapter.notifyDataSetChanged()
                             }
                         },
                     )
                },
                hideFile = { file, callback ->
                },
                onOpenLikeDialog = { file ->

                },
                onOpenCommentDialog = { file ->

                },
                openFileDetail = { file, callback ->

                },
                onLike = { file, callback ->

                }
            )
        }


    }

    private fun initData() {
        val intent = intent
        val id = intent.getStringExtra(Constant.USER_ID)
        id?.let {
            val userEntity = UserEntity(id)
            viewModel.getProfile(userEntity)
        } ?: {
            onBackPressed()
            Log.e("testing", "not found id")
        }
    }
}