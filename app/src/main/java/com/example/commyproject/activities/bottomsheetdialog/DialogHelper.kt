package com.example.commyproject.activities.bottomsheetdialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.commyproject.R
import com.example.commyproject.activities.profile.ProfileAct
import com.example.commyproject.base.BaseViewModel
import com.example.commyproject.base.checkPermissionFile
import com.example.commyproject.base.showPermissionSettingsDialog
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.model.EvaluationEntityType
import com.example.commyproject.data.model.FileEntity
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.StatusResponse
import com.example.commyproject.databinding.DialogBottomMenuBinding
import com.example.commyproject.databinding.DialogCommentBinding
import com.example.commyproject.databinding.DialogFileDetailBinding
import com.example.commyproject.databinding.DialogLikeBinding
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.Constant
import com.example.commyproject.ultil.adapter.CommentAdapter
import com.example.commyproject.ultil.adapter.LikeAdapter
import com.example.commyproject.ultil.converter.FileConverter
import com.example.commyproject.ultil.getNavigationBarHeight
import com.example.commyproject.ultil.getStatusBarHeight
import com.example.commyproject.ultil.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.internal.managers.ViewComponentManager
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

fun Fragment.showFileDetailDialog(
    file: FileEntry,
    updateState: (response: StatusResponse, file: FileEntity) -> Unit,
    updateLike: (evaluation: Evaluation) -> Unit,
) {
    val viewModel = ViewModelProvider(this)[DialogViewModel::class.java]
    val b = DialogFileDetailBinding.inflate(layoutInflater, null, false)

    b.root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        calculateUsableHeight()
    )

    openFileDetailDialog(
        requireContext(),
        this,
        requireActivity(),
        b,
        viewModel,
        file,
        updateState,
        updateLike
    )
}

fun Context.showFileDetailDialog(
    file: FileEntry,
    updateState: (response: StatusResponse, file: FileEntity) -> Unit,
    updateLike: (evaluation: Evaluation) -> Unit,
) {
    val mContext = if (this is ViewComponentManager.FragmentContextWrapper)
        this.baseContext
    else
        this
    val viewModel = ViewModelProvider(mContext as ViewModelStoreOwner)[DialogViewModel::class.java]
    val b = DialogFileDetailBinding.inflate(LayoutInflater.from(this), null, false)

    b.root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        calculateUsableHeight()
    )

    openFileDetailDialog(
        mContext,
        null,
        null,
        b,
        viewModel,
        file,
        updateState,
        updateLike
    )
}

