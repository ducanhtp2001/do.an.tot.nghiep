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
import com.example.commyproject.data.model.Evaluation
import com.example.commyproject.ultil.Config
import com.example.commyproject.ultil.loadAvatar

class LikeAdapter(
    private val context: Context,
    private val list: MutableList<Evaluation>,
    private val openProfile: (evaluation: Evaluation) -> Unit
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
        val viewHolder: LikeViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_like, parent, false)
            viewHolder = LikeViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as LikeViewHolder
        }

        val data = list[position]

        viewHolder.apply {
//            val avatarUrl = Config.SERVER_URL + data.avatar
//            Glide.with(context)
//                .load(avatarUrl)
//                .into(avatar)
            context.loadAvatar(data.idUser, avatar)

            userName.text = data.userName
            item.setOnClickListener {
                openProfile(data)
            }
        }

        return view
    }

    class LikeViewHolder(view: View) {
        val avatar: ImageView = view.findViewById(R.id.avatar)
        val userName: TextView = view.findViewById(R.id.txtUserName)
        val item: View = view.findViewById(R.id.itemLike)
    }
}