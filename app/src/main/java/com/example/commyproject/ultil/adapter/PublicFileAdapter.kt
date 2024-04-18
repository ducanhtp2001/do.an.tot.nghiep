package com.example.commyproject.ultil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.commyproject.R
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.ultil.converter.FileConverter

class PublicFileAdapter(
    val context: Context,
    private val list: MutableList<FileEntry>

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

        viewHolder.upvote.text = "$upvoteCount Upvote"
        viewHolder.comment.text = "$commentCount Comments"



        return view
    }

    class PrivateViewHolder(view: View) {
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtContent: TextView = view.findViewById(R.id.txtContent)
        val upvote: TextView = view.findViewById(R.id.upvote)
        val comment: TextView = view.findViewById(R.id.comment)
        val commentListView: ListView = view.findViewById(R.id.listViewComment)
    }
}