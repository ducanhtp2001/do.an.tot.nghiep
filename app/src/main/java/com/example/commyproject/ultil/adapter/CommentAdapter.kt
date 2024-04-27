package com.example.commyproject.ultil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.commyproject.R
import com.example.commyproject.data.model.Comment
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.converter.FileConverter

class CommentAdapter(
    private val context: Context,
    private val allComments: List<Comment> = emptyList(),
    private val onGoToUserProfile: () -> Unit,
    private val onClickLike: () -> Unit,
//    private val sendComment: (CommentEntity, callback: (Comment) -> Unit) -> Unit,
    private val onClickReply: () -> Unit
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

        viewHolder.apply {

//            commentLayout.visibility = View.GONE

            val avatarUrl = Config.SERVER_URL + data.avatar

            Glide.with(context)
                .load(avatarUrl)
                .into(avatar)

            userName.text = data.userName
            cmtContent.text = data.content
            txtTime.text = FileConverter.getTimePassFromId(data._id)
            txtUpvoteCount.text = data.like.toString()

//            data.replies?.let {
//                if (it.isNotEmpty()) {
//                    txtReplyCount.text =
//                        context.getString(R.string.comments_count_txt, data.replies?.size.toString())
//                }
//            } ?: {
//                txtReplyCount.visibility = View.GONE
//            }


            btnReply.setOnClickListener { onClickReply() }
            btnLike.setOnClickListener { onClickLike() }

//            btnSend.setOnClickListener {
//                if (viewHolder.inputReply.text.toString().isNotEmpty()) {
//                    // id if this comment, generate by userId and current time
//                    val id = FileConverter.generateIdByUserId(data.idUser)
//                    //
//                    val cmtId = commentId ?: data._id
//                    val comment = CommentEntity(id, userId, fileId, cmtId, EvaluationEntityType.COMMENT, viewHolder.inputReply.text.toString())
//                    sendComment(comment) {
//                        inputReply.setText("")
//                        currentComments.add(it)
//                        GlobalScope.launch(Dispatchers.Main) {
//                            notifyDataSetChanged()
//                        }
//                    }
//                }
//            }

//            val adapter = CommentAdapter(context, userId, fileId, data._id, data.replies, createContextMenu, sendUpvote, sendComment, onClickReply)
//            listViewReply.adapter = adapter

        }

        return view
    }

    class CommentViewHolder(view: View) {
        val avatar: ImageView = view.findViewById(R.id.cmtOwnerAvatar)
        val userName: TextView = view.findViewById(R.id.userCommentName)
        val cmtContent: TextView = view.findViewById(R.id.userCommentContent)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtUpvoteCount: TextView = view.findViewById(R.id.txtUpvoteCount)
//        val txtReplyCount: TextView = view.findViewById(R.id.txtReplyCount)
//        val listViewReply: ListView = view.findViewById(R.id.listViewReply)
        val btnLike: TextView = view.findViewById(R.id.btnLike)
        val btnReply: TextView = view.findViewById(R.id.btnReply)
//        val btnSend: ImageView = view.findViewById(R.id.btnSend)
//        val inputReply: EditText = view.findViewById(R.id.inputReply)
//        val commentLayout: View = view.findViewById(R.id.commentLayout)
    }
}