private fun openFileDetailDialog(
    context: Context,
    fragment: Fragment?,
    activity: FragmentActivity?,
    b: DialogFileDetailBinding,
    viewModel: DialogViewModel,
    file: FileEntry,
    updateState: (response: StatusResponse, file: FileEntity) -> Unit,
    updateLike: (evaluation: Evaluation) -> Unit,
) {
    val likeColor = ContextCompat.getColor(context, R.color.like)
    val nonLikeColor = ContextCompat.getColor(context, R.color.black)
    val bottomDialog = BottomSheetDialog(context)
    bottomDialog.setContentView(b.root)

    val user = viewModel.user

    b.apply {
        if (file.likes.any { it.idUser == user._id }) {
            btnLikeTxt.setTextColor(likeColor)
        } else {
            btnLikeTxt.setTextColor(nonLikeColor)
        }
        content.text = file.recognizeText
        body.setOnClickListener {
            context.goToUserProfile(file.idUser)
        }
        btnMenu.setOnClickListener {
            context.showContextMenuDialog(file, updateState)
        }
        btnFollow.setOnClickListener {
            // ====================================================================================
        }
        btnBack.setOnClickListener {
            bottomDialog.dismiss()
        }
        btnOpenLike.setOnClickListener {
            context.showLikeDialog(file, null)
        }
        btnComment.setOnClickListener {
            context.showCommentDialog(file)
        }
        btnLike.setOnClickListener {
            val id = FileConverter.generateIdByUserId(user._id)
            val evaluation =
                EvaluationEntity(id, user._id, file._id, null, EvaluationEntityType.FILE)
            viewModel.postLike(evaluation) { evaluationResponse ->
                if (file.likes.any { it.idUser == evaluationResponse.idUser }) {
                    for (item in file.likes) {
                        if (item.idUser == evaluationResponse.idUser) file.likes.remove(item)
                    }
                } else {
                    file.likes.add(evaluationResponse)
                }
                btnLike.post {
                    Log.d("testing", file.likes.size.toString())
                    txtLikeCount.text = file.likes.size.toString()

                    if (file.likes.any { it.idUser == user._id }) {
                        btnLikeTxt.setTextColor(likeColor)
                        Log.d("testing", "like")
                    } else {
                        btnLikeTxt.setTextColor(nonLikeColor)
                        Log.d("testing", "dislike")
                    }
                }
                updateLike(evaluationResponse)
            }
        }
        txtLikeCount.text = file.likes.size.toString()
        txtCommentCount.text = file.comments.size.toString()

        btnOpenComment.setOnClickListener {
            context.showCommentDialog(file)
        }
        val prettyTime = FileConverter.getTimePassFromId(file._id)
        txtTime.text = prettyTime

        txtUserNameTop.text = user.userName
        txtUserNameBot.text = user.userName
        val avatarUrl = Config.SERVER_URL + user.avatar
        Glide.with(context)
            .load(avatarUrl)
            .into(avatar)
    }
    bottomDialog.show()
    if (bottomDialog.behavior.state != BottomSheetBehavior.STATE_EXPANDED) bottomDialog.behavior.state =
        BottomSheetBehavior.STATE_EXPANDED
}

fun Fragment.showCommentDialog(file: FileEntry) {
    val viewModel = ViewModelProvider(this)[DialogViewModel::class.java]
    val b = DialogCommentBinding.inflate(layoutInflater, null, false)
    b.root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        calculateUsableHeight()
    )
    openCommentDialog(requireContext(), this, requireActivity(), b, viewModel, file)
}

fun Context.showCommentDialog(file: FileEntry) {
    val viewModel = ViewModelProvider(this as ViewModelStoreOwner)[DialogViewModel::class.java]
    val b = DialogCommentBinding.inflate(LayoutInflater.from(this), null, false)
    b.root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        calculateUsableHeight()
    )
    openCommentDialog(this, null, null, b, viewModel, file)
}

private fun openCommentDialog(
    context: Context?,
    fragment: Fragment?,
    activity: FragmentActivity?,
    b: DialogCommentBinding,
    viewModel: BaseViewModel,
    file: FileEntry
) {
    val bottomDialog = BottomSheetDialog(context!!)  //, android.R.style.Theme_DeviceDefault_Light
    bottomDialog.setContentView(b.root)
    val userId = viewModel.profileId
    val cmtAdapter = CommentAdapter(
        context,
        userId,
        file.comments,
        onGoToUserProfile = { cmt ->
            context.goToUserProfile(cmt.idUser)
        },
        onClickLike = { cmt, callback ->
            postLike(null, cmt, EvaluationEntityType.COMMENT, userId, viewModel, callback)
        },
        onClickReply = { cmt ->
            viewModel.toId = cmt.idUser
            b.apply {
                toUser.visibility = View.VISIBLE
                cancelReply.visibility = View.VISIBLE
                toUser.text = context.getString(R.string.answer_to, cmt.userName)
            }
        },
        onOpenLike = { cmt ->
            context.showLikeDialog(null, cmt)
            bottomDialog.dismiss()
        }
    )
    b.listViewComment.adapter = cmtAdapter
    b.apply {
        // button send comment
        btnSend.setOnClickListener {
            if (b.inputReply.text.isNotEmpty()) {
                var content = b.inputReply.text.toString()
                b.inputReply.setText("")
                val id = FileConverter.generateIdByUserId(file.idUser)
                if (viewModel.toId != null) content = "Answer @${viewModel.toUserName} $content"
                val cmt = CommentEntity(id, userId, viewModel.toId, file._id, content)
                viewModel.postComment(cmt) {
                    file.comments.add(it)
                    (context as Activity).runOnUiThread {
                        cmtAdapter.notifyDataSetChanged()
                    }
                    viewModel.toId = null
                    viewModel.toUserName = null
                }
            }
        }
        cancelReply.setOnClickListener {
            it.visibility = View.GONE
            b.toUser.visibility = View.GONE
            viewModel.toId = null
        }
        txtLikeCount.text = file.likes.size.toString()
        like.setOnClickListener {
            context.showLikeDialog(file, null)
        }
    }
    bottomDialog.show()
    if (bottomDialog.behavior.state != BottomSheetBehavior.STATE_EXPANDED) bottomDialog.behavior.state =
        BottomSheetBehavior.STATE_EXPANDED
}

