package com.example.commyproject.ultil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.commyproject.R
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.EvaluationEntityType
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.converter.FileConverter

class CommentAdapter(
    private val context: Context,
    private val allComments: List<Comment>? = emptyList(),
    private val createContextMenu: () -> Unit,
    private val sendUpvote: (vote: Evaluation) -> Unit,
    private val sendComment: (CommentEntity) -> Unit,
    private val onClickReply: () -> Unit
): BaseAdapter() {

    private val currentComments = mutableListOf<Comment>().apply {
        if (allComments?.isNotEmpty() == true) {
            add(allComments[0])
        }
    }

    fun loadAllComment() {
        currentComments.clear()
        allComments?.let {
            currentComments.addAll(it)
        }
    }

    override fun getCount(): Int {
        return currentComments.size
    }

    override fun getItem(position: Int): Any {
        return currentComments[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

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

        val data = currentComments[position]

        viewHolder.apply {

            commentLayout.visibility = View.GONE

            val avatarUrl = Config.SERVER_URL + data.avatar

            Glide.with(context)
                .load(avatarUrl)
                .into(avatar)

            userName.text = data.userName
            cmtContent.text = data.comment
            txtTime.text = FileConverter.getTimePassFromId(data._id)
            txtUpvoteCount.text = data.votes.size.toString()
            txtReplyCount.text = data.replies.size.toString()

            menu.setOnClickListener { createContextMenu() }
            btnUpvote.setOnClickListener {
                val id = "${System.currentTimeMillis()}_${data.idUser}"
                val upvote = Evaluation(id, data.idUser, EvaluationEntityType.COMMENT, data._id)
                sendUpvote(upvote)
            }

            btnReply.setOnClickListener { onClickReply() }

            btnSend.setOnClickListener {
                if (viewHolder.inputReply.text.toString().isNotEmpty()) {
                    val id = FileConverter.generateIdByUserId(data.idUser)
                    val comment = CommentEntity(id, null, EvaluationEntityType.FILE, viewHolder.inputReply.text.toString())
                    sendComment(comment)
                }
            }

            val adapter = CommentAdapter(context, data.replies, createContextMenu, sendUpvote, sendComment, onClickReply)
            listViewReply.adapter = adapter

        }

        return view
    }

    class CommentViewHolder(view: View) {
        val avatar: ImageView = view.findViewById(R.id.cmtOwnerAvatar)
        val userName: TextView = view.findViewById(R.id.userCommentName)
        val cmtContent: TextView = view.findViewById(R.id.userCommentContent)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtUpvoteCount: TextView = view.findViewById(R.id.txtUpvoteCount)
        val txtReplyCount: TextView = view.findViewById(R.id.txtReplyCount)
        val listViewReply: ListView = view.findViewById(R.id.listViewReply)
        val menu: ImageView = view.findViewById(R.id.option)
        val btnUpvote: ImageView = view.findViewById(R.id.btnUpvote)
        val btnReply: ImageView = view.findViewById(R.id.btnReply)
        val btnSend: ImageView = view.findViewById(R.id.btnSend)
        val inputReply: EditText = view.findViewById(R.id.inputReply)
        val commentLayout: View = view.findViewById(R.id.commentLayout)
    }
}