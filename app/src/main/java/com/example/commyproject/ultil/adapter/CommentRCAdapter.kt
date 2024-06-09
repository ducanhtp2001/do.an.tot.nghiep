package com.example.commyproject.ultil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.commyproject.R
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.converter.FileConverter
import com.example.commyproject.ultil.loadAvatar

class CommentRCAdapter(
    private val context: Context,
    private val idUser: String,
    private val allComments: MutableList<Comment> = mutableListOf(),
    private val onGoToUserProfile: (cmt: Comment) -> Unit,
    private val onClickLike: (cmt: Comment, callback: (evaluation: Evaluation) -> Unit) -> Unit,
    private val onClickReply: (cmt: Comment) -> Unit,
    private val onOpenLike: (cmt: Comment) -> Unit
): RecyclerView.Adapter<CommentRCAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.cmtOwnerAvatar)
        val userName: TextView = view.findViewById(R.id.userCommentName)
        val cmtContent: TextView = view.findViewById(R.id.userCommentContent)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val btnOpenLike: View = view.findViewById(R.id.btnOpenLike)
        val btnLike: TextView = view.findViewById(R.id.btnLike)
        val txtLikeCount: TextView = view.findViewById(R.id.txtLikeCount)
        val btnReply: TextView = view.findViewById(R.id.btnReply)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return allComments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = allComments[position]

        val likeColor = ContextCompat.getColor(context, R.color.like)
        val nonLikeColor = ContextCompat.getColor(context, R.color.black)
        // neu user like thi nut like mau xanh
        data.likes?.let {
            for (item in it) {
                if (item.idUser == idUser) holder.btnLike.setTextColor(likeColor)
                else holder.btnLike.setTextColor(nonLikeColor)
            }
        }

        holder.apply {
//            val avatarUrl = Config.SERVER_URL + data.avatar
//
//            Glide.with(context)
//                .load(avatarUrl)
//                .into(avatar)
            context.loadAvatar(data.idUser, avatar)

            userName.text = data.userName
            cmtContent.text = data.content
            txtTime.text = FileConverter.getTimePassFromId(data._id)
            txtLikeCount.text = data.likes?.size?.toString() ?: "0"
            btnOpenLike.setOnClickListener {
                onOpenLike(data)
            }

            avatar.setOnClickListener { onGoToUserProfile(data) }
            userName.setOnClickListener { onGoToUserProfile(data) }
            btnReply.setOnClickListener { onClickReply(data) }

            btnLike.setOnClickListener {

                btnLike.isEnabled = false

                onClickLike(data) {evaluation ->

                    if (allComments[position].likes == null) {
                        allComments[position].likes = mutableListOf()
                        allComments[position].likes!!.add(evaluation)
                        btnLike.setTextColor(likeColor)
                    } else {
                        if (allComments[position].likes!!.any{ it.idUser == evaluation.idUser }) {
                            allComments[position].likes?.removeIf { it.idUser == evaluation.idUser }
                            btnLike.setTextColor(nonLikeColor)
                        } else {
                            allComments[position].likes!!.add(evaluation)
                            btnLike.setTextColor(likeColor)
                        }
                    }

                    btnLike.post {
                        btnLike.isEnabled = true
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }
}