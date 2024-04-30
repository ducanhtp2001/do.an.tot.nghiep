package com.example.commyproject.ultil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.commyproject.R
import com.example.commyproject.data.model.FollowerResponse
import com.example.commyproject.ultil.loadImg

class PeopleRCAdapter(
    private val context: Context,
    private val list: List<FollowerResponse>,
) : RecyclerView.Adapter<PeopleRCAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.avatar)
        val userName: TextView = view.findViewById(R.id.userName)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_people, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        context.loadImg(data.avatar, holder.avatar)
        holder.userName.text = data.userName
    }

    override fun getItemCount(): Int {
        return list.size
    }

}