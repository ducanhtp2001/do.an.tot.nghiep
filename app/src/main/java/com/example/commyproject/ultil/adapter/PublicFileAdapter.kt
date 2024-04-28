package com.example.commyproject.ultil.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.commyproject.R
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.ultil.converter.FileConverter


class PublicFileAdapter(
    private val context: Context,
    private val userId: String,
    private val list: MutableList<FileEntry>,
//    private val sendComment: (CommentEntity, callback: (Comment) -> Unit) -> Unit,
    private val createContextMenu: (file: FileEntry) -> Unit,
    private val hideFile: (file: FileEntry, callback:() -> Unit) -> Unit,
    private val onOpenLikeDialog: (file: FileEntry) -> Unit,
    private val onClickComment: (file: FileEntry) -> Unit,
    private val onItemClick: (file: FileEntry) -> Unit,

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

        viewHolder.apply {
            txtContent.text = data.summaryText
            moreContent.setOnClickListener {
                if (txtContent.maxLines + 20 < Int.MAX_VALUE) {
                    txtContent.maxLines += 20
                } else {
                    txtContent.maxLines = Int.MAX_VALUE
                    it.visibility = View.GONE
                }
            }
        }

        viewHolder.apply {
            title.setOnClickListener {
                onItemClick(data)
            }
            txtContent.setOnClickListener {
                onItemClick(data)
            }

            like.text = context.getString(R.string.upvote, data.likes?.size ?: 0)
            like.setOnClickListener {
                onOpenLikeDialog(data)
            }

            comment.text = context.getString(R.string.comments, data.comments?.size ?: 0)
            comment.setOnClickListener {
                onClickComment(data)
            }

            btnMenu.setOnClickListener {
                createContextMenu(data)
            }
        }

        return view
    }

    class PrivateViewHolder(view: View) {

        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtContent: TextView = view.findViewById(R.id.txtContent)
        val like: TextView = view.findViewById(R.id.like)
        val comment: TextView = view.findViewById(R.id.comment)
        val moreContent: TextView = view.findViewById(R.id.btnMoreContent)
        val btnMenu: ImageView = view.findViewById(R.id.btnMenu)
        val btnDelete: ImageView = view.findViewById(R.id.btnDelete)

        val title: View = view.findViewById(R.id.title)
    }

}