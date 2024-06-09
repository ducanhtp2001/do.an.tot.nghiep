package com.example.commyproject.activities.profile

import android.annotation.SuppressLint
import android.app.Activity
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
import com.example.commyproject.activities.main.fragment.home.HomeFragment
import com.example.commyproject.activities.setting.SettingActivity
import com.example.commyproject.data.model.EvaluationEntityType
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.UserEntity
import com.example.commyproject.databinding.ActivityProfileBinding
import com.example.commyproject.ultil.Constant
import com.example.commyproject.ultil.adapter.ProfileFileRCAdapter
import com.example.commyproject.ultil.adapter.PeopleRCAdapter
import com.example.commyproject.ultil.checkFilePermission
import com.example.commyproject.ultil.converter.FileConverter
import com.example.commyproject.ultil.loadAvatar
import com.example.commyproject.ultil.loadBanner
import com.example.commyproject.ultil.loadImg
import com.example.commyproject.ultil.requestFilePermission
import com.example.commyproject.ultil.showSetConfigDialog
import com.example.commyproject.ultil.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileAct : AppCompatActivity() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var b: ActivityProfileBinding
    private lateinit var list: MutableList<FileEntry>
    private lateinit var followerAdapter: PeopleRCAdapter
    private lateinit var fileAdapter: ProfileFileRCAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(b.root)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        initData()
        initEvent()
        initObserver()
    }

    private fun initEvent() {
        b.apply {
            imgAvatar.setOnClickListener {
                if (checkFilePermission()) {
                    requestFilePermission()
                } else {
                    readFile(REQUEST_AVATAR)
                }
            }

            imgBanner.setOnClickListener {
                if (checkFilePermission()) {
                    requestFilePermission()
                } else {
                    readFile(REQUEST_BANNER)
                }
            }
        }
    }

    private fun readFile(action: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "image/*"
            ))
        }
        startActivityForResult(Intent.createChooser(intent, "Select Image"), action)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var type = "AVATAR"
            if (requestCode != REQUEST_AVATAR) {
                type = "BANNER"
            }
            data?.data?.let { uri ->
//                showToast(uri.toString())
                viewModel.uploadImage(this, uri, viewModel.profileId, type) {
                    runOnUiThread {
                        showToast(it)
                        if (type == "BANNER") loadBanner(viewModel.profileId, b.imgBanner)
                        else loadAvatar(viewModel.profileId, b.imgAvatar)
                    }
                }
            }
        }
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
            btnFind.setOnClickListener {
                startActivity(Intent(this@ProfileAct, FindActivity::class.java))
            }
            btnBack.setOnClickListener { finish() }
            txtUserNameTop.text = user.userName
            txtUserNameBot.text = user.userName
            this@ProfileAct.loadAvatar(user._id, imgAvatar)
            this@ProfileAct.loadBanner(user._id, imgBanner)
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
            listViewFollower.layoutManager = LinearLayoutManager(this@ProfileAct, LinearLayoutManager.HORIZONTAL, false)
            listViewFollower.adapter = followerAdapter

            fileAdapter = ProfileFileRCAdapter(
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
                         onDelete = { fileId ->
                             this@ProfileAct.runOnUiThread {
                                 list.removeIf { it._id == fileId}
                                 fileAdapter.notifyDataSetChanged()
                             }

                         }
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
                        },
                        onDelete = { fileId ->
                            this@ProfileAct.runOnUiThread {
                                list.removeIf { it._id == fileId}
                                fileAdapter.notifyDataSetChanged()
                            }

                        }
                    )
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

    companion object {
        const val REQUEST_AVATAR = 10200
        const val REQUEST_BANNER = 10201
    }
}