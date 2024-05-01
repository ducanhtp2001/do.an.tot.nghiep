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
import com.example.commyproject.activities.bottomsheetdialog.postLike
import com.example.commyproject.activities.bottomsheetdialog.showCommentDialog
import com.example.commyproject.activities.bottomsheetdialog.showContextMenuDialog
import com.example.commyproject.activities.bottomsheetdialog.showFileDetailDialog
import com.example.commyproject.activities.bottomsheetdialog.showLikeDialog
import com.example.commyproject.activities.find.FindActivity
import com.example.commyproject.activities.setting.SettingActivity
import com.example.commyproject.data.model.EvaluationEntityType
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.databinding.ActivityProfileBinding
import com.example.commyproject.ultil.Constant
import com.example.commyproject.ultil.adapter.GlobalFileRCAdapter
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
    private lateinit var fileAdapter: GlobalFileRCAdapter
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
                    viewModel.profileUserName = it.user.userName
                    viewModel.profileId = it.user._id
                    viewModel.profileAvatar = it.user.avatar
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
            followerAdapter = PeopleRCAdapter(this@ProfileAct, R.layout.item_people, user.followers)
            val layoutManager = LinearLayoutManager(this@ProfileAct)
            listViewFollower.layoutManager = layoutManager
            listViewFollower.adapter = followerAdapter

            fileAdapter = GlobalFileRCAdapter(
                this@ProfileAct,
                viewModel.user, // user cua nguoi su dung
                user, // user data cua profile dang duoc show
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
                    this@ProfileAct.showLikeDialog(file, null)
                },
                onOpenCommentDialog = { file ->
                    this@ProfileAct.showCommentDialog(file)
                },
                openFileDetail = { file, updateLike ->
                    this@ProfileAct.showFileDetailDialog(file,
                        updateState = { response, mFile ->
                            this@ProfileAct.showToast(response.msg)
                            this@ProfileAct.list.removeIf { it._id == mFile._id }
                            this@ProfileAct.runOnUiThread {
                                this@ProfileAct.fileAdapter.notifyDataSetChanged()
                            }
                        },
                        updateLike = { evaluation ->
                            updateLike(evaluation)
                            val position = list.indexOf(file)
                            if (list[position].likes.any { it.idUser == evaluation.idUser }) {
                                list[position].likes.removeIf{ it.idUser == evaluation.idUser }
                            } else {
                                list[position].likes.add(evaluation)
                            }
                        })
                },
                onLike = { file, callback ->
                    postLike(file, null, EvaluationEntityType.FILE, user._id, viewModel) { evaluation ->
                        callback(evaluation)
                    }
                },
                onFollow = { file, callback ->

                }
            )
            listViewFile.layoutManager = LinearLayoutManager(this@ProfileAct).apply {
                isAutoMeasureEnabled = true
            }
            listViewFile.isFocusable = false
//            listViewFile.isNestedScrollingEnabled = false
            listViewFile.adapter = fileAdapter
            listViewFile.requestLayout()
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