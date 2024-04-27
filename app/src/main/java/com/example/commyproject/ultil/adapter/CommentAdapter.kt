package com.example.commyproject.ultil.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.commyproject.R
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.converter.FileConverter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CommentAdapter(
    private val context: Context,
    private val idUser: String,
    private val allComments: MutableList<Comment> = mutableListOf(),
    private val onGoToUserProfile: (cmt: Comment) -> Unit,
    private val onClickLike: (cmt: Comment, callback: (evaluation: Evaluation) -> Unit) -> Unit,
//    private val sendComment: (CommentEntity, callback: (Comment) -> Unit) -> Unit,
    private val onClickReply: (cmt: Comment) -> Unit,
    private val onOpenLike: (cmt: Comment) -> Unit
): BaseAdapter() {
    override fun getCount(): Int {
        return allComments.size
    }

    override fun getItem(position: Int): Any {
        return allComments[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: CommentViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
            viewHolder = CommentViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as CommentViewHolder
        }

        val data = allComments[position]

        // neu user like thi nut like mau xanh
        data.likes?.let {
            for (item in it) {
                if (item.idUser == idUser) viewHolder.btnLike.setTextColor(0x3586F6)
                else viewHolder.btnLike.setTextColor(0x000000)
            }
        }

        viewHolder.apply {
            val avatarUrl = Config.SERVER_URL + data.avatar

            Glide.with(context)
                .load(avatarUrl)
                .into(avatar)

            userName.text = data.userName
            cmtContent.text = data.content
            txtTime.text = FileConverter.getTimePassFromId(data._id)
            txtLikeCount.text = data.likes?.size?.toString() ?: "0"

            avatar.setOnClickListener { onGoToUserProfile(data) }
            userName.setOnClickListener { onGoToUserProfile(data) }
            btnReply.setOnClickListener { onClickReply(data) }

            btnLike.setOnClickListener {

                btnLike.isEnabled = false

                onClickLike(data) {evaluation ->

                    if (allComments[position].likes == null) {
                        allComments[position].likes = mutableListOf()
                        allComments[position].likes!!.add(evaluation)
                        btnLike.setTextColor(0x3586F6)
                    } else {
                        if (allComments[position].likes!!.contains(evaluation)) {
                            allComments[position].likes!!.remove(evaluation)
                            btnLike.setTextColor(0x000000)
                        } else {
                            allComments[position].likes!!.add(evaluation)
                            btnLike.setTextColor(0x3586F6)
                        }
                    }

                    GlobalScope.launch(Dispatchers.Main) {
                        btnLike.isEnabled = true
                        notifyDataSetChanged()
                    }
                }
            }
        }

        return view
    }

    class CommentViewHolder(view: View) {
        val avatar: ImageView = view.findViewById(R.id.cmtOwnerAvatar)
        val userName: TextView = view.findViewById(R.id.userCommentName)
        val cmtContent: TextView = view.findViewById(R.id.userCommentContent)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtLikeCount: TextView = view.findViewById(R.id.txtUpvoteCount)
        val btnLike: TextView = view.findViewById(R.id.btnLike)
        val btnReply: TextView = view.findViewById(R.id.btnReply)
    }
}