fun postLike(
    file: FileEntry?,
    cmt: Comment?,
    type: EvaluationEntityType,
    userId: String,
    viewModel: BaseViewModel,
    callback: (evaluation: Evaluation) -> Unit
) {
    val evaluationEntity: EvaluationEntity
    val id = FileConverter.generateIdByUserId(userId)
    evaluationEntity = when (type) {
        EvaluationEntityType.FILE -> {
            EvaluationEntity(id, userId, file!!._id, null, type)
        }
        EvaluationEntityType.COMMENT -> {
            EvaluationEntity(id, userId, cmt!!.idFile, cmt._id, type)
        }
    }
    viewModel.postLike(evaluationEntity) { evaluation ->
        callback(evaluation)
    }
}

fun Fragment.showLikeDialog(file: FileEntry?, cmt: Comment?) {
    val viewModel = ViewModelProvider(this)[DialogViewModel::class.java]
    val b = DialogLikeBinding.inflate(layoutInflater, null, false)
    b.root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        calculateUsableHeight()
    )
    openLikeDialog(requireContext(), this, requireActivity(), b, viewModel, file, cmt)
}

fun Context.showLikeDialog(file: FileEntry?, cmt: Comment?) {
    val viewModel = ViewModelProvider(this as ViewModelStoreOwner)[DialogViewModel::class.java]
    val b = DialogLikeBinding.inflate(LayoutInflater.from(this), null, false)
    b.root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        calculateUsableHeight()
    )
    openLikeDialog(this, null, null, b, viewModel, file, cmt)
}

private fun openLikeDialog(
    context: Context,
    fragment: Fragment?,
    activity: FragmentActivity?,
    b: DialogLikeBinding,
    viewModel: DialogViewModel,
    file: FileEntry?,
    cmt: Comment?
) {
    val bottomDialog = BottomSheetDialog(context)
    bottomDialog.setContentView(b.root)

    val likeList = file?.likes ?: cmt!!.likes
    val likeAdapter = LikeAdapter(
        context,
        likeList ?: mutableListOf(),
        openProfile = { evaluation ->
            context.goToUserProfile(evaluation.idUser)
        }
    )
    b.apply {
        listViewLike.adapter = likeAdapter

        btnBack.setOnClickListener {
            bottomDialog.dismiss()
        }
    }
    bottomDialog.show()
    if (bottomDialog.behavior.state != BottomSheetBehavior.STATE_EXPANDED) bottomDialog.behavior.state =
        BottomSheetBehavior.STATE_EXPANDED
}


// callback to update the list file display on fragment if this file change state or delete
fun Fragment.showContextMenuDialog(
    file: FileEntry,
    updateState: (response: StatusResponse, file: FileEntity) -> Unit,
//    updateLike: (Evaluation) -> Unit
) {
    val viewModel = ViewModelProvider(this)[DialogViewModel::class.java]
    val b = DialogBottomMenuBinding.inflate(layoutInflater, null, false)
    b.root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
//        calculateUsableHeight()
    )
    openContextMenuDialog(
        requireContext(),
        this,
        requireActivity(),
        b,
        viewModel,
        file,
        updateState,
//        updateLike
    )
}

