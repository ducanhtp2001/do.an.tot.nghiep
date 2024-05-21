package com.example.commyproject.ultil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.commyproject.R
import com.example.commyproject.data.model.FileEntry
import com.example.commyproject.ultil.converter.FileConverter

class PrivateFileAdapter(
    val context: Context,
    private val list: MutableList<FileEntry>,
    private val onClick: (FileEntry) -> Unit,
    private val onOpenMenu: (FileEntry) -> Unit,
    ): BaseAdapter() {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_private_file, parent, false)
            viewHolder = PrivateViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as PrivateViewHolder
        }

        val data = list[position]

        viewHolder.apply {
            txtTitle.text = data.title
            txtTime.text = FileConverter.getTimePassFromId(data._id)
            txtContent.text = data.summaryText
            itemView.setOnClickListener {
                onClick(data)
            }
            menu.setOnClickListener {
                onOpenMenu(data)
            }
        }

        return view
    }

    class PrivateViewHolder(view: View) {
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtContent: TextView = view.findViewById(R.id.txtContent)
        val menu: ImageView = view.findViewById(R.id.menu)
        val itemView: View = view.findViewById(R.id.itemView)
    }
}