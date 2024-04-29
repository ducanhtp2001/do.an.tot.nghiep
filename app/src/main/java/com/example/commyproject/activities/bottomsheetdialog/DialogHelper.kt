package com.example.commyproject.activities.bottomsheetdialog

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.commyproject.R
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntity
import com.example.commyproject.data.model.EvaluationEntityType
import com.example.commyproject.data.model.FileEntity
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.data.model.StatusResponse
import com.example.commyproject.data.model.User
import com.example.commyproject.databinding.DialogBottomMenuBinding
import com.example.commyproject.databinding.DialogCommentBinding
import com.example.commyproject.databinding.DialogFileDetailBinding
import com.example.commyproject.databinding.DialogLikeBinding
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.adapter.CommentAdapter
import com.example.commyproject.ultil.adapter.LikeAdapter
import com.example.commyproject.ultil.converter.FileConverter
import com.example.commyproject.ultil.getNavigationBarHeight
import com.example.commyproject.ultil.getStatusBarHeight
import com.example.commyproject.ultil.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.showFileDetailDialog(file: FileEntry) {
    val viewModel = ViewModelProvider(this)[DialogViewModel::class.java]
    val b = DialogFileDetailBinding.inflate(layoutInflater, null, false)

    b.root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        calculateUsableHeight()
    )

    openFileDetailDialog(requireContext(), b, viewModel, file)
}

private fun openFileDetailDialog(
    context: Context?,
    b: DialogFileDetailBinding,
    viewModel: DialogViewModel,
    file: FileEntry
) {
    val bottomSheetDialog = BottomSheetDialog(context!!)
    bottomSheetDialog.setContentView(b.root)

    val user = viewModel.user



    b.apply {

        content.text = file.recognizeText
        body.setOnClickListener {
            // open profile
        }
        btnMenu.setOnClickListener {
            // ====================================================================================
        }
        btnFollow.setOnClickListener {
            // ====================================================================================
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

    bottomSheetDialog.show()
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

private fun openCommentDialog(
    context: Context?,
    fragment: Fragment,
    activity: FragmentActivity,
    b: DialogCommentBinding,
    viewModel: DialogViewModel,
    file: FileEntry
) {
    val bottomDialog = BottomSheetDialog(context!!)  //, android.R.style.Theme_DeviceDefault_Light
    bottomDialog.setContentView(b.root)
    val user = viewModel.user

    val cmtAdapter = CommentAdapter(
        context,
        user._id,
        file.comments,
        onGoToUserProfile = { cmt ->
            fragment.goToUserProfile(cmt.idUser)
        },
        onClickLike = { cmt, callback ->
            postLike(null, cmt, EvaluationEntityType.COMMENT, user, viewModel, callback)
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
            fragment.showLikeDialog(null, cmt)
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
                val cmt = CommentEntity(id, user._id, viewModel.toId, file._id, content)
                viewModel.postComment(cmt) {
                    file.comments.add(it)
                    activity.runOnUiThread {
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
            fragment.showLikeDialog(file, null)
        }
    }

    bottomDialog.show()
}

private fun postLike(
    file: FileEntry?,
    cmt: Comment?,
    type: EvaluationEntityType,
    user: User,
    viewModel: DialogViewModel,
    callback: (evaluation: Evaluation) -> Unit
) {
    val evaluationEntity: EvaluationEntity
    val id = FileConverter.generateIdByUserId(user._id)
    evaluationEntity = when (type) {
        EvaluationEntityType.FILE -> {
            EvaluationEntity(id, user._id, file!!._id, null, type)
        }

        EvaluationEntityType.COMMENT -> {
            EvaluationEntity(id, user._id, cmt!!.idFile, cmt._id, type)
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

private fun openLikeDialog(
    context: Context,
    fragment: Fragment,
    activity: FragmentActivity,
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
            fragment.goToUserProfile(evaluation.idUser)
        }
    )

    b.apply {
        listViewLike.adapter = likeAdapter

        btnBack.setOnClickListener {
            bottomDialog.dismiss()
        }
    }

    bottomDialog.show()
}


// callback to update the list file display on fragment if this file change state or delete
fun Fragment.showContextMenuDialog(file: FileEntry, callback: (response: StatusResponse, file: FileEntity) -> Unit) {
    val viewModel = ViewModelProvider(this)[DialogViewModel::class.java]
    val b = DialogBottomMenuBinding.inflate(layoutInflater, null, false)
    b.root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        calculateUsableHeight()
    )
    openContextMenuDialog(requireContext(), this, requireActivity(), b, viewModel, file, callback)
}

private fun openContextMenuDialog(
    context: Context,
    fragment: Fragment,
    activity: FragmentActivity,
    b: DialogBottomMenuBinding,
    viewModel: DialogViewModel,
    file: FileEntry,
    callback: (response: StatusResponse, file: FileEntity) -> Unit
) {
    val bottomDialog = BottomSheetDialog(context)
    bottomDialog.setContentView(b.root)
    val fileEntity = FileEntity(file._id)
    b.apply {
        btnDetail.setOnClickListener {
            fragment.showFileDetailDialog(file)
        }
        btnChangeState.setOnClickListener {
            viewModel.changeState(fileEntity) { response, mFile ->
                callback(response, mFile)
//                context.showToast(response.msg)
//                list.removeIf {
//                    it._id == file._id
//                }
//                activity.runOnUiThread {
//                    adapter.notifyDataSetChanged()
//                }
            }

        }
        btnNotification.setOnClickListener {

        }
        btnDownload.setOnClickListener {

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
    requireContext().showToast("Open user profile")
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