fun Context.showContextMenuDialog(
    file: FileEntry,
    updateState: (response: StatusResponse, file: FileEntity) -> Unit,
//    updateLike: (Evaluation) -> Unit
) {
    val viewModel = ViewModelProvider(this as AppCompatActivity)[DialogViewModel::class.java]
    val b = DialogBottomMenuBinding.inflate(layoutInflater, null, false)
    b.root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
//        calculateUsableHeight()
    )
    openContextMenuDialog(
        this,
        null,
        null,
        b,
        viewModel,
        file,
        updateState,
//        updateLike
    )
}

private fun openContextMenuDialog(
    context: Context,
    fragment: Fragment?,
    activity: FragmentActivity?,
    b: DialogBottomMenuBinding,
    viewModel: DialogViewModel,
    file: FileEntry,
    updateState: (response: StatusResponse, file: FileEntity) -> Unit,
) {
    val bottomDialog = BottomSheetDialog(context)
    bottomDialog.setContentView(b.root)
    val fileEntity = FileEntity(file._id)
    b.apply {
        btnChangeState.setOnClickListener {
            viewModel.changeState(fileEntity) { response, mFile ->
                updateState(response, mFile)
            }
        }
        btnNotification.setOnClickListener {
            // ====================================================================================
        }
        btnDownload.setOnClickListener {
            bottomDialog.dismiss()
            viewModel.download(fileEntity) { responseBody ->
                if (context.checkPermissionFile()) {
                    context.saveFile(responseBody, file.fileName)
                } else {
                    context.showPermissionSettingsDialog()
                }
            }
        }
        btnDelete.setOnClickListener {

            viewModel.deleteFile(fileEntity) {
                context.showToast(it.msg)
            }
        }
    }

    bottomDialog.show()
}

fun Fragment.goToUserProfile(idUser: String) {
    startActivity(Intent(requireActivity(), ProfileAct::class.java).apply {
        putExtra(Constant.USER_ID, idUser)
    })
}
fun Context.goToUserProfile(idUser: String) {
    startActivity(Intent(this, ProfileAct::class.java).apply {
        putExtra(Constant.USER_ID, idUser)
    })
}

fun Fragment.goToFragment(id: Int) {
    findNavController().navigate(id)
}

fun Context.saveFile(body: ResponseBody, fileName: String) {
    try {
        Log.d("testing", "start storage")
        val inputStream: InputStream = body.byteStream()
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)
//        val file = File(getExternalFilesDir(null), fileName)
        val outputStream: OutputStream = FileOutputStream(file)

        val data = ByteArray(1024)
        var count: Int
        while (inputStream.read(data).also { count = it } != -1) {
            outputStream.write(data, 0, count)
        }
        showToast("Download success!")
        outputStream.flush()
        outputStream.close()
        inputStream.close()
    } catch (e: IOException) {
        Log.d("testing", "storage false")
        e.printStackTrace()
    }
}

fun Fragment.calculateUsableHeight(): Int {
    val windowHeight = requireActivity().window.decorView.height
    val statusBarHeight = getStatusBarHeight()
    val navigationBarHeight = getNavigationBarHeight()
    return windowHeight - statusBarHeight - navigationBarHeight
}

fun Activity.calculateUsableHeight(): Int {
    val windowHeight = window.decorView.height
    val statusBarHeight = getStatusBarHeight()
    val navigationBarHeight = getNavigationBarHeight()
    return windowHeight - statusBarHeight - navigationBarHeight
}

fun Context.calculateUsableHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = android.graphics.Point()
    display.getRealSize(size)

    val statusBarHeight = getStatusBarHeight()
    val navigationBarHeight = getNavigationBarHeight()

    return size.y - statusBarHeight - navigationBarHeight
}

fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

fun Context.getNavigationBarHeight(): Int {
    val hasNavigationBar = ViewConfiguration.get(this).hasPermanentMenuKey()
    if (hasNavigationBar) {
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    } else {
        return 0
    }
}
