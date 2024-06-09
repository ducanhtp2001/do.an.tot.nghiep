package com.example.commyproject.ultil.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.commyproject.R
import com.example.commyproject.activities.profile.ProfileAct
import com.example.commyproject.data.model.FollowerResponse
import com.example.commyproject.ultil.Constant
import com.example.commyproject.ultil.loadAvatar
import com.example.commyproject.ultil.loadImg

class PeopleRCAdapter(
    private val context: Context,
    private val layoutId: Int,
    private val list: List<FollowerResponse>,
) : RecyclerView.Adapter<PeopleRCAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.avatar)
        val userName: TextView = view.findViewById(R.id.userName)
        val item: View = view.findViewById(R.id.item)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
//        context.loadImg(data.avatar, holder.avatar)
        context.loadAvatar(data._id, holder.avatar)
        holder.userName.text = data.userName
        holder.item.setOnClickListener {
            context.startActivity(Intent((context as Activity), ProfileAct::class.java).apply {
                putExtra(Constant.USER_ID, data._id)
            })
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}