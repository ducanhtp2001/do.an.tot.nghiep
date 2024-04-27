package com.example.commyproject.ultil.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.commyproject.R
import com.example.commyproject.data.model.Comment
import com.example.commyproject.data.model.CommentEntity
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.databinding.ItemPublicFileBinding
import com.example.commyproject.ultil.converter.FileConverter


class PublicFileAdapter(
    private val context: Context,
    private val userId: String,
    private val list: MutableList<FileEntry>,
//    private val sendComment: (CommentEntity, callback: (Comment) -> Unit) -> Unit,
    private val createContextMenu: (idFile: String) -> Unit,
    private val hideFile: (idFile: String) -> Unit,
    private val onClickLike: (idFile: String) -> Unit,
    private val onClickComment: (idFile: String) -> Unit,
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
            viewHolder = PrivateViewHolder(context, view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as PrivateViewHolder
        }

        val data = list[position]

        viewHolder.txtTitle.text = data.title
        viewHolder.txtTime.text = FileConverter.getTimePassFromId(data._id)


        // set event click on read more to expand the textview to see all content
        val spanString = SpannableString(
            context.getString(
                R.string.read_more_content,
                data.summaryText
            )
        )
        viewHolder.txtContent.text = spanString

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                viewHolder.txtContent.maxLines = Int.MAX_VALUE
            }
        }
        spanString.setSpan(
            clickableSpan,
            data.summaryText.length + 3,
            spanString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        viewHolder.like.text = context.getString(R.string.upvote, data.likes?.size ?: 0)
        viewHolder.like.setOnClickListener {
            onClickLike(data._id)
        }

        viewHolder.comment.text = context.getString(R.string.comments, data.comments?.size ?: 0)
        viewHolder.like.setOnClickListener {
            onClickComment(data._id)
        }


        viewHolder.b.apply {
            btnMenu.setOnClickListener {
                createContextMenu(data._id)
            }

            btnDelete.setOnClickListener {
                hideFile(data._id)
            }
        }


//        viewHolder.btnMoreComment.setOnClickListener {  }
//        viewHolder.btnSend.setOnClickListener {
//            context.showToast("send")
//            if (viewHolder.inputComment.text.toString().isNotEmpty()) {
//                val id = FileConverter.generateIdByUserId(data.idUser)
//                val comment = CommentEntity(id, userId, data._id, null, viewHolder.inputComment.text.toString())
//                sendComment(comment) {
//                    viewHolder.inputComment.setText("")
//                    data.comments?.add(it)
//
//                    GlobalScope.launch(Dispatchers.Main) {
//                        notifyDataSetChanged()
//                    }
//                }
//            }
//        }

//        val adapter = CommentAdapter(context, userId, data._id, null, data.comments, createContextMenu, sendUpvote, sendComment, onClickReply = {
//            viewHolder.inputComment.requestFocus()
//        })
//        viewHolder.commentListView.adapter = adapter

        return view
    }

    class PrivateViewHolder(context: Context, view: View) {

        val b: ItemPublicFileBinding =
            ItemPublicFileBinding.inflate(LayoutInflater.from(context), null, false)

        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtContent: TextView = view.findViewById(R.id.txtContent)
        val like: TextView = view.findViewById(R.id.like)
        val comment: TextView = view.findViewById(R.id.comment)
//        val btnMenu: ImageView = view.findViewById(R.id.btnMenu)
//        val btnDelete: ImageView = view.findViewById(R.id.btnDelete)
    }

}