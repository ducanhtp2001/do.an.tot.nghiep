package com.example.commyproject.ultil.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.example.commyproject.R
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.ultil.converter.FileConverter

class PublicFileAdapter(
    val context: Context,
    private val list: MutableList<FileEntry>,
    val getMoreComments: () -> Unit,
    val sendComment: (String) -> Unit,
    private val createContextMenu: () -> Unit,
    private val sendUpvote: () -> Unit,

) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("StringFormatMatches")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: PrivateViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_public_file, parent, false)
            viewHolder = PrivateViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as PrivateViewHolder
        }

        val data = list[position]

        viewHolder.txtTitle.text = data.title
        viewHolder.txtTime.text = FileConverter.getTimePassFromId(data._id)
        viewHolder.txtContent.text = data.summaryText

        var commentCount = 0
        if (data.comments?.isNotEmpty() == true) {
            commentCount = data.comments.size
        }

        var upvoteCount = 0
        if (data.evaluation?.isNotEmpty() == true) {
            upvoteCount = data.evaluation.size
        }

        viewHolder.upVote.text = context.getString(R.string.upvote, upvoteCount)
        viewHolder.comment.text = context.getString(R.string.comments, commentCount)

        viewHolder.btnMoreComment.setOnClickListener { getMoreComments() }
        viewHolder.btnSend.setOnClickListener { sendComment(viewHolder.inputComment.text.toString()) }

        val adapter = CommentAdapter(context, data.comments, createContextMenu, sendUpvote, sendComment)
        viewHolder.commentListView.adapter = adapter

        return view
    }

    class PrivateViewHolder(view: View) {
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtContent: TextView = view.findViewById(R.id.txtContent)
        val upVote: TextView = view.findViewById(R.id.upvote)
        val comment: TextView = view.findViewById(R.id.comment)
        val btnMoreComment: TextView = view.findViewById(R.id.btnMoreComments)
        val commentListView: ListView = view.findViewById(R.id.listViewComment)
        val inputComment: EditText = view.findViewById(R.id.inputReply)
        val btnSend: ImageView = view.findViewById(R.id.btnSend)
    }
}